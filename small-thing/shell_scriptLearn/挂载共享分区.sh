#!/bin/bash

echo "root" | sudo -S mount /dev/sda6 ~/windows
if [ $? = 0 ]
then
    echo "挂载~/windows成功 "
fi
sudo mount /dev/sda8 ~/linux2

if [ $? = 0 ]
then
    echo "挂载~/linux2成功 关闭退出"
fi
