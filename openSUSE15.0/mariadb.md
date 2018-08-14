# 安装
    1安装完成后（opensuse自带，无需安装）执行mysql_secure_installation 进行安全设置
    2要想远程连接，需要进行防火墙设置
    firewall-cmd --parmanent --add-service=mysql
    firewall-cmd --reload （图形界面有个管理程序，第一次需要安装 在里面可以找到mysql设置）
    3到/etc/my.cnf文件注释band-address那行 解邦ip;
