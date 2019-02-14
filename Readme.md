# CTool 用户协作工具

## 用户故事地图协作工具



### 模块

- apiAndModel：共享Model，和Dubbo服务接口与API
- userservice：用户登录注册管理，利用redis实现Session共享。kafka作为消息中间件。
- pservice：项目管理，Board-Lane-Card结构，使用netty+websocket实现。

### 配置
- mysql - 默认端口3306
- zookeeper 默认端口2181
- redis 默认端口6379
- Kafka 默认端口9092，使用外置zookeeper
- netty的websocket端口为12345,测试url为`/wstest`

### 数据库

#### MySQL

MYSQL DUMP  /pservice/src/test/resources/Board-init.sql

- 五个表user，board，lane，card，board_user_relation
- 其中包含有外键约束
- 外键约束的执行为`ON DELETE CASCADE ON UPDATE CASCADE` ，即删除或更新主表，则外键表也跟着删除与修改。

#### Redis

​	session共享为Redis实现，session的内置Attribute->userid为登录凭证，**这个方法未在跨域情况下测试。**

​	登录限制（即一旦用户在其他地方登录，则之前的登录则失效需要重新登录，同一时刻只允许一个用户登录同一账号），同样使用Redis实现，基本方法为使用`UserID:sessionID`键值对确定用户。



### Kafka事件机制

使用kafka作为消息中间件，每一种事件为一个Topic，EventProvider 与 EventConsumer分别作为提供者与消费者。



### 前后端信息传输

#### 基本规则

后台接收前端的信息大部分使用application/json放入HTTP的Body中，后台返回的响应信息绝大部分为JSON字符串（少数为页面的重定向与静态资源），字段与描述如下。

|             字段             | 调用成功 |   调用失败   |      类型       |
| :--------------------------: | :------: | :----------: | :-------------: |
|             code             |    0     |   >0的数字   |       int       |
|             msg              |    无    |   失败信息   |     String      |
|            detail            |    无    | 失败消息细节 |     String      |
| 其他的业务字段如user,members |    有    |      无      | Object/List/Map |





##### 解码与编码

​	解码与编码主要是在pservice模块下的`/ctool/board/service/ActionService`的`handlerJsonCode`方法中。目前缺少操作失败返回的错误报告种类，后台只有成功与失败，可以考虑在后期增加报错信息。



