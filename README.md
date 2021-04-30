# 1.Nacos集群配置

### a. 下载

[下载地址]: https://github.com/alibaba/nacos
[官方文档]: https://nacos.io/zh-cn/index.html

### b. 安装

#### 单机版

从github下载安装包(linux/windows),已linux为例  

​	获取nacos 安装包后 对文件进行解压

```shell
tar -xvf nacos-server-2.0.1.tar.gz
```

由于我选择较高版本nacos，官方支持MySQL数据源，这里我选择使用MySQL方便观察数据的基本存储情况，若选择内嵌数据库可忽略本步骤。

1. 官方要求MySQL版本5.6.5+

2. 初始化mysql数据库，数据库初始化脚本在刚刚压缩包下的conf文件夹下的 nacos-mysql.sql
3. 修改conf/application.properties文件，增加支持mysql数据源配置（目前只支持mysql），添加mysql数据源的url、用户名和密码

```shell
[root@localhost nacos]# cd conf/
[root@localhost conf]# ll
total 88
-rw-r--r--. 1  502 games  1224 Apr 19 16:55 1.4.0-ipv6_support-update.sql
-rw-r--r--. 1  502 games  8504 Apr 30 13:50 application.properties
-rw-r--r--. 1  502 games  6515 Apr 19 16:55 application.properties.example
-rw-r--r--. 1 root root     97 Apr 30 13:51 cluster.conf
-rw-r--r--. 1  502 games   670 Mar 18 11:36 cluster.conf.example
-rw-r--r--. 1  502 games 31156 Apr 29 16:37 nacos-logback.xml
-rw-r--r--. 1  502 games 10660 Apr 19 16:55 nacos-mysql.sql
-rw-r--r--. 1  502 games  8795 Apr 19 16:55 schema.sql
[root@localhost conf]# 

```

修改application.properties 文件 ,将下面注释放开 使用自己的数据库

```shell
### If use MySQL as datasource:
 spring.datasource.platform=mysql

### Count of DB:
 db.num=1

### Connect URL of DB:
 db.url.0=jdbc:mysql://127.0.0.1:3306/nacos?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true&useUnicode=true&useSSL=false&serverTimezone=UTC
 db.user.0=nacos
 db.password.0=nacos
```

nacos默认启动方式为 cluster 集群模式 ，单机启动需要参数 -m standalone

```shell
# 进入到 nacos bin目录下
./startup.sh -m standalone
#查看 logs 目录下 start.out日志文件 观察
#输出 Nacos started successfully in stand alone mode. use external storage
即为启动成功


```

启动成功在浏览器输入 `http://192.168.56.1:8848/nacos/index.html`即可访问 默认用户名密码均为nacos

#### 集群安装

在单机版的基础上添加集群配置

```shell
# 在conf 目录下
cp cluster.conf.example cluster.conf
# 修改cluster.conf文件  设置为集群机器ip
# ip:port
192.168.56.21:8848
192.168.56.22:8848
192.168.56.23:8848
```

进入bin 目录 执行启动脚本即可

```shell
./startup.sh
#查看 logs 目录下 start.out日志文件 观察
#输出  INFO Nacos started successfully in cluster mode. use external storage
# 即为启动成功
```

配置nginx服务器

nginx下载安装不再赘述，有需要可自行百度，主要是添加配置

```shell
# 部分配置文件
	upstream cluster{
        server 192.168.56.21:8848;
        server 192.168.56.22:8848;
        server 192.168.56.23:8848;
    }
    server {
        listen       80;
        server_name  localhost;
        #charset koi8-r;
        #access_log  logs/host.access.log  main;

        location / {
        #    root   html;
        #    index  index.html index.htm;
          proxy_pass http://cluster;
        }
    }

```

#### 客户端注册nacos集群遇到的一些问题

集群正常启动，客户应用注册不进nacos集群，控制台一直输出错误日志，应用无法启动，部分异常日志如下

```java
com.alibaba.nacos.api.exception.NacosException: <html><body><h1>Whitelabel Error Page</h1><p>This application has no explicit mapping for /error, so you are seeing this as a fallback.</p><div id='created'>Fri Apr 30 16:32:56 CST 2021</div><div>There was an unexpected error (type=Bad Request, status=400).</div><div>receive invalid redirect request from peer 192.168.56.21</div></body></html>
	at com.alibaba.nacos.client.naming.net.NamingProxy.callServer(NamingProxy.java:611) [nacos-client-1.3.2.jar:na]
	at com.alibaba.nacos.client.naming.net.NamingProxy.reqApi(NamingProxy.java:524) [nacos-client-1.3.2.jar:na]
	at com.alibaba.nacos.client.naming.net.NamingProxy.reqApi(NamingProxy.java:491) [nacos-client-1.3.2.jar:na]
	at com.alibaba.nacos.client.naming.net.NamingProxy.reqApi(NamingProxy.java:486) [nacos-client-1.3.2.jar:na]
	at com.alibaba.nacos.client.naming.net.NamingProxy.registerService(NamingProxy.java:239) [nacos-client-1.3.2.jar:na]
	at com.alibaba.nacos.client.naming.NacosNamingService.registerInstance(NacosNamingService.java:200) [nacos-client-1.3.2.jar:na]
	at com.alibaba.cloud.nacos.registry.NacosServiceRegistry.register(NacosServiceRegistry.java:70) [spring-cloud-starter-alibaba-nacos-discovery-2.2.2.RELEASE.jar:2.2.2.RELEASE]
	at org.springframework.cloud.client.serviceregistry.AbstractAutoServiceRegistration.register(AbstractAutoServiceRegistration.java:239) [spring-cloud-commons-2.2.5.RELEASE.jar:2.2.5.RELEASE]
	at com.alibaba.cloud.nacos.registry.NacosAutoServiceRegistration.register(NacosAutoServiceRegistration.java:76) [spring-cloud-starter-alibaba-nacos-discovery-2.2.2.RELEASE.jar:2.2.2.RELEASE]
	at org.springframework.cloud.client.serviceregistry.AbstractAutoServiceRegistration.start(AbstractAutoServiceRegistration.java:138) [spring-cloud-commons-2.2.5.RELEASE.jar:2.2.5.RELEASE]
	at org.springframework.cloud.client.serviceregistry.AbstractAutoServiceRegistration.bind(AbstractAutoServiceRegistration.java:101) [spring-cloud-commons-2.2.5.RELEASE.jar:2.2.5.RELEASE]
	at org.springframework.cloud.client.serviceregistry.AbstractAutoServiceRegistration.onApplicationEvent(AbstractAutoServiceRegistration.java:88) [spring-cloud-commons-2.2.5.RELEASE.jar:2.2.5.RELEASE]
	at org.springframework.cloud.client.serviceregistry.AbstractAutoServiceRegistration.onApplicationEvent(AbstractAutoServiceRegistration.java:47) [spring-cloud-commons-2.2.5.RELEASE.jar:2.2.5.RELEASE]
	at org.springframework.context.event.SimpleApplicationEventMulticaster.doInvokeListener(SimpleApplicationEventMulticaster.java:172) [spring-context-5.2.11.RELEASE.jar:5.2.11.RELEASE]
	at org.springframework.context.event.SimpleApplicationEventMulticaster.invokeListener(SimpleApplicationEventMulticaster.java:165) [spring-context-5.2.11.RELEASE.jar:5.2.11.RELEASE]
	at org.springframework.context.event.SimpleApplicationEventMulticaster.multicastEvent(SimpleApplicationEventMulticaster.java:139) [spring-context-5.2.11.RELEASE.jar:5.2.11.RELEASE]
	at org.springframework.context.support.AbstractApplicationContext.publishEvent(AbstractApplicationContext.java:404) [spring-context-5.2.11.RELEASE.jar:5.2.11.RELEASE]
	at org.springframework.context.support.AbstractApplicationContext.publishEvent(AbstractApplicationContext.java:361) [spring-context-5.2.11.RELEASE.jar:5.2.11.RELEASE]
	at org.springframework.boot.web.servlet.context.WebServerStartStopLifecycle.start(WebServerStartStopLifecycle.java:46) [spring-boot-2.3.6.RELEASE.jar:2.3.6.RELEASE]
	at org.springframework.context.support.DefaultLifecycleProcessor.doStart(DefaultLifecycleProcessor.java:182) [spring-context-5.2.11.RELEASE.jar:5.2.11.RELEASE]
	at org.springframework.context.support.DefaultLifecycleProcessor.access$200(DefaultLifecycleProcessor.java:53) [spring-context-5.2.11.RELEASE.jar:5.2.11.RELEASE]
	at org.springframework.context.support.DefaultLifecycleProcessor$LifecycleGroup.start(DefaultLifecycleProcessor.java:360) [spring-context-5.2.11.RELEASE.jar:5.2.11.RELEASE]
	at org.springframework.context.support.DefaultLifecycleProcessor.startBeans(DefaultLifecycleProcessor.java:158) [spring-context-5.2.11.RELEASE.jar:5.2.11.RELEASE]
	at org.springframework.context.support.DefaultLifecycleProcessor.onRefresh(DefaultLifecycleProcessor.java:122) [spring-context-5.2.11.RELEASE.jar:5.2.11.RELEASE]
	at org.springframework.context.support.AbstractApplicationContext.finishRefresh(AbstractApplicationContext.java:895) [spring-context-5.2.11.RELEASE.jar:5.2.11.RELEASE]
	at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:554) [spring-context-5.2.11.RELEASE.jar:5.2.11.RELEASE]
	at org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.refresh(ServletWebServerApplicationContext.java:143) [spring-boot-2.3.6.RELEASE.jar:2.3.6.RELEASE]
	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:758) [spring-boot-2.3.6.RELEASE.jar:2.3.6.RELEASE]
	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:750) [spring-boot-2.3.6.RELEASE.jar:2.3.6.RELEASE]
	at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:405) [spring-boot-2.3.6.RELEASE.jar:2.3.6.RELEASE]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:315) [spring-boot-2.3.6.RELEASE.jar:2.3.6.RELEASE]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1237) [spring-boot-2.3.6.RELEASE.jar:2.3.6.RELEASE]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1226) [spring-boot-2.3.6.RELEASE.jar:2.3.6.RELEASE]
	at com.leaf.SmsApplication.main(SmsApplication.java:13) [classes/:na]
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[na:1.8.0_131]
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62) ~[na:1.8.0_131]
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[na:1.8.0_131]
	at java.lang.reflect.Method.invoke(Method.java:498) ~[na:1.8.0_131]
	at org.springframework.boot.devtools.restart.RestartLauncher.run(RestartLauncher.java:49) [spring-boot-devtools-2.3.6.RELEASE.jar:2.3.6.RELEASE]

2021-04-30 16:32:58.473 ERROR 20304 --- [  restartedMain] com.alibaba.nacos.client.naming          : [NA] failed to request

com.alibaba.nacos.api.exception.NacosException: <html><body><h1>Whitelabel Error Page</h1><p>This application has no explicit mapping for /error, so you are seeing this as a fallback.</p><div id='created'>Fri Apr 30 16:32:56 CST 2021</div><div>There was an unexpected error (type=Bad Request, status=400).</div><div>receive invalid redirect request from peer 192.168.56.22</div></body></html>
	at com.alibaba.nacos.client.naming.net.NamingProxy.callServer(NamingProxy.java:611) [nacos-client-1.3.2.jar:na]
	at com.alibaba.nacos.client.naming.net.NamingProxy.reqApi(NamingProxy.java:538) [nacos-client-1.3.2.jar:na]
	at com.alibaba.nacos.client.naming.net.NamingProxy.reqApi(NamingProxy.java:491) [nacos-client-1.3.2.jar:na]
	at com.alibaba.nacos.client.naming.net.NamingProxy.reqApi(NamingProxy.java:486) [nacos-client-1.3.2.jar:na]
	at com.alibaba.nacos.client.naming.net.NamingProxy.registerService(NamingProxy.java:239) [nacos-client-1.3.2.jar:na]
	at com.alibaba.nacos.client.naming.NacosNamingService.registerInstance(NacosNamingService.java:200) [nacos-client-1.3.2.jar:na]
	at com.alibaba.cloud.nacos.registry.NacosServiceRegistry.register(NacosServiceRegistry.java:70) [spring-cloud-starter-alibaba-nacos-discovery-2.2.2.RELEASE.jar:2.2.2.RELEASE]
	at org.springframework.cloud.client.serviceregistry.AbstractAutoServiceRegistration.register(AbstractAutoServiceRegistration.java:239) [spring-cloud-commons-2.2.5.RELEASE.jar:2.2.5.RELEASE]
	at com.alibaba.cloud.nacos.registry.NacosAutoServiceRegistration.register(NacosAutoServiceRegistration.java:76) [spring-cloud-starter-alibaba-nacos-discovery-2.2.2.RELEASE.jar:2.2.2.RELEASE]
	at org.springframework.cloud.client.serviceregistry.AbstractAutoServiceRegistration.start(AbstractAutoServiceRegistration.java:138) [spring-cloud-commons-2.2.5.RELEASE.jar:2.2.5.RELEASE]
	at org.springframework.cloud.client.serviceregistry.AbstractAutoServiceRegistration.bind(AbstractAutoServiceRegistration.java:101) [spring-cloud-commons-2.2.5.RELEASE.jar:2.2.5.RELEASE]
	at org.springframework.cloud.client.serviceregistry.AbstractAutoServiceRegistration.onApplicationEvent(AbstractAutoServiceRegistration.java:88) [spring-cloud-commons-2.2.5.RELEASE.jar:2.2.5.RELEASE]
	at org.springframework.cloud.client.serviceregistry.AbstractAutoServiceRegistration.onApplicationEvent(AbstractAutoServiceRegistration.java:47) [spring-cloud-commons-2.2.5.RELEASE.jar:2.2.5.RELEASE]
	at org.springframework.context.event.SimpleApplicationEventMulticaster.doInvokeListener(SimpleApplicationEventMulticaster.java:172) [spring-context-5.2.11.RELEASE.jar:5.2.11.RELEASE]
	at org.springframework.context.event.SimpleApplicationEventMulticaster.invokeListener(SimpleApplicationEventMulticaster.java:165) [spring-context-5.2.11.RELEASE.jar:5.2.11.RELEASE]
	at org.springframework.context.event.SimpleApplicationEventMulticaster.multicastEvent(SimpleApplicationEventMulticaster.java:139) [spring-context-5.2.11.RELEASE.jar:5.2.11.RELEASE]
	at org.springframework.context.support.AbstractApplicationContext.publishEvent(AbstractApplicationContext.java:404) [spring-context-5.2.11.RELEASE.jar:5.2.11.RELEASE]
	at org.springframework.context.support.AbstractApplicationContext.publishEvent(AbstractApplicationContext.java:361) [spring-context-5.2.11.RELEASE.jar:5.2.11.RELEASE]
	at org.springframework.boot.web.servlet.context.WebServerStartStopLifecycle.start(WebServerStartStopLifecycle.java:46) [spring-boot-2.3.6.RELEASE.jar:2.3.6.RELEASE]
	at org.springframework.context.support.DefaultLifecycleProcessor.doStart(DefaultLifecycleProcessor.java:182) [spring-context-5.2.11.RELEASE.jar:5.2.11.RELEASE]
	at org.springframework.context.support.DefaultLifecycleProcessor.access$200(DefaultLifecycleProcessor.java:53) [spring-context-5.2.11.RELEASE.jar:5.2.11.RELEASE]
	at org.springframework.context.support.DefaultLifecycleProcessor$LifecycleGroup.start(DefaultLifecycleProcessor.java:360) [spring-context-5.2.11.RELEASE.jar:5.2.11.RELEASE]
	at org.springframework.context.support.DefaultLifecycleProcessor.startBeans(DefaultLifecycleProcessor.java:158) [spring-context-5.2.11.RELEASE.jar:5.2.11.RELEASE]
	at org.springframework.context.support.DefaultLifecycleProcessor.onRefresh(DefaultLifecycleProcessor.java:122) [spring-context-5.2.11.RELEASE.jar:5.2.11.RELEASE]
	at org.springframework.context.support.AbstractApplicationContext.finishRefresh(AbstractApplicationContext.java:895) [spring-context-5.2.11.RELEASE.jar:5.2.11.RELEASE]
	at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:554) [spring-context-5.2.11.RELEASE.jar:5.2.11.RELEASE]
	at org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.refresh(ServletWebServerApplicationContext.java:143) [spring-boot-2.3.6.RELEASE.jar:2.3.6.RELEASE]
	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:758) [spring-boot-2.3.6.RELEASE.jar:2.3.6.RELEASE]
	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:750) [spring-boot-2.3.6.RELEASE.jar:2.3.6.RELEASE]
	at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:405) [spring-boot-2.3.6.RELEASE.jar:2.3.6.RELEASE]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:315) [spring-boot-2.3.6.RELEASE.jar:2.3.6.RELEASE]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1237) [spring-boot-2.3.6.RELEASE.jar:2.3.6.RELEASE]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1226) [spring-boot-2.3.6.RELEASE.jar:2.3.6.RELEASE]
	at com.leaf.SmsApplication.main(SmsApplication.java:13) [classes/:na]
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[na:1.8.0_131]
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62) ~[na:1.8.0_131]
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[na:1.8.0_131]
	at java.lang.reflect.Method.invoke(Method.java:498) ~[na:1.8.0_131]
	at org.springframework.boot.devtools.restart.RestartLauncher.run(RestartLauncher.java:49) [spring-boot-devtools-2.3.6.RELEASE.jar:2.3.6.RELEASE]

2021-04-30 16:32:58.563 ERROR 20304 --- [  restartedMain] com.alibaba.nacos.client.naming          : [NA] failed to request

```

通过查找资料文档发现是因为我nacos服务配置问题  因为我服务安装在虚拟机上，配置了两个网卡，启动nacos服务需要指定ip ,找到问题 修改配置重新启动nacos即可.

```properties
# 修改 nacos 启动配置
#将一下注释放开 并指定本机ip
### Specify local server's IP:
nacos.inetutils.ip-address=192.168.56.23

```

