
## 结构

* Servlet
```java
public interface Servlet {

    //在第一次请求servlet时调用
    public void init(ServletConfig config) throws ServletException;

    
    public ServletConfig getServletConfig();

   
    public void service(ServletRequest req, ServletResponse res)
            throws ServletException, IOException;

    
    public String getServletInfo();

    //servlet销毁方法
    public void destroy();
}

```
* GenericServlet类重载一个init（）方法，在重写的init方法中调用;
```java
public abstract class GenericServlet implements Servlet, ServletConfig,
        java.io.Serializable {


    @Override
    public void init(ServletConfig config) throws ServletException {
        this.config = config;
        this.init();
    }


    public void init() throws ServletException {
        // NOOP by default
    }
  
}

```
* HttpServlet重载service方法，并在重写的service方法中调用。重载的service方法作一个分发调用，根据请求方法
DELETE，HEAD，GET，OPTIONS，POST，PUT，TRACE。分别调用
```java

public abstract class HttpServlet extends GenericServlet {

    protected void service(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {

        String method = req.getMethod();

        if (method.equals(METHOD_GET)) {
            doGet();

        } else if (method.equals(METHOD_HEAD)) {
            long lastModified = getLastModified(req);
            maybeSetLastModified(resp, lastModified);
            doHead(req, resp);

        } else if (method.equals(METHOD_POST)) {
            doPost(req, resp);

        } else if (method.equals(METHOD_PUT)) {
            doPut(req, resp);

        } else if (method.equals(METHOD_DELETE)) {
            doDelete(req, resp);

        } else if (method.equals(METHOD_OPTIONS)) {
            doOptions(req,resp);

        } else if (method.equals(METHOD_TRACE)) {
            doTrace(req,resp);

        } else {
            //
        
        }
    }


    @Override
    public void service(ServletRequest req, ServletResponse res)
        throws ServletException, IOException {

        HttpServletRequest  request;
        HttpServletResponse response;

        try {
            request = (HttpServletRequest) req;
            response = (HttpServletResponse) res;
        } catch (ClassCastException e) {
            throw new ServletException("non-HTTP request or response");
        }
        service(request, response);
    }
}

}

```
* HttpServletBean开始作为springmvc包内的类。最终重写init（）方法，增加initServletBean（）和initBeanWrapper(BeanWrapper bw)
留给子类初始化实现。

```java

@SuppressWarnings("serial")
public abstract class HttpServletBean extends HttpServlet implements EnvironmentCapable, EnvironmentAware {
	
	@Override
	public void setEnvironment(Environment environment) {
	
	}

	@Override
	public ConfigurableEnvironment getEnvironment() {
		
	}

	@Override
	public final void init() throws ServletException {

		// Set bean properties from init parameters.
		.....

		// Let subclasses do whatever initialization they like.
		initServletBean();
	}

	
	protected void initBeanWrapper(BeanWrapper bw) throws BeansException {
	}

	
	protected void initServletBean() throws ServletException {
	}	

}

```
* FrameworkServlet重写initServletBean()来创建容器（如果不存在）并设置父容器（可为空）。新增onRefresh(ApplicationContext context)方法让子类从容器中
获取组件（各种解析器）。并且最终重写所有do get，post，put。。。所有基类（HttpServlet）方法。并且在processRequest内调用新增的方法doService由子类实现。

```java

@SuppressWarnings("serial")
public abstract class FrameworkServlet extends HttpServletBean implements ApplicationContextAware {

	
	public FrameworkServlet() {
	}

	
	public FrameworkServlet(WebApplicationContext webApplicationContext) {
		this.webApplicationContext = webApplicationContext;
	}


	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		
	}


	@Override
	protected final void initServletBean() throws ServletException {
		
			this.webApplicationContext = initWebApplicationContext();
			initFrameworkServlet();
		
		
	}


	protected WebApplicationContext initWebApplicationContext() {
		WebApplicationContext rootContext =
				WebApplicationContextUtils.getWebApplicationContext(getServletContext());
		WebApplicationContext wac = null;

		if (this.webApplicationContext != null) {
			// A context instance was injected at construction time -> use it
			wac = this.webApplicationContext;
			if (wac instanceof ConfigurableWebApplicationContext) {
				ConfigurableWebApplicationContext cwac = (ConfigurableWebApplicationContext) wac;
				if (!cwac.isActive()) {
					// The context has not yet been refreshed -> provide services such as
					// setting the parent context, setting the application context id, etc
					if (cwac.getParent() == null) {
						
						cwac.setParent(rootContext);
					}
					configureAndRefreshWebApplicationContext(cwac);
				}
			}
		}
		if (wac == null) {
			
			wac = findWebApplicationContext();
		}
		if (wac == null) {
			
			wac = createWebApplicationContext(rootContext);
		}

		if (!this.refreshEventReceived) {
			
			synchronized (this.onRefreshMonitor) {
				onRefresh(wac);
			}
		}

		if (this.publishContext) {
			// Publish the context as a servlet context attribute.
			String attrName = getServletContextAttributeName();
			getServletContext().setAttribute(attrName, wac);
		}

		return wac;
	}




	protected WebApplicationContext createWebApplicationContext(@Nullable ApplicationContext parent) {
		Class<?> contextClass = getContextClass();
        //创建context
		ConfigurableWebApplicationContext wac =
				(ConfigurableWebApplicationContext) BeanUtils.instantiateClass(contextClass);

		wac.setEnvironment(getEnvironment());
		//设置父容器
		wac.setParent(parent);
		
		configureAndRefreshWebApplicationContext(wac);

		return wac;
	}

	protected void configureAndRefreshWebApplicationContext(ConfigurableWebApplicationContext wac) {
	
		postProcessWebApplicationContext(wac);
		applyInitializers(wac);
		//刷新context
		wac.refresh();
	}
	
	protected void initFrameworkServlet() throws ServletException {
	}

	
	public void onApplicationEvent(ContextRefreshedEvent event) {
		this.refreshEventReceived = true;
		synchronized (this.onRefreshMonitor) {
			onRefresh(event.getApplicationContext());
		}
	}

	protected void onRefresh(ApplicationContext context) {
		// For subclasses: do nothing by default.
	}
    @Override
	protected void doOptions(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
    }
    @Override
	protected final void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		processRequest(request, response);
	}
	@Override
	protected final void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		processRequest(request, response);
	}
	//等等重写的基类方法
	/**
	 * Override the parent class implementation in order to intercept PATCH requests.
	 */
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpMethod httpMethod = HttpMethod.resolve(request.getMethod());
		if (httpMethod == HttpMethod.PATCH || httpMethod == null) {
			processRequest(request, response);
		}
		else {
			super.service(request, response);
		}
	}


	protected final void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			doService(request, response);
		}
	}

	protected abstract void doService(HttpServletRequest request, HttpServletResponse response)
			throws Exception;

}

```
* DispatcherServlet重写onRefresh方法来初始化解析器。在重写的doService方法中在request中放入解析器并调用自己新增的的doDispatch方法。
