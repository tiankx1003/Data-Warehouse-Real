# 需求概述

## 1.离线处理架构


## 2.实时处理架构


## 3.需求


# 一、服务器环境说明

CentOS 6.8
JDK 1.8
Scala 2.11.8
Hadoop 2.7.2
Zookeeper 3.4.10
Kafka 2.11
HBase 1.3.1
MySQL 5.6
Phoenix 4.14.2
Canal 1.1.2
Nginx 1.12.2

# 二、Maven模块说明

module | description
 :- | :-:
gmall-common | 通用代码
gmall-mock | 模拟数据生成
gmall-logger | 启动数据采集服务器
gmall-realtime | 实时处理模块
gmall-publisher | 数据接口发布
gmall-canal | Canal -> Kafka
gmall-chart | 可视化


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
