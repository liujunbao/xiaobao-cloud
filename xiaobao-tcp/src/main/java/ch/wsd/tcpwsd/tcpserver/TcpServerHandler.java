package ch.wsd.tcpwsd.tcpserver;

import ch.wsd.tcpwsd.constant.ChannelConstant;
import ch.wsd.tcpwsd.constant.DeviceConstant;
import ch.wsd.tcpwsd.constant.TopicConstant;
import ch.wsd.tcpwsd.util.ByteUtil;
import ch.wsd.tcpwsd.util.CrcUtil;
import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.Attribute;
import io.netty.util.ReferenceCountUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Component;

/**
 * 由于集成了spring，所以就加上了@ChannelHandler.Sharable，表示当前类的实例可以被多个channel共享。所以编码时要注意多线路问题。
 * 也可以不用spring的自动注入，使用new实例。
 */
@Component
@Slf4j
@ChannelHandler.Sharable
@RequiredArgsConstructor
public class TcpServerHandler extends ChannelInboundHandlerAdapter {

    private final RocketMQTemplate rocketMQTemplate;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf byteBuf = (ByteBuf) msg;
        try {
            byte[] bytes = ByteBufUtil.getBytes(byteBuf);
            String message = ByteBufUtil.hexDump(bytes);
            log.info("收到报文为：{}", message);
            //todo 报文校验(硬件没做校验，所以我也不能校验)
            //获取设备编号
            String deviceType = ByteUtil.getStrData(bytes, 5, 2);
            int deviceNum = ByteUtil.getIntData(bytes, 7, 4);
            String sn = deviceType + deviceNum;
            log.info("设备编号为：{}", sn);
            //绑定设备编号到channel
            ctx.channel().attr(ChannelConstant.deviceNum).set(sn);
            //重置读空闲次数为1（用于离线判断）
            ctx.channel().attr(ChannelConstant.readIdleCount).set(1);
            //异步发送到rocketmq
            sendToMq(bytes);
            //回应设备
            response(bytes, ctx, sn);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("netty捕获异常", cause);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.READER_IDLE) {
                Attribute<Integer> attr = ctx.channel().attr(ChannelConstant.readIdleCount);
                int count = attr.get() == null ? 1 : attr.get();
                String sn = ctx.channel().attr(ChannelConstant.deviceNum).get();
                sn = sn == null ? DeviceConstant.device_never_send_msg : sn;
                if (count < 3) {
                    log.info("设备{}心跳超时{}次", sn, count);
                    attr.set((count + 1));
                } else {
                    log.info("设备{}心跳超时{}次,关闭channel", sn, count);
                    ctx.close();
                }
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    //-------------------------------------------------------以下为自定义方法---------------------------------------------

    /**
     * 发送数据到rocketmq
     *
     * @param bytes 数据
     */
    private void sendToMq(byte[] bytes) {
        //功能码
        String functionCode = ByteUtil.getStrData(bytes, 19, 1);
        //0a代表写功能，意思就是设备给我数据
        if ("0a".endsWith(functionCode)) {
            JSONObject jsonObject = new JSONObject();
            //温度
            int temp = ByteUtil.getIntData(bytes, 28, 2);
            //湿度
            int humidity = ByteUtil.getIntData(bytes, 30, 2);
            log.info("温度为：{}，湿度为：{}", temp, humidity);
            jsonObject.put("temp", temp / 10);
            jsonObject.put("humidity", humidity / 10);
            rocketMQTemplate.asyncSend(TopicConstant.real_time_topic, jsonObject.toJSONString(), new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    log.info("rocketmq发送消息成功，返回消息为：{}", sendResult.toString());
                }

                @Override
                public void onException(Throwable throwable) {
                    log.error("rocketmq发送消息失败，错误信息为", throwable);
                }
            });
        }
    }

    /**
     * 回应设备 - 目前只有正常回应
     *
     * @param bytes 接受到的数据
     * @param ctx   ctx
     * @param sn    设备编号
     */
    private void response(byte[] bytes, ChannelHandlerContext ctx, String sn) {
        int registerSize = ByteUtil.getIntData(bytes, 17, 2, true);
        int registerLength = ByteUtil.getIntData(bytes, 22, 2, true);
        //要回应的报文
        byte[] response = new byte[bytes.length - registerSize * registerLength];
        System.arraycopy(bytes, 0, response, 0, bytes.length - registerSize * registerLength - 4);
        //0x0b：正常回应
        response[19] = 0x0b;
        //crc16-modbus校验
        byte[] check = new byte[response.length - 1];
        System.arraycopy(response, 1, check, 0, check.length);
        check = CrcUtil.crc16ModbusReturnArray(check);
        //报文结尾
        byte[] tail = {0x0d, 0x0a};
        System.arraycopy(check, 0, response, response.length - 4, check.length);
        System.arraycopy(tail, 0, response, response.length - 4 + check.length, tail.length);

        ByteBuf out = ctx.alloc().buffer();
        out.writeBytes(response);
        ctx.channel().writeAndFlush(out).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                log.info("回应报文{}给设备{}成功", ByteBufUtil.hexDump(response), sn);
            }
        });
    }
}
