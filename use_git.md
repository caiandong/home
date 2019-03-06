
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

### HEAD Index WorkingDirectory

  * git reset --soft ~HEAD 
    
    移动HEAD所指向的当前分支(比如master)到分支的父节点。
    
  * git reset --mixed ~HEAD 
    
    不仅把当前分支移动到父节点，也把索引更新为父节点。这是git reset ~HEAD 的默认行为

####  git reset --mixed ~HEAD
    
    连同当前节点，索引，和工作目录都变为父节点。reset唯一的危险的地方，是不可逆的，覆盖工作目录
    
  * 说明
    reset 命令会以特定的顺序重写这三棵树,在你指定以下选项时停止:
    1. 移动 HEAD 分支的指向 (若指定了 --soft,则到此停止)
    2. 使索引看起来像 HEAD (若未指定 --hard,则到此停止)
    3. 使工作目录看起来像索引
    
  * git reset file.txt (这其实是 git reset --mixed HEAD file.txt 的简写形式)
  
    git跳过第一步，因为你无法让它同时指向两个提交中各自的一部分(找不到这样的提交节点去引用，相反索引和工作目录可以部分改变)。
    结果就是再一次把索引部分更改为其他引用(这里是HEAD)所指向的版本，例如:
    也可以指定具体的提交节点 git reset eb43bf file.txt 。
    
#### git checkout file.txt
    
    也不改变HEAD,但把索引内file.txt更改为其他提交节点的版本，也把工作目录file.txt也覆盖掉。这是危险的命令，不可逆

  * git commit --amend 
    
    等价于git reset --soft ~HEAD，效果就是放弃上一次提交。
    
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
