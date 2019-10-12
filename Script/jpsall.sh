#!/bin/bash
for i in `cat /opt/module/hadoop-2.7.2/etc/hadoop/slaves`
do
    echo -e "\033[31m ======== $i ======== \033[0m"
    ssh $i 'jps'
done
