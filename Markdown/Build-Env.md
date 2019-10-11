## Nginx


## Phoenix
```bash
tar -zxvf apache-phoenix-4.14.2-HBase-1.3-bin.tar.gz -C /opt/module
mv apache-phoenix-4.14.2-HBase-1.3-bin phoenix
cp phoenix-4.14.2-HBase-1.3-server.jar /opt/module/hbase-1.3.1/lib
cp phoenix-4.14.2-HBase-1.3-client.jar /opt/module/hbase-1.3.1/lib
sudo vim /etc/properties
```
```properties
export PHOENIX_HOME=/opt/module/phoenix
export PHOENIX_CLASSPATH=$PHOENIX_HOME
export PATH=$PATH:$PHOENIX_HOME/bin
```

## Canal
 * 配置mysql bin-log
```bash
sudo vim /etc/my.cnf
sudo service mysql restart
mysql -uroot -proot
```
```conf
[mysqld]
server-id= 1
log-bin= mysql-bin
binlog_format= row
# binlog-do-db= gmall
```
```sql
show variables like 'log_%';
-- log_bin                     on
-- log_bin_basename            /var/lib/mysql/mysql-bin
-- log_bin_index               /var/lib/mysql/mysql-bin.index
-- log_bin_use_v1_row_events   off
```
 * 安装配置Canal
```bash
wget https://github.com/alibaba/canal/releases/download/canal-1.1.2/canal.deployer-1.1.2.tar.gz
mkdir /opt/module/canal
tar -zxvf canal.deployer-1.1.2.tar.gz -C /opt/module/canal
vim conf/canal.properties
vim conf/example/instance.properties
```
```conf
canal.port=11111
```
```conf
#################################################
## mysql serverId , v1.0.26+ will autoGen
# slaveId 必须配置, 不能和 mysql 的 id 重复
canal.instance.mysql.slaveId=100

# enable gtid use true/false
canal.instance.gtidon=false

# position info
# mysql 的位置信息
canal.instance.master.address=hadoop102:3306
canal.instance.master.journal.name=
canal.instance.master.position=
canal.instance.master.timestamp=
canal.instance.master.gtid=

# rds oss binlog
canal.instance.rds.accesskey=
canal.instance.rds.secretkey=
canal.instance.rds.instanceId=

# table meta tsdb info
canal.instance.tsdb.enable=true
#canal.instance.tsdb.url=jdbc:mysql://127.0.0.1:3306/canal_tsdb
#canal.instance.tsdb.dbUsername=canal
#canal.instance.tsdb.dbPassword=canal

#canal.instance.standby.address =
#canal.instance.standby.journal.name =
#canal.instance.standby.position =
#canal.instance.standby.timestamp =
#canal.instance.standby.gtid=

# username/password
# 用户名和密码
canal.instance.dbUsername=root
canal.instance.dbPassword=root
canal.instance.connectionCharset = UTF-8
canal.instance.defaultDatabaseName =
# enable druid Decrypt database password
canal.instance.enableDruid=false
#canal.instance.pwdPublicKey=MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBALK4BUxdDltRRE5/zXpVEVPUgunvscYFtEip3pmLlhrWpacX7y7GCMo2/JM6LeHmiiNdH1FWgGCpUfircSwlWKUCAwEAAQ==

# table regex
canal.instance.filter.regex=.*\\..*
# table black regex
canal.instance.filter.black.regex=

# mq config
canal.mq.topic=example
canal.mq.partition=0
# hash partition config
#canal.mq.partitionsNum=3
#canal.mq.partitionHash=mytest.person:id,mytest.role:id
#################################################
```


## Redis


## HBase


## ElasticSearch


## Kibana




