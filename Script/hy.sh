#!/bin/bash
case $1 in
# 传参为1时开启，0关闭
"1"){ 
	mr-jobhistory-daemon.sh start historyserver
    start-dfs.sh
    ssh hadoop103 'start-yarn.sh'
};;
"0"){
    stop-dfs.sh
    mr-jobhistory-daemon.sh stop historyserver
    ssh hadoop103 'stop-yarn.sh'
};;
esac

