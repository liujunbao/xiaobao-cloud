# Getting Started

### 技术说明
tcp-server基础搭建，使用到了netty和rocketmq
- #### netty粘包/拆包处理
本项目用到了netty自带的解码器：分割符-DelimiterBasedFrameDecoder。当然还有其他的，
比如固定长度-FixedLengthFrameDecoder、长度字段-LengthFieldBasedFrameDecoder等。

如果netty自带的解码器不能满足需求，可以自定义解码器（见下面的参考文档）。
### 协议
基于modbus rtu协议。  
程序中有一些魔法值，具体请看协议

| 换行符（newline） | \n | 0a |
|-------------|---|---|
 | 回车符（return） | \r | 0d |

### 部署
- docker部署
### docker命令
- ```shell
  #trunc意思是截取
  docker ps --no-trunc
  ```

  
运行docker目录下的deploy.sh

### 调试
- ubuntu20.04（命令行）
```bash
#-n:去掉换行符
#-e:解释反斜杠转义字符
#nc：网络工具，主要用于tcp、udp相关操作
#> tcp.log将server端发送的消息存储到tcp.log
echo -n -e "\x2\x1" | nc localhost 8080 > tcp.log
#然后vim tcp.log，在vim命令状态下，输入:%!xxd，就会将当前文本转换为16进制格式
```
- ubuntu20.04（网络调试工具）
> 没找到网络调试工具
- window11
> 应该有很多网络调试工具
### 参考文档

- [Netty用户指南](https://netty.io/wiki/user-guide-for-4.x.html)
- [Netty Demo](https://github.com/netty/netty/tree/4.1/example)
- [Rocketmq集成Springboot](https://github.com/apache/rocketmq-spring)
- [Netty自定义解码器-粘包/拆包](https://blog.csdn.net/qincidong/article/details/82656593)