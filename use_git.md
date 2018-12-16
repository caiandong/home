

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
  
    更新本地的所有远程分支指针，并不合并
## 合并分支
  * git merge branch_name
  
    branch_name既可以是本地分支也可以是远程分支指针
