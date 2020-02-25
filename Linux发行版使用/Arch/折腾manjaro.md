

### 切换国内源(我选的中科大的)

- 选择国内镜像源

  - sudo pacman-mirrors -i -c China -m rank

- 选择源完成后刷新(-Syu 同步源列表并且更新已安装的)

  - sudo pacman -Syy  	//强制同步本地软件源列表

- 然后添加 ArchLinuxCN 的源，编辑`/etc/pacman.conf`，在文件末尾添加如下内容：

  - ```
    [archlinuxcn]
    SigLevel = Optional TrustedOnly
    Server = https://mirrors.ustc.edu.cn/archlinuxcn/$arch
    #如果中科大挂了，用清华的吧
    #https://mirrors.tuna.tsinghua.edu.cn/archlinuxcn/$arch
    ```

- 刷新,签名认证什么的

  - sudo pacman -S archlinuxcn-keyring && sudo pacman -Syｕ

- 安装bash命令补全

  - sudo pacman -S bash-completion

- 安装搜狗(fcitx-sogoupinyin)

  - sudo pacman -S fcitx-qt5 fcitx-sogoupinyin fcitx-configtool fcitx-lilydjwg-git

- 要设置开机启动，我们还需要在/etc/profile.d/目录中下加一个任意名以.sh结尾的文件，添加如下内容：

  - ```
    export GTK_IM_MODULE=fcitx
    export QT_IM_MODULE=fcitx
    export XMODIFIERS=@im=fcitx
    #这一个也有人写成这样:
    #export XMODIFIERS="@im=fcitx"
    #据说含义相同
    ```

- 安装mariadb(mariadb)，我瞎了，没看到安装输出信息，结果踩坑了

  - sudo pacman -S mariadb
  - sudo mysql_install_db --user=mysql --basedir=/usr --datadir=/var/lib/mysql   //初始化

  > 随后如果安装mysql workbench，要安装以下东西，不然无法连接

  - sudo pacman -S gnome-keyring

- 安装MKVToolNix修改默认视频字幕或者音轨一类的

- 安装sublime text

- 安装qq(deepin-wine-qq)，位于AUR [deepin-wine-qq](https://aur.archlinux.org/packages/deepin-wine-qq/)，可使用 `yay` 安装:

  - ```
    yay -S deepin-wine-qq
    ```

- 安装迅雷(deepin-wine-thunderspeed)，同上

  - ```
    yay -S deepin-wine-thunderspeed
    ```
    
- 安装百度云(baidunetdisk-bin)，这是官方rpm包重新打包

  - ```
    sudo pacman -S baidunetdisk-bin
    
- 安装百度云(deepin-wine-baidupan),可以不用这个了

  - ```
    yay -S deepin-wine-baidupan
    ```

- 安装markdown编辑器 Typora(typora)

- 安装百度网盘命令行`baidupcs-go-git`

- dde桌面环境退出命令(类似命令行注销): ` dde-*`找到合适的补全命令

- kde桌面环境退出(类似于注销),我不想说这个,不过已在各个环境下都可以使用一个命令:`loginctl terminate-user [username]`

- 红警 opneRa



[archlinux维基](https://wiki.archlinux.org)

[安装桌面环境](https://wiki.manjaro.org/index.php?title=Install_Desktop_Environments)

[manjaro维基](https://wiki.manjaro.org/)

[个人博客](https://www.lulinux.com/archives/1319)

### 一些问题

安装deepin-wine-baidupan后无法打开	[简书博客问题描述](https://www.jianshu.com/p/ccebb0d437bd)

解决方法是安装`gnome-settings-daemon`，直接pacman安装即可，然后运行`/usr/lib/gsd-xsettings`
​此时另起终端执行运行命令应该就可以成功运行

相关问题描述与讨论见：<https://aur.archlinux.org/packages/deepin.com.qq.office/> 下的评论，和<https://github.com/wszqkzqk/deepin-wine-ubuntu/issues/12>

以下是我按照说明解决方式

```bash
#安装gnome-settings-daemon后，进入到安装百度网盘软件的目录 /opt/deepinwine/apps/Deepin-BaiduNetDisk/
#在run.sh脚本里，前面添加下面脚本防止多次启动gsd-xsetting

    number=`ps -ef |grep gsd-xsettings* |grep -v grep |wc -l`

    if [ $number == 0  ];then
        echo "启动gsd-xsetting"
        /usr/lib/gsd-xsettings
    else 
        echo "fuck you"
    fi
 
```

- 为火狐浏览器添加翻译插件[点击此处](https://support.mozilla.org/zh-CN/kb/%E5%A6%82%E4%BD%95%E4%B8%BAFirefox%E6%B7%BB%E5%8A%A0%E7%BF%BB%E8%AF%91%E5%8A%9F%E8%83%BD)，也可以直接去**附加组件**搜索翻译插件.我找到*翻译侠*

- [美化参考](https://www.cnblogs.com/luoshuitianyi/p/10587788.html)  

  ```
  1.工作空间主题 -> Plasma主题 -> 获得新 Plasma 主题 ，搜索 MacBreeze Shadowless ，点击安装。
  2.再点击 图标 -> 获取新图标主题 ，搜索 Mojave CT icons 并安装。
  3.应用程序风格 -> 部件风格->部件样式后面的配置 -> 透明度。
  4.我们点击顶栏的设置，并点击 添加部件 ，我们向顶栏添加如下部件，直接用鼠标拖上去就行：应用程序启动器、锁定/注销、系统托盘、数字时钟，视个人情况添加.
  5.点击 配置 程序启动器 -> 很明显的启动器图标 -> 选择 -> 其他 -> 浏览 ，找到你保存的图标，然后点击 确定 -> 确定 即可。
  6.下载latte-dock(latte-dock)与文件管理器(nautilus)
  MacBreeze Shadowless
  ```

  [Arch Linux 安装手册/傻瓜书/教程/指南](https://www.jianshu.com/p/6fe59c24b3df)
