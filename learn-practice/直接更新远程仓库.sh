#!/bin/bash
git add . 
rtv=$(echo $?) 

if [ $rtv -eq 0 ]
then
    echo "执行成功：git add"
    git commit -a -m $1
else
    echo "执行失败：git add"
    exit 111
fi
rtv=$(echo $?)

if [ $rtv -eq 0 ]
then
    echo "执行成功：git commit"
    git push origin 
else
    echo "执行失败：git commit"
    exit 111
fi
rtv=$(echo $?)
if [ $rtv -eq 0 ]
then
    echo "执行成功：git push"
else
    echo "执行失败：git push"
    exit 111
fi
