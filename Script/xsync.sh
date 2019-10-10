#!/bin/bash

pcount=$#

if ((pcount==0)); then
echo no args;
exit;
fi

p1=$1
fname=`basename $p1`
echo fname=$fname
pdir=`cd -P $(dirname $p1); pwd`
echo pdir=$pdir
user=`whoami`

for i in hadoop102 hadoop103 hadoop104
do
	echo ------------------- $i -------------------
	rsync -av $pdir/$fname $user@$i:$pdir
done
