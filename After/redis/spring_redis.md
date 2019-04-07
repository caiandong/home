 

- ##### 配置连接工厂。

  有两种，一种是LettuceConnectionFactory，另一种JedisConnectionFactory:

```java
            @Configuration
            class AppConfig {

              @Bean
              public LettuceConnectionFactory LettuceConnectionFactory() {

                return new LettuceConnectionFactory(new RedisStandaloneConfiguration("server", 6379));
              }

              @Bean
              public JedisConnectionFactory JedisConnectionFactory() {

                RedisStandaloneConfiguration config = new RedisStandaloneConfiguration("server", 6379);
                return new JedisConnectionFactory(config);

              }
            }
```

​		他们都需要RedisStandaloneConfiguration类来进行配置。

- ##### RedisTemplate是中心类。

  它提供了丰富的高级抽象以及低级方法调用，这些低级方法接受字节数组或者返回数组，并且它也负责序列化以及连接管理。因此，让用户从使用细节解脱出来。它也提供了各种不同数据类型操作视图，例如	

  HashOperations	Redis hash operations  mapp类型

  ListOperations   	Redis list operations	list类型

  SetOperations 	Redis set operations	集合类型

  等等。实际使用时不用显示调用opsFor[X]的方法，只需在容器中声明这些接口的依赖，容器利用转换器自动执行模板到相应视图额转换。

  注意，这里要用@Resource注解，不能用@Autowired与@Qualifier组合。

  ```java
  public class Example {
  
    // inject the actual template
    @Autowired
    private RedisTemplate<String, String> template;
  
    // inject the template as ListOperations
    @Resource(name="redisTemplate")
    private ListOperations<String, String> listOps;
  
    public void addLink(String userId, URL url) {
      listOps.leftPush(userId, url.toExternalForm());
    }
  }
  ```

- ##### 针对key value为字符串操作的方便的类。

  ```java
  public class Example {
  
    @Autowired
    private StringRedisTemplate redisTemplate;
  
    public void addLink(String userId, URL url) {
      redisTemplate.opsForList().leftPush(userId, url.toExternalForm());
    }
  }
  ```

  它同样可以调用对应redis数据类型的视图。RedisTemplate 和``StringRedisTemplate`可以让你通过RedisCallback直接与redis对话。

  注意，使用StringRedisTemplate，它收到的是StringRedisConnection，所以可以转型为它。

  ```java
  public void useCallback() {
  
    redisTemplate.execute(new RedisCallback<Object>() {
      public Object doInRedis(RedisConnection connection) throws DataAccessException {
        Long size = connection.dbSize();
        // Can cast to StringRedisConnection if using a StringRedisTemplate
        ((StringRedisConnection)connection).set("key", "value");
      }
     });
  }
  ```

- ##### 序列化器

  透过框架，实际上数据在redis中只是以字节存储，redis支持各种数据类型，这说的各种类型是它存储方式而不是它表示的方式，底层表示就是字节数组。这由用户决定是否信息被转成字符串或者其他对象。

  RedisSerializer和RedisElementReader RedisElementWriter主要的不同在于前者主要序列化成字节数组，后者使用的是ByteBuffer。

  - JdkSerializationRedisSerializer,默认被使用于 RedisCache and RedisTemplate.
  -  the StringRedisSerializer.
  - Jackson2JsonRedisSerializer or GenericJackson2JsonRedisSerializer  序列化json格式

  序列化器不局限于value ，包括key ，hashes，没有限制。

- ##### Hash mapping

  spring data redis 提供了三种从对象映射到hash:

  - Direct mapping, by using `HashOperations` and a `serializer`
  - Using Redis Repositories
  - `Using HashMapper` and `HashOperations`

  HashMapper是一个转换器，它把对象映射成Map<K, V>，或者从Map<K, V>映射回对象。

  | BeanUtilsHashMapper | HashMapper<T, String, String>      |
  | ------------------- | ---------------------------------- |
  | ObjectHashMapper    | HashMapper<Object, byte[], byte[]> |
  | Jackson2HashMapper  | HashMapper<Object, String, Object> |

  通常的步骤:

  ```java
  //获取HashOperations
  HashOperations<String, byte[], byte[]> hashOperations=.......;
  //把对象转为map
  Map<byte[], byte[]> mappedHash = mapper.toHash(someobject);
  //通过HashOperations把map写入redis
  hashOperations.putAll(key, mappedHash);
  
  //从redis获取键值为字节数组的map
  Map<byte[], byte[]> loadedHash = hashOperations.entries(key);
  //把map转为对象
  someobject=mapper.fromHash(loadedHash);
  ```

  注意的是，这整个map，也就是redis中的map，代表一个对象。并不是一个map中存多个对

- ##### Publishing (Sending Messages)

  ```java
  // send message through connection RedisConnection con = ...
  byte[] msg = ...
  byte[] channel = ...
  con.publish(msg, channel); // send message through RedisTemplate
  RedisTemplate template = ...
  template.convertAndSend("hello!", "world");
  ```

  RedisTemplate可以让任意对象作为消息发送

- ##### Subscribing (Receiving Messages)

  接收端通过直接的名字或者模式匹配，可以订阅多个频道。模式匹配很有用，他可以订阅尚不存在的频道。订阅者调用订阅后，它会等待消息，它所在的线程会阻塞，除非(在其他线程)在相同的RedisConnection调用unsubscribe` or `pUnsubscribe。

  在此RedisConnection上，调用非`subscribe`, `pSubscribe`, `unsubscribe`, or `pUnsubscribe` 方法将抛出异常。

  实现MessageListener接口，当一个消息到达后，onMessage方法会被回调。

- ##### Message Listener Containers

  为了缓和由低级订阅机制造成线程阻塞，管理线程。Spring Data提供了RedisMessageListenerContainer。它从redis频道接受消息后驱动MessageListener实例运行