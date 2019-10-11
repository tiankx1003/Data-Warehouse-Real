#!/bin/bash
case $1  in
    "1") {
        for i in `cat /opt/module/hadoop-2.7.2/etc/hadoop/slaves`
        do
            ssh $i  "source /etc/profile;$ES_HOME/bin/elasticsearch >$ES_HOME/logs/es.log 2>&1 &"

        done

        nohup $KIBANA_HOME/bin/kibana >$KIBANA_HOME/kibana.log 2>&1 &

    };;
    "0") {
        ps -ef|grep ${KIBANA_HOME} |grep -v grep|awk '{print $2}'|xargs kill

        for i in `cat /opt/module/hadoop-2.7.2/etc/hadoop/slaves`
        do
            ssh $i "ps -ef|grep $ES_HOME |grep -v grep|awk '{print \$2}'|xargs kill" >/dev/null 2>&1
        done


    };;
    *){
        echo "你启动的姿势不正确, 请使用参数 1 来启动es集群, 使用参数 0 来关闭es集群"
    };;
esac
