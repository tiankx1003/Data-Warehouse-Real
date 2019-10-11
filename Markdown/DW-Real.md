# 需求概述

## 1.离线处理架构


## 2.实时处理架构


## 3.需求

1. 当日活跃用户及分时趋势
2. 当日交易额及分时趋势
3. 风险预警
4. 灵活查询

```
需求一 日活数据与分时趋势
模拟生产随机启动日志数据
数据采集到Kafka(SpringBoot Tomcat)
数据采集的负载均衡(Nginx)
从Kafka消费数据
实时处理业务逻辑(SparkStreaming)
数据写入到HBase(Phoenix)
日活统计数据查询接口(SpringBoot Tomcat)
数据可视化展示(ECharts)

需求二 业务数据与分时趋势
模拟生成业务数据到MySQL
从Canal读取数据到Kafka
从Kafka消费数据
实时处理业务逻辑(SparkStreaming)
数据写入到HBase(Phoenix)
日活统计数据查询接口(SpringBoot Tomcat)
数据可视化展示(ECharts)

需求三 实时预警
模拟生产随机启动日志数据
数据采集到Kafka(SpringBoot Tomcat)
数据采集的负载均衡(Nginx)
从Kafka消费数据
实时处理业务逻辑(SparkStreaming)
数据保存到ElasticSearch
Kibana完成可视化

需求四 灵活查询

```


# 三、操作说明

 * 服务器端执行SpringBoot工程gmall-logger
```bash
# 方法一
java -cp /opt/gmall/jars/gmall-logger-0.0.1-SNAPSHOT.jar:$SCALA_HOME/lib/scala-library.jar org.springframework.boot.loader.JarLauncher
# 方法二
java -Djava.ext.dirs=/opt/module/scala-2.11.8/lib -cp ./gmall-logger-0.0.1-SNAPSHOT.jar org.springframework.boot.loader.JarLauncher
```
 * 进程启停
```bash
# HBase
start-hbase.sh
stop-hbase.sh
# Phoenix
$PHOENIX_HOME/bin/sqlline.py hadoop102,hadoop103,hadoop104:2181
!quit
# Canal
$CANAL_HOME/bin/startup.sh
$CANAL_HOME/bin/stop.sh
```

 * Kafka操作
```bash
# 显示当前所有topic

# 创建topic

# 删除topic

# 生产消息

# 消费消息

```

 * Redis操作
```sql
```

 * HBase操作
```sql
```

 * MySQL执行脚本
```sql
CALL init_data('2019-10-10',10000,200,TRUE);
```

 * 常用端口和链接
```
HBase

```
