#!/bin/sh

# -t 表示指定镜像仓库名称/镜像名称:镜像标签 .表示使用当前目录下的Dockerfile
docker build -t tcp-wsd:0.0.1-SNAPSHOT .

# 查找是否已经存在tcp-wsd容器，存在的话就删除
if docker ps -a | grep tcp-wsd; then
    echo "删除tcp-wsd容器"
    # awk '{print $1}'等于tcp-wsd容器的id
    docker rm -f $(docker ps -a | grep tcp-wsd | awk '{print $1}');
else
    echo "没有找到tcp-wsd容器"
fi

# 启动容器
# -d 后台运行
# ${ref_port}等于-p 9999:8080 将容器的 8080 端口映射到主机的 9999 端口
# ${ref_volume}等于-Xms=256m -Xmx=512m意思是作为命令添加到ENTRYPOINT后面执行
# -v /home/xiaobao/docker:/home/xiaobao/logs  挂载主机的目录/home/xiaobao/docker到容器的目录/home/xiaobao/logs（保存日志）
# -v /etc/localtime:/etc/localtime:ro表示把主机的/etc/localtime挂载到容器中，保证容器时间和主机的一致，ro代表只读
docker run -d --name tcp-wsd -p 9999:8080 -v /home/xiaobao/docker:/home/xiaobao/logs -v /etc/localtime:/etc/localtime:ro tcp-wsd:0.0.1-SNAPSHOT -Xms=256m -Xmx=512m