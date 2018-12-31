# 一些碰到的常用问题与解决
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
