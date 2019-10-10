#! /bin/bash
# 启动Kafka时要先开启JMX端口，是用于后续KafkaManager监控
case $1 in
"1"){
        for i in `cat $HADOOP_HOME/etc/hadoop/slaves`
        do
                echo " --------启动 $i Kafka-------"
                # 用于KafkaManager监控
                ssh $i "export JMX_PORT=9988 && kafka-server-start.sh -daemon $KAFKA_HOME/config/server.properties "
                echo $?
        done
};;
"0"){
        for i in `cat $HADOOP_HOME/etc/hadoop/slaves`
        do
                echo " --------停止 $i Kafka-------"
                ssh $i "kafka-server-stop.sh stop"
                echo $?
        done
};;
esac

