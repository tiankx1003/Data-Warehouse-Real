#!/bin/bash
for i in hadoop102 hadoop103 hadoop104
do
    echo -e "\033[31m ======== $i ======== \033[0m"
    ssh $i 'jps'
done
