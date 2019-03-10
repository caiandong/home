
//xml中：添加<tx:annotation-driven/>
//java中：代码开启事物
//这两种方式是用来替代在xml中利用aop的方式来切入事物

@EnableTransactionManagement    //PlatformTransactionManager名字不需要是transactionManager

@Configuration //配置类
public class Appcnf{
@Bean
public PlatformTransactionManager transactionManager(){
     return  new DataSourceTransactionManager();
    }
}
//只有一个PlatformTransactionManager名字不需要是transactionManager,
//多个需要指定
@Transactional      //不用再配置tx:advice aop:advisor等等
public class DefaultFooService implements FooService {

    Foo getFoo(String fooName);

    Foo getFoo(String fooName, String barName);

    void insertFoo(Foo foo);

    void updateFoo(Foo foo);
}
