package ch.wsd.tcpwsd.util;

import io.netty.buffer.ByteBufUtil;

public class ByteUtil {
    public static void intToByteArray(int value, byte[] bytes) {
        intToByteArray(value, bytes, 0, 4, false);
    }

    /**
     * int转byte数组
     *
     * @param value  int数值
     * @param bytes  要转换成的byte数组
     * @param offset 数组的偏移量，也就是从第几个index开始
     * @param count  转成几个字节
     * @param isBigEnd 是否是大端模式
     */
    public static void intToByteArray(int value, byte[] bytes, int offset, int count, boolean isBigEnd) {
        for (int i = 0; i < count; ++i) {
            if (isBigEnd) {
                bytes[offset + i] = (byte) (value >> 8 * (count - 1 -i) & 255);
            } else {
                bytes[offset + i] = (byte) (value >> 8 * i & 255);
            }
        }
    }

    public static int byteArrayToInt(byte[] bytes) {
        return byteArrayToInt(bytes, 0, 4, false);
    }

    /**
     * 字节数组转int，跟上面的方法intToByteArray刚好相反
     *
     * @param bytes    源字节数组
     * @param offset   数组的偏移量，也就是从第几个index开始
     * @param count    几个字节
     * @param isBigEnd 是否是大端模式
     * @return int数值
     */
    public static int byteArrayToInt(byte[] bytes, int offset, int count, boolean isBigEnd) {
        int value = 0;

        for (int i = 0; i < count; ++i) {
            if (isBigEnd) {
                value |= (bytes[offset + i] & 255) << 8 * (count - 1 - i);
            } else {
                value |= (bytes[offset + i] & 255) << 8 * i;
            }
        }

        return value;
    }

    /**
     * 字节数组 相邻的每两个字节转换成一个字节
     *
     * @param bytes 目标字节数组
     * @return 转换后的字节数组
     */
    public static byte[] tweByteToOneByte(byte[] bytes) {
        byte[] result = new byte[bytes.length / 2];
        for (int i = 0; i < bytes.length - 1; i++) {
            result[i] = tweByteToOneByte(bytes[i], bytes[i + 1]);
        }
        return result;
    }

    public static byte tweByteToOneByte(byte b1, byte b2) {
        return (byte) (b1 << 4 | b2);
    }

    /**
     * 从字节数组里获取数据
     *
     * @param srcBytes 目标字节数组
     * @param srcPos   数据在字节数组的位置
     * @param length   数据的字节数
     * @return 数据
     */
    public static String getStrData(byte[] srcBytes, int srcPos, int length) {
        byte[] destBytes = new byte[length];
        System.arraycopy(srcBytes, srcPos, destBytes, 0, destBytes.length);
        return ByteBufUtil.hexDump(destBytes);
    }

    /**
     * 从字节数组里获取数据
     *
     * @param srcBytes 源字节数组
     * @param srcPos   数据在字节数组的位置
     * @param length   数据的字节数
     * @return 数据
     */
    public static int getIntData(byte[] srcBytes, int srcPos, int length) {
        return getIntData(srcBytes, srcPos, length, false);
    }

    /**
     * 从字节数组里获取数据
     *
     * @param srcBytes 源字节数组
     * @param srcPos   数据在字节数组的位置
     * @param length   数据的字节数
     * @param isBigEnd 是否是大端模式
     * @return 数据
     */
    public static int getIntData(byte[] srcBytes, int srcPos, int length, boolean isBigEnd) {
        byte[] destBytes = new byte[length];
        System.arraycopy(srcBytes, srcPos, destBytes, 0, destBytes.length);
        return ByteUtil.byteArrayToInt(destBytes, 0, destBytes.length, isBigEnd);
    }

    public static void main(String[] args) {
        byte[] bytes = {0x01, 0x02};
        int i = byteArrayToInt(bytes, 0, 2, false);
        System.out.println(i);
        byte[] bytes1 = new byte[2];
        intToByteArray(i, bytes1, 0, 2, false);
        System.out.println(bytes1[0] + "," + bytes1[1]);

        byte[] bytes2 = tweByteToOneByte(bytes);
        System.out.println(bytes2[0]);
    }
}
