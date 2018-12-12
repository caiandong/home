## 常见spring springboot spring mvc注解

* @SpringBootApplication            等于下面三个注解

* @Configuration

* @EnableAutoConfiguration 

* @ComponentScan            默认扫描当前包

* @Autowired                自动注入
 ```text
 If a bean has one constructor, you can omit the @Autowired
 ```

* @Import           导入其他配置类

* @ImportResource 
```text
导入bean  xml配置文件
```
* @ConfigurationProperties 
```text
Add this to a class definition or a @Bean method in a @Configuration class 
```
