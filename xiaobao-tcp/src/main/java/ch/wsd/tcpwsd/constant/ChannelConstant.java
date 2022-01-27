package ch.wsd.tcpwsd.constant;

import io.netty.util.AttributeKey;

public class ChannelConstant {
    /**
     * 设备read空闲的次数（用于离线判断）
     */
    public static final AttributeKey<Integer> readIdleCount = AttributeKey.valueOf("readIdleCount");
    /**
     * 设备的编号
     */
    public static final AttributeKey<String> deviceNum = AttributeKey.valueOf("deviceNum");
}
