#! /bin/bash
case $1 in
# 传参为1时开启，0关闭
"1"){ 
	hy 1
    zk 1
    kf 1
    start-hbase.sh
    es 1
    startup.sh
    jpsall
};;
"0"){
    es 0
    stop-hbase.sh
    kf 0
    sleep 8s;
    zk 0
    hy 0
    stop.sh
    jpsall
};;
esac