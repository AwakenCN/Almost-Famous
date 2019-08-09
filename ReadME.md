# 《成名之路》(Almost-Famous)

~~~
     _    _                     _        _____                               
    / \  | |_ __ ___   ___  ___| |_     |  ___|_ _ _ __ ___   ___  _   _ ___ 
   / _ \ | | '_ ` _ \ / _ \/ __| __|____| |_ / _` | '_ ` _ \ / _ \| | | / __|
  / ___ \| | | | | | | (_) \__ \ ||_____|  _| (_| | | | | | | (_) | |_| \__ \
 /_/   \_\_|_| |_| |_|\___/|___/\__|    |_|  \__,_|_| |_| |_|\___/ \__,_|___/

~~~

## 1.1 项目架构

~~~
    使用Gradle+SpringBoot构建项目，为此还特意去完整的学习了Gradle整体的相关知识，包括编译Gradle脚本的Groovy语法、
以及Gradle的生命周期，Gradle的核心模块Project、Task等，为此我也想将这部分的学习成果与大家分享    
~~~    

~~~
Root project 'Almost-Famous'
+--- Project ':famous-battle'
+--- Project ':famous-common'
+--- Project ':famous-game'
+--- Project ':famous-login'
\--- Project ':famous-unique'
~~~

* [unique服务的介绍](./famous-unique/README.md)

## 1.2 Gradle学习资料

* [Gradle中文版](https://github.com/DONGChuan/GradleUserGuide)
* [Gradle阅读地址](https://dongchuan.gitbooks.io/gradle-user-guide-/)
    
    
## 1.3 Thrift RPC通信框架    

~~~
    Thrift Facebook开源的通讯、rpc框架    
~~~

* [Thrift生成规则](https://github.com/noseparte/thrift-server) 

* [Banner生成器](http://patorjk.com/software/taag/#p=display&f=Graffiti&t=Type%20Something%20)