#!/bin/bash
ssh-keygen
for i in hadoop102 hadoop103 hadoop104
do
    echo "======== $i ========"
    ssh-copy-id $i
done
