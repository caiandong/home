## 一般配置与用法
  * @Mapper注解和@Mapperscan注解
    
    在spring中,他们是用来把接口mapper注入成spring内的dao对象的，
    而接口与xml文件绑定的前提是xml文件中namespace名要和mapper接口全类名相等，
    并且id值对应方法名相等。这样，mapper可以绑定成功。
    
    在springboot中会由自动配置下的类扫描，过滤规则是只扫描@Mapper注解;
    而@Mapperscan注解过滤规则是扫描配置的所有接口;

## 动态sql
  where         增加where  去除前面and 
  where 元素只会在至少有一个子元素的条件返回 SQL 子句的情况下才去插入“WHERE”子句。而且，若语句的开头为“AND”或“OR”，where 元素也会将它们去除。
  set           增加set  去除后面逗号
  这里，set 元素会动态前置 SET 关键字，同时也会删掉无关的逗号，因为用了条件语句之后很可能就会在生成的 SQL 语句的后面留下这些逗号。
