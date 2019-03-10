
@Configuration
//xml中：<aop:aspectj-autoproxy/>
@EnableAspectJAutoProxy
@Aspect
public class Myaspectj {
@Pointcut("execution(* com.example.aop..*.*(..))")
public void point_cut() {}
@Before("point_cut()&&target(bean)&&this(bean1)")
public void before_adv(Object bean,Object bean1) {
	System.out.println("before通知执行=======");
	System.out.println("这是"+bean);
	System.out.println("这是"+bean1);
}
@AfterReturning(value="point_cut()",returning="Return")
public void afterreturn_adv(Object Return) {
	System.out.println("返回通知执行=======");
	if(Return!=null)
		System.out.println("这是返回值"+Return);
	else
		System.out.println("返回void");
}
@AfterThrowing(pointcut="point_cut()",throwing="ex")
public void aftthrow_adv(Exception ex) {
	System.out.println("异常通知执行"+ex);
}
@After("point_cut()")
public void aft_adv() {
	System.out.println("after通知执行--------");
}
@Around("point_cut()")
public Object around_adv(ProceedingJoinPoint jp) throws Throwable {
	System.out.println("环绕通知开始>>>>>>>>>");
	Object obj=jp.proceed();
	System.out.println("环绕通知结束<<<<<<<<<");
	return obj;
}
}
