# famous-login

    login 登录服，玩家注册、登录。 
    redisson缓存玩家token防篡改
    rpc获取玩家uniqueID
    
    
* [登录服接口测试-2019-08-20](../famous-static/doc/login/login-api.md)    


## 开发中遇到的问题

* Q: rpcConfig注入失败
* A: 在FamousLoginApplication添加 @ComponentScan({"com.liema.login.*", "com.liema.common.*"})，扫描注入
* S: RpcConfig在common工程在login服注入失败，因此需要扫描common工程。
