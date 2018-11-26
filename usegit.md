## 远程仓库 
 * 重命名仓库
   
    git remote rename oldname newname
    
  * 删除远程仓库
  
    git remote rm remotename

## 查看分支
    git branch -vv
 
## 跟踪远程分支
  * git checkout -b [name] [remotename/remotebranch] 
  
    新建分支跟踪远程分支
  
  *  跟踪同名远程分支快捷方式
  
    git checkout --track [remotename/remotebranch]
    
    等价于
    
    git checkout -b [remotebranch] [remotename/remotebranch]
    
  * git branch -u [remotename/remotebranch] 
  
    更换当前分支跟踪远程分支或者添加跟踪远程分支
## 推送远程分支
  * git push [remotename] [localbranch] 
  
    推送本地分支作为远程分支
  
    如果不存在远程分支名为'localbranch'，那么会生成新的名为'localbranch'的远程分
    
    支（并不会自动跟踪，下同）
    
    如果存在，则更新同名远程分支
    
  * git push [remotename] [localbranch:remotebranch] 
    
    推送不同名分支
    不存在，则生成。存在则更新。
  * git push [remotename] --delete [remotebranch]
  
    此命令用来删除远程分支
