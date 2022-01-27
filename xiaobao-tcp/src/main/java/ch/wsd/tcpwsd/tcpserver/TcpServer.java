package ch.wsd.tcpwsd.tcpserver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.EventExecutorGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TcpServer {
    @Value("${tcp.port}")
    private Integer port;
    @Value("${tcp.heartbeat}")
    private Integer heartbeat;
    @Value("${tcp.package-handle}")
    private Boolean packageHandle;

    private final EventLoopGroup bossGroup;
    private final EventLoopGroup workerGroup;
    private final ServerBootstrap b;
    private final EventExecutorGroup businessExecutors;
    private final TcpServerHandler handler;

    public void run() throws Exception {
        try {
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    //设置netty的日志级别
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new IdleStateHandler(heartbeat, 0, 0));
                            if (packageHandle) {
                                //解决粘包/拆包：以\r\n(0d0a)为结尾分割符号。
                                //注意：加上这个之后，在handler的channelRead方法里就读不到\r\n(0d0a)这两个字节了
                                ByteBuf delimiter = Unpooled.copiedBuffer("\r\n".getBytes());
                                p.addLast(new DelimiterBasedFrameDecoder(64, delimiter));
                            }
                            //用业务线程池处理业务
                            p.addLast(businessExecutors, handler);
                        }
                    });

            // Bind and start to accept incoming connections.
            ChannelFuture f = b.bind(port).sync();

            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
