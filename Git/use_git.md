# [git官方中文教程](https://git-scm.com/book/zh/v2)
# [markdown语法说明](https://www.appinn.com/markdown)

## 用户配置
    $ git config --global user.name "John Doe"
    $ git config --global user.email johndoe@example.com

## 检查配置信息

    如果想要检查你的配置，可以使用 git config --list 命令来列出所有 Git 当时能找到的配置。
# 特别的,生成ssh密钥
$ ssh-keygen -t rsa -b 4096 -C "your_email@example.com"    
> 默认生成在~/.ssh/目录下,复制*key.pub*文件中的公钥内容内容到github指定处.

## 基本命令
* git init

    初始化一个仓库

* git clone [仓库名]  [仓库的本地名]

    如果本地名省略。默认为 origin

* git add [filename|.]

  跟踪新文件或暂存修改,注意`.`代表当前目录.

* git commit -m "your short message"

  提交当前暂存区.-m表示附加一个提交信息,否则会打开一个默认设置的文本编辑器来编辑提交信息

  -a选项用来提交所有已跟踪的文件,这就是说,之前已暂存的文件被修改了,不用再执行git add命令暂存修改,直接执行commit,省去一个git add命令.

* git status -s

  查看当前仓库的简短信息,输入如下所示

     M README
  MM Rakefile
  A 	 lib/git.rb
  M 	lib/simplegit.rb
  ?? 	LICENSE.txt

  新添加的未跟踪文件前面有 ?? 标记,新添加到暂存区中的文件前面有 A 标记,修改过的文件前面有 M 标记。 你
  可能注意到了 M 有两个可以出现的位置,出现在右边的 M 表示该文件被修改了但是还没放入暂存区,出现在靠左边的 M 表示该文件被修改了并放入了暂存区。

  一般可以直接不用-s选项.

* git rm PROJECTS.md

  从当前仓库删除PROJECTS.md.当前目录以及暂存区都被删除,下次提交时就会记录起来.

* git rm --cached README

  仅仅从暂存区删除README,当前目录还存在.之后提交会被记录

* git mv file_from file_to

  重命名或者移动文件

* git reset HEAD CONTRIBUTING.md

  取消暂存的文件,HEAD可以省略

* git checkout -- CONTRIBUTING.md

  丢弃对已跟踪但还未暂存的文件的修改

  > 你需要知道 git checkout -- [file] 是一个危险的命令,这很重要。 你对那个文
  > 件做的任何修改都会消失 - 你只是拷贝了另一个文件来覆盖它。 除非你确实清楚不想要
  > 那个文件了,否则不要使用这个命令。
  
* git commit --amend 

  等价于git reset --soft ~HEAD.类似于把上次提交放弃,然后加上当前提交内容,组成一个新提交.

## 日志

  * git log
  ```text
    -p
    --graph
    --pretty=oneline
  ```

 ## 远程仓库 

* git remote -v

  显示所有远程仓库简写与其对应的 URL

* git remote show origin

  显示某个远程仓库的详细信息
  
* git remote add <anyname> <url>
  添加远程仓库
  
* git remote rename oldname newname
  重命名仓库
  
* git remote rm remotename  
  删除远程仓库
  
* git fetch [remote-name]
  抓取某个远程仓库的数据到本地仓库

## 分支
  * git branch -vv
  
     详细查看分支
     
  * git branch --merged

     详细查看已合并分支;--no-merged是未合并分支
     
  * git branch [newbranch_name]
  
    新建分支.不带参数则显示所有分支
    
  * git checkout [branch_name]
  
    切换到分支
    
  * git branch -b [newbranch_name]
  
    新建分支并且切换到新分支
    
  * git branch -d [branch_name]
  
    删除分支 -D强制删除，即使没有合并

* git merge branch_name

  合并分支.branch_name既可以是本地分支也可以是远程分支指针


## 远程分支
  * git checkout -b [name]  [remotename/remotebranch] 
  
    检出到一个新分支,此新分支跟踪远程仓库分支

* git checkout --track [remotename/remotebranch]

  本地生成一个与远程分支同名的分支,并且它跟踪远程分支.等价于上条命令中[name]与[remotebranch]相等

  * git branch -u [remotename/remotebranch] 

    更换当前分支跟踪远程分支.如果当前分支没有远程分支,则添加跟踪远程分支


  * git push [remotename]  [localbranch] 
  
    推送本地分支作为远程分支
  
    如果不存在名为`localbranch`的远程分支，那么会生成新的名为`localbranch`的远程分支（并不会自动跟踪，下同）如果存在，则更新同名远程分支
    
  * git push [remotename]  [localbranch:remotebranch] 
    
    推送不同名分支
    不存在，则生成。存在则更新。
  * git push [remotename] --delete [remotebranch]
  
    此命令用来删除远程分支
    
## 标签    
   * git tag -a v1.4 -m 'my version 1.4'

	附注标签. 标签名:v1.4 信息:my version 1.4

   * git tag v1.5

	轻量标签.

> 注意: 附注标签是可以携带不止说明信息的其他东西,例如校验.是真正的标签.
        轻量标签仅仅是某个提交的引用

   * git show v1.4

	显示某个标签信息

   * git tag

	查看所有标签

   * git tag -a v1.2 9fceb02

	 对过去某个提交打上标签

   * git push origin v1.5   //把v1.5标签传到远程仓库
   
   * git push origin --tags  //把所有不在远程仓库的标签都上传

	标签共享到远程仓库中

   * git checkout -b version2 v2.0.0

	检出标签.检出v2.0.0标签到新分支version2

## 变基

变基操作是这样的:

> 它找到master分支和another分支的共同最近祖先提交,然后对比当前分支相对于该祖先的历次提交,提取相应的修改并存为临时文件,再移动another分支指向master分支,最后在当前指向的地方应用刚刚保存为临时文件的修改.

通常先先检出到需要变基的分支,假设是`another`

* git checkout another

随后把当前分支变基到某个分支,假如是master

* git rebase master

前两步操作可以合并成一个命令

* git rebase master another

还有另一种操作.假如master分支上创建新分支A,A分支上再创建分支B,A提交一些工作,B也提交一些工作.则可以越过A分支提交,把在B分支的提交应用到master分支上

* git rebase --onto master A B

## 提交前操作

### HEAD Index WorkingDirectory

  * git reset --soft ~HEAD 
    
    移动HEAD所指向的当前分支(比如master)到分支的父commit。
    
  * git reset --mixed ~HEAD 
    
    不仅把当前分支移动到父commit，也把index更新为父commit。这是git reset ~HEAD 的默认行为

####  git reset --mixed ~HEAD

    连同当前节点，索引，和工作目录都变为父节点。reset唯一的危险的地方，是不可逆的，覆盖工作目录

  * 说明
    reset 命令会以特定的顺序重写这三棵树,在你指定以下选项时停止:
    1. 移动 HEAD 分支的指向 (若指定了 --soft,则到此停止)
    2. 使index看起来像 HEAD (若未指定 --hard,则到此停止)
    3. 使工作目录看起来像index
    
  * git reset file.txt (这其实是 git reset --mixed HEAD file.txt 的简写形式)
  
    git跳过第一步，因为你无法让它同时指向两个提交中各自的一部分(找不到这样的提交节点去引用，相反index和工作目录可以部分改变)。
    结果就是再一次把index更改为其他引用(这里是HEAD)所指向的版本，例如:
    也可以指定具体的提交节点 git reset eb43bf file.txt 。
#### git checkout file.txt

    也不改变HEAD,但把index内file.txt更改为其他提交节点的版本，也把工作目录file.txt也覆盖掉。这是危险的命令，不可逆
