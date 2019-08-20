# famous-login

## 注册 register

**Request**

~~~json
{
	"cmd":"103",
	"account":"snowflow",
	"password":"123456"
}
~~~

**Response**

~~~json
{
    "msg": "success",
    "code": 0,
    "cmd": 104
}
~~~


## 登录 login

**Request**

~~~json
{
	"cmd":"101",
	"account":"snowflow",
	"password":"123456"
}
~~~

**Response**

~~~json
{
    "msg": "success",
    "code": 0,
    "cmd": 102
}
~~~
