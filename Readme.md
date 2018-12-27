# CTool 用户协作工具

## 用户故事地图协作工具



### 模块

- apiAndModel：共享Model，和Dubbo服务接口与API
- userservice：用户登录注册管理，利用redis实现Session共享。session->userid为登录凭证。
- pservice：项目管理，Board-Lane-Card结构，初定使用websocket实现。

### 配置
mysql - 默认端口3306
zookeeper 默认端口2181
redis 默认端口6379





数据库dump

/pservice/src/test/resources/Board-init.sql



