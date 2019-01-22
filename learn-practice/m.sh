#!/bin/bash
#fsdfdfsf
# IFS=$'\n'
# for name in $(cat ~/桌面/name)
# do
#     echo $name
#     done
# echo ==================
# count=0
# count2=5
# ((count2++))
# echo "fafaf $count2"
# while (($count<10))
# do
#    count=$[$count+1] 
#     echo  "count:$count"
#     if (( count2--))
#     then
#         echo "sssscount$count2"
#     fi
# done 

test()
{
 count=2
 local x=3
 echo "函数内$count";
}
test2()
{
echo $@
    for v in $@
    do
        echo $v
    done
}
array=(1 2 3 4)

test2 ${array[*]}

# echo "函数外count:$count"
# echo "函数外x:$x"

