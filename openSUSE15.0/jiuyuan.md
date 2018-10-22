## [这是社区解决方法](https://zh.opensuse.org/%E4%BF%AE%E5%A4%8DGRUB2)
### 挂载原系统的各分区
首先查看各分区的情况，使用命令：
fdisk -l
列出分区情况。然后将各个分区挂载。例如，假设 sda6 为 /boot 分区，sda7 为 swap 分区，sda8 为 / 分区，sda9 为 /home 分区，我们就可以这样挂载：
```text
mount /dev/sda8 /mnt
mount /dev/sda6 /mnt/boot
mount /dev/sda9 /mnt/home
```
注意，swap 分区不需要挂载。要先挂在根分区，然后把其他的分区相应的挂在到根分区下。接着，
```text
mount -t proc proc /mnt/proc
mount --rbind /sys /mnt/sys
mount --rbind /dev /mnt/dev
```
这样，所需的挂载就结束了。 再然后
```text
chroot /mnt /bin/bash
```
chroot到需要修复的系统

### 重装 GRUB2
首先生成 /boot/grub2/grub.cfg：
```text
grub2-mkconfig -o /boot/grub2/grub.cfg
```
然后将 GRUB2 安装到 sda：
```text
grub2-install /dev/sda
```
## ubuntu18.10
```text
  我先安装openSUSE15.0，随后安装ubuntu。没想到把openSUSE引导覆盖了，在用上面的方法修复好openSUSE后，
  openSUSE会自动找到其他系统并添加到启动项。
  然而我重启后虽然找到ubuntu启动项，但是出现找不到内核文件情况。
  偶然情况，我从openSUSE系统中进入到ubuntu所在分区，发现其挂载路径多出一个'/@',并且后来发现这个/@是btrfs文件系统产生的，
  我的ubuntu根分区也是和 openSUSE一样 格式化为btrfs。随后又在ubuntu的grub下看到grub.cfg中的启动项是有/@的，
  于是我觉得应该在openSUSE中的grub.cnf中
  加入它，结果成功了。
  ```
