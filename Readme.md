# CTool 用户协作工具

## 用户故事地图协作工具



### 模块

- apiAndModel：共享Model，和Dubbo服务接口与API
- userservice：用户登录注册管理，利用redis实现Session共享。session->userid为登录凭证。
- pservice：项目管理，Board-Lane-Card结构，使用netty+websocket实现。

### 配置
mysql - 默认端口3306
zookeeper 默认端口2181
redis 默认端口6379

### 数据库

/pservice/src/test/resources/Board-init.sql

- 四个表User，Board，Lane，Card
- 其中包含有外键约束
- 外键约束的执行为ON DELETE CASCADE ON UPDATE CASCADE ，即删除或更新主表，则外键表也跟着删除与修改。



### Websocket json

##### json 格式

|   属性名    |             格式              |  类型  |          作用           |           是否必需            |
| :---------: | :---------------------------: | :----: | :---------------------: | :---------------------------: |
|  board_id   |           b_112233            | String |         看板ID          |              是               |
|   user_id   |            112233             | String |         用户ID          |              是               |
|   action    | "update"/"delete"/"insert"... | String |    N者取一，规定动作    |              是               |
|   entity    |   "card"/"lane",/board"....   | String | N者取一，规定操作的类型 |              是               |
|   card_id   |           c_112233            | String |         卡片ID          |              否               |
|   lane_id   |           l_112233            | String |         泳道ID          | 否，注意添加card时必有lane_id |
|   content   |             xxxxx             | String |          内容           |              否               |
| description |             xxxxx             | String |          描述           |              否               |
|     msg     |         success/fail          | String |   描述后台操作的结果    |         后台必须提供          |
|   detail    |            xxxxxx             | String | 描述后台操作失败的描述  |       msg为fail时存在。       |





##### 解码与编码

​	解码与编码主要是在pservice模块下的`/ctool/board/service/ActionService`的`handlerJsonCode`方法中。目前缺少操作失败返回的错误报告种类，后台只有成功与失败，可以考虑在后期增加报错信息。

