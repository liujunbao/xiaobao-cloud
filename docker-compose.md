### 安装

- ~~用的是v1版本，py写的，v2改用go写了~~

```shell
sudo curl -L "https://github.com/docker/compose/releases/download/1.29.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
 ```

- 上面的方法因为一些原因无法下载，所以我选择用浏览器下载
    - 打开[github地址](https://github.com/docker/compose/releases), 同样选择1.29.2版本， 然后下载哪个呢？
        - windows就直接选择windows了，因为就一个
        - linux的话，按docker-compose-$(uname -s)-$(uname -m)在命令行输入uname -s和uname -m
    - 复制一份到/usr/local/bin里，并修改名称为docker-compose
  ```shell
  # 如果没有/usr/local/bin/docker，就手动创建一下，用mkdir
  sudo cp docker-compose-Linux-x86_64 /usr/local/bin/docker-compose
  ```
- 赋予可执行权限
    ```shell
    sudo chmod +x /usr/local/bin/docker-compose
    ```
- 测试是否安装成功
    ```shell
    docker-compose --version
    #docker-compose version 1.29.2, build 5becea4c
    ```
### 卸载
```shell
sudo rm /usr/local/bin/docker-compose
```
### 开发
[]
### 参考文档

- [https://docs.docker.com/compose/install/](https://docs.docker.com/compose/install/)
- [https://docs.docker.com/compose/gettingstarted/](https://docs.docker.com/compose/gettingstarted/)
- [版本兼容](https://docs.docker.com/compose/compose-file/)