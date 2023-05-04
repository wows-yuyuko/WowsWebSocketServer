# WowsWebSocketServer

`基于jdk20开发 springboot 3.0.6`

wows 数据推送服务

# 客户端自定义生成唯一的ID

# 接收端扫码获取客户端ID去服务器订阅数据

# 上报地址 POST类型

`http://127.0.0.1:7899/wows/real/battle/report/data`

设置请求头放入唯一ID `wows-client-id`

设置`Content-Type`为`application/json`

结构:

![6({6BTMYUPQR68RYQEVZU33](https://user-images.githubusercontent.com/28168503/236216279-822a091e-b3b9-43ce-a9bb-362c7ceaed80.png)

# ws订阅地址

`ws://127.0.0.1:7899/ws/wows/report/push`

# 连接时请发送ping消息

`[ping:这里是你从客户端获取的唯一ID]`

该消息可以作为一个ping发送,服务器收到会回复同样的消息

# 接收订阅

在发送ping消息后,如果服务器收到客户端发上来的数据会直接转发给订阅端
