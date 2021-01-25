# Kafka入门

## web sites

- **home**: http://kafka.apache.org/

- **wiki**: https://cwiki.apache.org/confluence/display/KAFKA/Index

- **生态**: https://cwiki.apache.org/confluence/display/KAFKA/Ecosystem

## from wikipedia

> Kafka from领英，2011年初开源，并于2012年10月23日由Apache Incubator孵化出站。2014年11月，几个曾在领英为Kafka工作的工程师，创建了confluent.io新公司。根据2014年Quora的帖子，Jay Kreps似乎已经将它以作家弗朗茨·卡夫卡命名，它是“一个用于优化写作的系统”。

## different with MessageQueue

> common mq: RabbitMQ, Redis, ActiveMQ, zeroMQ, rocketMQ，数据库？应用服务器间消息传递

> kafka: 主要用于处理活跃的流式数据,大数据量的数据处理上.

## get started

1. 下载安装, 可以配置KAFKA_HOME和PATH

    ```bash
    export KAFKA_HOME="/data/tools/kafka"
    export PATH="$PATH:$KAFKA_HOME/bin"
    ```

2. 单机配置: 另选zookeeper port, 一些工作目录 /tmp/ -> 正常目录(注意磁盘和内存大小？daily-disk-clean.sh)

- zookeeper config(`zookeeper.properties`)

    ```ini
    dataDir=/data/appdata/kafka/zookeeper
    clientPort=32181
    ```

- kafka server config(`server.properties`)

    ```ini
    zookeeper.connect=localhost:32181
    log.dirs=/data/appdata/kafka/kafka-logs
    log.retention.hours=90
    log.retention.bytes=61073741824
    ```

3. 启动 zookeeper & kafka，包装启动脚本，放到``$HOME/bin``

- `kafka_zk_start`

    ```bash
    #!/bin/bash
    cd $KAFKA_HOME
    nohup bin/zookeeper-server-start.sh config/zookeeper.properties &
    ```

- `kafka_server_start`

    ```bash
    #!/bin/bash
    cd $KAFKA_HOME
    nohup bin/kafka-server-start.sh config/server.properties &
    ```

4. 创建topic

- create

    ```bash
    # create
    kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic test
    ```

- list

    ```bash
    kafka-topics.sh --list --bootstrap-server localhost:9092
    ```

> ``--bootstrap-server`` 指定kafka server，也可用``--zookeeper``

5. 启动producer发送信息

    ```bash
    kafka-console-producer.sh --bootstrap-server localhost:9092 --topic test
    ```

> 不创建topic，直接指定topic发消息，也会自动创建。``advoracle/cli/tool/userlog2kafka.py``

6. 启动consumer接收消息

    ```bash
    kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test --from-beginning
    ```

> - 可以看到正在发送的消息，也可看到之前发送的消息
> - 去掉``--from-beginning``, 只能看到正在发送的消息

## Kafka Web Manager

https://github.com/yahoo/CMAK

scala项目, 效果: http://localhost:9000/

## 客户端

> https://cwiki.apache.org/confluence/display/KAFKA/Clients

- python: [confluent-kafka](https://github.com/confluentinc/confluent-kafka-python)

## others

1. 集群部署
2. Connect组件导入导出迁移数据
3. Kafka Stream, 致力于流式计算框架，like flink。所以还有个产品叫ksqlDB
