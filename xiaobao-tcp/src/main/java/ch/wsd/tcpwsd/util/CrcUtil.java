package ch.wsd.tcpwsd.util;

public class CrcUtil {
    /**
     * ModBus 通信协议的 CRC ( 冗余循环校验码含2个字节, 即 16 位二进制数。
     * CRC 码由发送设备计算, 放置于所发送信息帧的尾部。
     * 接收信息设备再重新计算所接收信息 (除 CRC 之外的部分）的 CRC,
     * 比较计算得到的 CRC 是否与接收到CRC相符, 如果两者不相符, 则认为数据出错。
     * <p>
     * 1) 预置 1 个 16 位的寄存器为十六进制FFFF(即全为 1) , 称此寄存器为 CRC寄存器。
     * 2) 把第一个 8 位二进制数据 (通信信息帧的第一个字节) 与 16 位的 CRC寄存器的低 8 位相异或, 把结果放于 CRC寄存器。
     * 3) 把 CRC 寄存器的内容右移一位( 朝低位)用 0 填补最高位, 并检查右移后的移出位。
     * 4) 如果移出位为 0, 重复第 3 步 ( 再次右移一位); 如果移出位为 1, CRC 寄存器与多项式A001 ( 1010 0000 0000 0001) 进行异或。
     * 5) 重复步骤 3 和步骤 4, 直到右移 8 次,这样整个8位数据全部进行了处理。
     * 6) 重复步骤 2 到步骤 5, 进行通信信息帧下一个字节的处理。
     * 7) 将该通信信息帧所有字节按上述步骤计算完成后,得到的16位CRC寄存器的高、低字节进行交换。
     * 8) 最后得到的 CRC寄存器内容即为 CRC码。
     */
    public static int crc16ModbusReturnInt(byte[] bytes) {
        int CRC = 0x0000ffff;
        int POLYNOMIAL = 0x0000a001;

        int i, j;
        for (i = 0; i < bytes.length; i++) {
            CRC ^= bytes[i];
            for (j = 0; j < 8; j++) {
                if ((CRC & 0x00000001) == 1) {
                    CRC >>= 1;
                    CRC ^= POLYNOMIAL;
                } else {
                    CRC >>= 1;
                }
            }
        }
        //当前结果：高位在左，低位在右边，即大端模式，也就是我们习惯的模式。
        //高低位转换
        //CRC = ( (CRC & 0x0000FF00) >> 8) | ( (CRC & 0x000000FF ) << 8);
        return CRC;
    }

    public static byte[] crc16ModbusReturnArray(byte[] bytes) {
        int CRC = crc16ModbusReturnInt(bytes);
        byte[] result = new byte[2];
        ByteUtil.intToByteArray(CRC, result, 0, 2, false);
        return result;
    }

    public static void main(String[] args) {
        byte[] bytes = {0x01, 0x02, 0x03, 0x04, 0x05};
        int i = crc16ModbusReturnInt(bytes);
        System.out.println(i);
        System.out.println(Integer.toHexString(i));

    }
}
