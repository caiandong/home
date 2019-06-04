

### 切换国内源(我选的中科大的)

- 查找国内源

  - sudo pacman-mirrors -i -c China -m rank

- 选择源完成后刷新

  - sudo pacman -Syy

- 安装bash命令补全

  - sudo pacman -Syy bash-completion

- 然后添加 ArchLinuxCN 的源，编辑`/etc/pacman.conf`，在文件末尾添加如下内容：

  - ```
    [archlinuxcn]
    SigLevel = Optional TrustedOnly
    Server = https://mirrors.ustc.edu.cn/archlinuxcn/$arch
    ```

- 刷新,签名认证什么的

  - sudo pacman -Syy && sudo pacman -S archlinuxcn-keyring && sudo pacman -Syy

- 安装搜狗,搜狗不兼容qt5,所以qt4要有,不能卸载.

  - sudo pacman -S fcitx-im
  - sudo pacman -S fcitx-configtool
  - sudo pacman -S fcitx-sogoupinyin  //有一步不能用root,忘记是哪一个了

- 要设置开机启动，我们还需要在/etc/profile中添加如下内容：

  - ```
    export GTK_IM_MODULE=fcitx
    export QT_IM_MODULE=fcitx
    export XMODIFIERS=@im=fcitx
    ```

- 安装mariadb(我瞎了，没看到安装输出信息，结果踩坑了)

  - sudo pacman -S mariadb
  - sudo mysql_install_db --user=mysql --basedir=/usr --datadir=/var/lib/mysql   //初始化

  > 随后安装mysql workbench，要安装以下东西，不然无法连接

  - sudo pacman -S gnome-keyring

- 安装MKVToolNix修改默认视频字幕或者音轨一类的

- 安装sublime text

- 安装迅雷(需要先安装yaourt)

  - yaourt -Sy deepin.com.thunderspeed

- 安装markdown编辑器 Typora

- 安装百度网盘命令行`baidupcs-go-git`

- dde桌面环境退出命令(类似命令行注销): ` dde-*`找到合适的补全命令

- kde桌面环境退出(类似于注销):`kdeinit5_shutdown`

- 红警 opneRa

## 官网

[archlinux维基](https://wiki.archlinux.org)

[安装桌面环境](https://wiki.manjaro.org/index.php?title=Install_Desktop_Environments)

[manjaro维基](https://wiki.manjaro.org/)




