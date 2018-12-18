## 通配符与泛型 <? super T>
 
 这是java1.8中 新增的Iterable接口forEach参数类型
 
 ```java
 public interface Iterable<T> {
   
    Iterator<T> iterator();
    
    default void forEach(Consumer<? super T> action) {
        Objects.requireNonNull(action);
        for (T t : this) {
            action.accept(t);
        }
    }
    }

这是Collections中排序的静态方法参数T的类型

public class Collections {

    @SuppressWarnings("unchecked")
        public static <T extends Comparable<? super T>> void sort(List<T> list) {
            list.sort(null);
        }
    }
```

这是thinkinginjava中的一个例子：

```java
//: generics/HijackedInterface.java
// {CompileTimeError} (Won't compile)

class Cat extends ComparablePet implements Comparable<Cat>{

    // Error: Comparable cannot be inherited with
    // different arguments: <Cat> and <Pet>
  
    public int compareTo(Cat arg) {
        return 0;
    }

} ///:~
``` 
带有通配符的下界泛型参数第一次看起来令人不知所措。其中一个目的用来解决泛型被擦除类型的问题。
