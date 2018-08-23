# 1.2.11. Multipart resolver

MultipartResolver from the org.springframework.web.multipart package is a strategy for parsing multipart requests including 
file uploads. There is one implementation based on Commons FileUpload and another based on Servlet 3.0 multipart request parsing.

To enable multipart handling, you need declare a MultipartResolver bean in your DispatcherServlet Spring configuration 
with the name `"multipartResolver"`. 
The DispatcherServlet detects it and applies it to incoming request. When a POST with content-type of "multipart/form-data" 
is received, the resolver parses the content 
and wraps the current HttpServletRequest as MultipartHttpServletRequest in order to provide access to resolved parts 
in addition to exposing them as request parameters.
```java
servlet3.0以上
Servlet 3.0 multipart parsing needs to be enabled through Servlet container configuration:

    in Java, set a MultipartConfigElement on the Servlet registration.

    in web.xml, add a "<multipart-config>" section to the servlet declaration.

    public class AppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

        // ...

        @Override
        protected void customizeRegistration(ServletRegistration.Dynamic registration) {

            // Optionally also set maxFileSize, maxRequestSize, fileSizeThreshold
            registration.setMultipartConfig(new MultipartConfigElement("/tmp"));
        }

    }
Once the Servlet 3.0 configuration is in place, simply add a bean of type StandardServletMultipartResolver 
with the name multipartResolver.

这是springboot2.04里的自动配置，由于是内嵌服务器，有些不同

    @Configuration
    @ConditionalOnClass({ Servlet.class, StandardServletMultipartResolver.class,
        MultipartConfigElement.class })
    @ConditionalOnProperty(prefix = "spring.servlet.multipart", name = "enabled", matchIfMissing = true)
    @ConditionalOnWebApplication(type = Type.SERVLET)
    @EnableConfigurationProperties(MultipartProperties.class)
    public class MultipartAutoConfiguration {

      private final MultipartProperties multipartProperties;

      public MultipartAutoConfiguration(MultipartProperties multipartProperties) {
        this.multipartProperties = multipartProperties;
      }

      @Bean
      @ConditionalOnMissingBean({ MultipartConfigElement.class,
          CommonsMultipartResolver.class })
      public MultipartConfigElement multipartConfigElement() {
        return this.multipartProperties.createMultipartConfig();
      }

      @Bean(name = DispatcherServlet.MULTIPART_RESOLVER_BEAN_NAME)
      @ConditionalOnMissingBean(MultipartResolver.class)
      public StandardServletMultipartResolver multipartResolver() {
        StandardServletMultipartResolver multipartResolver = new StandardServletMultipartResolver();
        multipartResolver.setResolveLazily(this.multipartProperties.isResolveLazily());
        return multipartResolver;
      }

    }
```
