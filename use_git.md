
是一个学习过后来查漏补缺的记录，不是初学者教程
没想到更好的表述方式，以每一个条目为分割，先命令再解释，与先解释再命令表述是等价的。
较短的格式会放在同一行，希望别糊涂

============

# [git官方中文教程](https://git-scm.com/book/zh/v2)
# [README.md基本编写](https://www.cnblogs.com/shiy/p/6526868.html)
# [markdown语法说明](https://www.appinn.com/markdown)
   
## 用户配置
    $ git config --global user.name "John Doe"
    $ git config --global user.email johndoe@example.com
    
## 检查配置信息

    如果想要检查你的配置，可以使用 git config --list 命令来列出所有 Git 当时能找到的配置。
# 特别的，github生成ssh密钥
    $ ssh-keygen -t rsa -b 4096 -C "your_email@example.com"
## 仓库
  * git init
  
    初始化一个仓库
  * git clone [仓库名]  [仓库的本地名]
  
    如果本地名省略。默认为 origin
    
## 日志
  * git log
  ```text
    -p
    --graph
    --pretty=
  ```
## 提交前操作
    
  * git commit --amend 
    
    如果在提交过后突然又觉得此次提交不妥，或者内容增加或改变，甚至提交说明中某个用词想改变下。
    通常直接 git add . 重新提交下。这里是一个缓解的方式。比方，规定长途汽车在发车最后五分钟时迟到者不允许进了。
    你通过某种方式打破这条规定，进去了。就好像没有迟到。
    这条命令和例子很像。这是个危险的命令，把当前暂存区数据提交，结果就是合并前一次提交，
    看上去所有提交都是，最后的，一次性的，提交。
    
  * git reset HEAD <文件名>
  
    把HEAD指针指向的树内的文件放到index区（暂存区）中，用来覆盖本次add后的结果，工作区内的内容不受影响。
    
  * git checkout -- <文件>
    
    与上一条命令一样，唯一不同的是，它会把工作区的内容覆盖，这是危险的命令，导致 未提交的修改丢失。
    
## 分支
  * git branch -vv
  
     详细查看分支
  * git branch --merged

     详细查看已合并分支;--no-merged是未合并分支
  * git branch [newbranch_name]
  
    新建分支
  * git checkout [branch_name]
  
    切换到分支
  * git branch -b [newbranch_name]
  
    新建分支并且切换到新分支
  * git branch -d [branch_name]
  
    删除分支 -D强制删除，即使没有合并
    
## 远程仓库 

  * 添加远程仓库
  
    git remote add <anyname> <url>
  * 重命名仓库
   
    git remote rename oldname newname
    
  * 删除远程仓库
  
    git remote rm remotename
 
## 跟踪远程分支
  * git checkout -b [name]  [remotename/remotebranch] 
  
    新建分支跟踪远程分支
  
  *  跟踪同名远程分支快捷方式
  
    git checkout --track [remotename/remotebranch]
    
    等价于
    
    git checkout -b [remotebranch] [remotename/remotebranch]
    
  * git branch -u [remotename/remotebranch] 
  
    更换当前分支跟踪远程分支或者添加跟踪远程分支
    
## 推送远程分支
  * git push [remotename]  [localbranch] 
  
    推送本地分支作为远程分支
  
    如果不存在远程分支名为`localbranch`，那么会生成新的名为`localbranch`的远程分
    
    支（并不会自动跟踪，下同）
    
    如果存在，则更新同名远程分支
    
  * git push [remotename]  [localbranch:remotebranch] 
    
    推送不同名分支
    不存在，则生成。存在则更新。
  * git push [remotename] --delete [remotebranch]
  
    此命令用来删除远程分支
    
## 抓取远程仓库数据
  * git fetch remotename 
  
    抓取远程仓库数据放到本地，并不合并
    
## 合并分支
  * git merge branch_name
  
    branch_name既可以是本地分支也可以是远程分支指针
