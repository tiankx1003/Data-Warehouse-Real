#! /bin/bash

case $1 in
# 传参为1时开启，0关闭，?查看状态
"1"){ 
	for i in `cat /opt/module/hadoop-2.7.2/etc/hadoop/slaves`
	do
		ssh $i "zkServer.sh start"
	done
};;
"0"){

	for i in `cat /opt/module/hadoop-2.7.2/etc/hadoop/slaves`
	do
		ssh $i "zkServer.sh stop"
	done
};;
"?"){
	for i in `cat /opt/module/hadoop-2.7.2/etc/hadoop/slaves`
	do
		ssh $i "zkServer.sh status"
	done
};;
esac

