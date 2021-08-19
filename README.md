# 工程简介
SpringBoot集成kafka,基本使用

## kafka

https://www.w3cschool.cn/apache_kafka/apache_kafka_installation_steps.html





### 优点

顺序写入 使用操作系统的页缓存 sendfile零拷贝 分区分段+索引 批量读写 批量压缩

### 消息模式

点对点

发布订阅(group设置为一致)

### 特点

高性能、持久化、多副本备份、横向扩展能力

### 作用

解耦、削峰、异步处理

### 关键术语

broker

topic

partition,分区对应一个append log文件,实现分布式的集群存储，通过ZK选举一个leader负责全部读写请求，follower只是同步数据。

offset

### 生产消费流程

1. producer会和leader保持socket连接，并通过watch监听ZK的leader变更事件。
2. 写(异步、批量)：topic,[partition],[key],value。（未指定partition，hash(key)选择分区，若key不存在，轮询）。
3. 存。
4. 读：主动pull消息。一个Partition，只能被消费组里的一个消费者消费。（不同group即可实现发布-订阅模式）。
5. 读：消费者消费后会想kafka代理发送确认，kafka修改存储在Zookeeper中的offset。（0.10版本后存在consumeroffsets topic中）

### 消息投递语义

At most once

At least once*

Exactly once

# 延伸阅读

## 快速启动
zookeeper-server-start.bat ..\..\config\zookeeper.properties

kafka-server-start.bat ..\..\config\server.properties

[kafka-tool,一款集成kafka、zookeeper的可视化工具](https://www.cnblogs.com/miracle-luna/p/11299345.html)

## 重置offset
[Kafka Consumer重置Offset脚本](https://cloud.tencent.com/developer/article/1436988)
