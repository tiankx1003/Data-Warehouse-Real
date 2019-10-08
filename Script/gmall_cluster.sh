#!/bin/bash
case $1 in
    "start")
    {
        for i in hadoop102 hadoop103 hadoop104
        do
            echo "========启动日志服务: $i==============="
            ssh $i  "java -cp /opt/gmall/jars/gmall-logger-0.0.1-SNAPSHOT.jar:$SCALA_HOME/lib/scala-library.jar org.springframework.boot.loader.JarLauncher >/dev/null 2>&1  &"
        done
     };;
    "stop")
    {
        for i in hadoop102 hadoop103 hadoop104
        do
            echo "========关闭日志服务: $i==============="
            ssh $i "ps -ef|grep gmall-logger-1.0-SNAPSHOT.jar | grep -v grep|awk '{print \$2}'|xargs kill" >/dev/null 2>&1
        done
    };;
    
    *)
    {
        echo 启动姿势不对, 请使用参数 start 启动日志服务, 使用参数 stop 停止服务
    };;
esac
