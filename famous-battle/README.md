# famous-battle

    battle服使用Netty长连接实现，玩家登录验证、匹配、战斗、心跳监测、断线重连等功能
    
    
* 登录验证
* 匹配
* 战斗
* 心跳监测
* 断线重连
    
    
    
    
    
## 网络游戏同步问题

在学习的同时向我的三位经验丰富的前辈请教过相关理论

* Leader1 (腾讯魔方工作室 ***资深工程师)
~~~
Q: 这个帧客户端跟服务器是怎么交互的
   是客户端实时发送给服务器吗
A: 其实 帧同步  的根本 在于 相同的初始化数据 经过相同的操作，理论最终结果 应该是一致的 
   所以  客户端要把所有的操作 我们称为帧   要先告诉服务器  服务器要把这个信息广播给所有参与的客户端 
Q: 我不太理解这个广播
   是收到的同时返回给客户端吗
A: 就是服务器要把帧 发送给所有参与的客户端呀
   是返还给所有的 客户端  比如王者荣耀  你的每一个操作  都要广播给10个玩家呀   （其中也包括你）
Q: 那pvp就是给对战的俩个人吧
A: 对, 有几个客户端参与 就要发给几个客户端
Q: 我说我理解的啊，假定客户端1秒向服务器发送4帧的数据，然后服务器将这4帧的数据广播给俩个客户端
   都是以pvp模式论
A: 你这 和 状态同步 有啥区别 
   哈哈 你要明白 帧同步的 根本在哪  
Q: 那断线重连之后的帧同步呢
A: 服务器 要把所有的帧都做好缓存 
   断线重连以后 要把游戏的初始状态 和所有帧 全部告诉客户端 
   客户端重新计算当前状态 
Q: 重点在于服务器是否参与计算，参与计算了是状态同步，只是做转发就是帧同步
   我理解的对不对
A: 不对,你根本没有理解啥是帧同步 
   你先好好 百度一下   
   先熟悉熟悉 再说吧   
Q: 好的
A: 你要先理解 啥是帧同步 
   帧同步的根本是什么  
   为什么王者荣耀可以用帧同步  
   为什么 我们的三消可以用帧同步  
   为什么  MMO 不能用帧同步  
A: 好，我先研究一下你说的这几个问题
~~~

* Leader2 (Funplus(趣加) ***资深Unity工程师)
~~~
Q: 想咨询一下游戏中帧同步的问题
A: 好啊，你说
Q: 客户端跟服务器帧是怎么交互的
A: 客户端把操作帧数据发给服务器，服务器再广播出来。所有客户端用广播的数据来执行流程
~~~

* Leader3 (北京猎码网络 ***资深Java工程师)
~~~
Q: 我昨天看了一下帧同步的几篇文章
A: 嗯 有何感想
Q: 大体了解了帧同步的概念和原理
A: 帧同步和状态同步的区别在哪啊
Q: 这篇文章有一个全球同服架构上的演进
A: 简单说就是帧同步对玩家的操作、运算、时间高度一致
Q: 那状态同步呢
A: 状态同步就是你告诉我你现在是什么状态，至于你怎么到的这个状态我是不需要知道的
   帧同步的运算两个客户端都知道对方的变化过程
   状态同步客户端不需要知道变化过程，只需要知道你现在的状态就好了。
Q: 那至于同步之后的广播操作是一样的吧
A: 不一样,状态同步不需要服务器推动时间轴
Q: 那我还有一个问题
A: 你说
Q: 帧同步和状态同步同步的是什么，是怎么做到同步
A: 同步就是广播数据
   状态同步服务器不广播客户端还可以做自己的逻辑
   帧同步客户端就停滞不前了
   区别主要在这里
   也就是上面说的  
   [图片]
   帧同步就是需要推动时间轴
   [图片]
Q: 就是初始状态S0，在t1是接收客户端发过来的帧数据，然后同步广播给客户端
A: 这个图画的不形象
   但你说的这句话就是我上面说的意思
   你只需要记住关键的一点
   服务器是时间轴，不推动谁也不能动
~~~

* 帧同步

* 状态同步  

* [网络游戏中的帧同步与状态同步](../famous-static/doc/almost-famous/frame.md)


### 精选文章 

-- 微信公众号 大码侯

* [像《王者荣耀》一样红过](https://mp.weixin.qq.com/s?__biz=MzI3MTQ1NzU2NA==&mid=2247483868&idx=1&sn=6904c1655d6a9116040bd3d65ac3638a&chksm=eac0cdafddb744b99c0bce4cc44fc842ef121f7b1dd219bde6d23e5272ea7f6daaa4dc83d9af&scene=21#wechat_redirect)
* [从《王者荣耀》来聊聊游戏的帧同步](https://mp.weixin.qq.com/s?__biz=MzI3MTQ1NzU2NA==&mid=2247483875&idx=1&sn=215a6d5965fbd578bf06b6c88496612f&chksm=eac0cd90ddb7448657df188a6c78777e66228cff592f61de2959867148a61a9005b80ab7b54f&mpshare=1&scene=1&srcid=0823qDv6Tenw7q2LTvhNgeXh&sharer_sharetime=1566526210568&sharer_shareid=95320fed4360c46ededbf4fd9173710f&key=7f7fdfb8d9436ed7f72150f432e8ab239ac8a0b20b982900073647eeb1a2cbf51a54d92fed1938a4559b85054c879e2222b91a7b4811ecf05572144622d15bea58d086c2b63ba1ef13673aa4cd45134b&ascene=1&uin=MTEzNzAxNTc0NA%3D%3D&devicetype=Windows+10&version=62060833&lang=zh_CN&pass_ticket=AAPfg3EjYCtUmxSwOBSzM8pD4s1PIaCJszVDI3pP9rFoWnfxbWhUr8CYDZyGN%2FfB)
* [游戏服务器的架构演进(完整版)](https://mp.weixin.qq.com/s?__biz=MzI3MTQ1NzU2NA==&mid=2247483884&idx=1&sn=3547c769a300f1d82cc04e9b1852c6d5&chksm=eac0cd9fddb7448997e38a74e2d26bde259cd2127583e31bc488511bc1fdcd9f35caff27d4a3&mpshare=1&scene=1&srcid=0823OVD2GWpB39V9L22t8c1c&sharer_sharetime=1566525206299&sharer_shareid=95320fed4360c46ededbf4fd9173710f&key=7f7fdfb8d9436ed7da3c8488b6d482d88a9a898b6fdd3cc0dff1d9837fb4096fb6280b14182095b531473b14396627e5ca370172a7d0a2b2b658c5f031bea00ed1e4d7087c8f222f77d674a5a95fe455&ascene=1&uin=MTEzNzAxNTc0NA%3D%3D&devicetype=Windows+10&version=62060833&lang=zh_CN&pass_ticket=AAPfg3EjYCtUmxSwOBSzM8pD4s1PIaCJszVDI3pP9rFoWnfxbWhUr8CYDZyGN%2FfB)
