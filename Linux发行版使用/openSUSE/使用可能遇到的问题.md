# 一些碰到的常用问题与解决
## 在安装任何新软件之前最好确保发行版是最新更新的，否则容易出问题
* 解压乱码 

  ```text
  解压某些下载下来的压缩文件，通常是zip rar的。用unzip出现乱码
  使用 unar解压可以。
  ```

* 切换jdk版本

  ```text
  由于java升级过快，并且还出现了jdk8下可以编译运行，到jdk11无法运行
  在多个版本之间选择一个使用alternatives命令。该命令要在root下使用。
  alternatives --config java 和
  alternatives --config javac 
  ```

* 修复挂载windows磁盘休眠，无法挂载或者只能以只读方式挂载

  ```text
  ntfsfix 设备名
  ```

* okular打开部分pdf乱码（说的就是tcp-ip详解卷一）

  ```text
  sudo zypper install poppler-data
  ```

* 刚装完系统后，使用git推送一直索要密码

  * 由于手贱，在初次使用git推送远程仓库，故意输入错误密码并且保存了，结果每次都是错误没有更正密码的机会。后来搜索并没有怎么解决，因为问题描述的不太好，但是搜索过程发现有一个对这个问题描述`ksshaskpass keeps asking for kwallet password`，见名知意，于是把它记录下来。
  * 我本来去找了家目录下的配置，希望删除关于kwallet的配置，没发现。后来才看见不但在`.config`中，`.local/share`也有。于是删除后，重新老老实实输入密码。。





