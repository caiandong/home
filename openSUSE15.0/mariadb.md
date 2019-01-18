# [这个好像是官网](https://mariadb.com/)

# 安装
   1安装完成后（opensuse自带，无需安装）执行mysql_secure_installation 进行安全设置
   
   2要想远程连接，需要进行防火墙设置
   
   firewall-cmd --parmanent --add-service=mysql
   firewall-cmd --reload （图形界面有个管理程序，第一次需要安装 在里面可以找到mysql设置）
   
   3到/etc/my.cnf文件注释band-address那行 解邦ip;
# 账户设置
  * 创建账户：create user 用户名@主机名（主机名也可以是 '%' 表示任意主机） identified by '密码';
  * 删除账户：DROP USER 用户名@主机名;
  * 修改密码：SET PASSWORD FOR 用户名@主机名 = PASSWORD('新密码');（修改当前密码：SET PASSWORD = PASSWORD('新密码');)
# 赋予账户权限
  * GRANT 权限ON 数据库.表单名称TO 账户名@主机名
    * 对某个特定数据库中的特定表单给予授权
  * GRANT 权限 ON 数据库.*TO 账户名@主机名
    * 对某个特定数据库中的所有表单给予授权
  * GRANT 权限 ON*.*TO 账户名@主机名
    * 对所有数据库及所有表单给予授权
  * GRANT 权限1,权限2 ON 数据库.*TO 账户名@主机名  
    * 对某个数据库中的所有表单给予多个授权
  * GRANT ALL PRIVILEGES ON *.*TO 账户名@主机
    * 对所有数据库及所有表单给予全部授权
# 查看权限
   * show grants for 用户名@'主机名' 
# 移除权限
  * REVOKE 权限ON 数据库.表单名称FROM 账户名@主机名
# 数据库备份与恢复
  * 备份 mysqldump -u 用户 -p 数据库名 [tab1 tab2 ...] > 路径+名字+.dump后缀
  * 恢复 mysql -u 用户 -p 数据库名 [tab1 tab2 ...] < 备份文件名
# 约束添加

```text
    create table tablename(
    id int primary key,
    name varchar(10) not null,
    seat int unique,
    age int default 18,
    maiorId int #references major(id)
    #mysql中列级外键约束无效
    );

```
  * 建表时直接在列后追加约束类型。
  
```text
    create table tablename(
    id int ，
    name varchar(10) ，
    seat int ，
    age int ，
    maiorId int,
    constraint fk_xxx foreing key (majorId) references
    major(id) #外键约束
    );
```
  * 这种不支持not null和default
