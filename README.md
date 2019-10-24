# Data-Warehouse-Real
一个应用了Spark生态体系相关技术的数仓实时计算案例。

### 环境说明

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

### Maven模块介绍

module | description
 :- | :-:
gmall-common | 通用代码
gmall-mock | 模拟数据生成
gmall-logger | 启动数据采集服务器
gmall-realtime | 实时处理模块
gmall-publisher | 数据接口发布
gmall-canal | Canal -> Kafka
gmall-chart | 可视化
