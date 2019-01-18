## 一般配置与用法
  * @Mapper注解和@Mapperscan注解
    
    在spring中,他们是用来把接口mapper注入成spring内的dao对象的，
    而接口与xml文件绑定的前提是xml文件中namespace名要和mapper接口全类名相等，
    并且id值对应方法名相等。这样，mapper可以绑定成功。
    
    在springboot中会由自动配置下的类扫描，过滤规则是只扫描@Mapper注解;
    而@Mapperscan注解过滤规则是扫描配置的所有接口;
