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
```
这是Collections中排序的静态方法参数T的类型

```java
public class Collections {

    @SuppressWarnings("unchecked")
        public static <T extends Comparable<? super T>> void sort(List<T> list) {
            list.sort(null);
        }
    }
```
带有通配符的下界泛型参数第一次看起来令人不知所措。
泛型被擦除导致一些问题，例如运行时类型丢失。
由于编译时泛型参数被擦除为`Object`，一个接口的不同泛型的声明形式事实上是两个相同的参数为Object类型的接口，任何编译器都不喜欢为调用者作出决策，
它拒绝对两个Object参数类型的接口的调用，因此任何类无法多次实现一个不同泛型参数的相同接口。
这是thinkinginjava中的一个例子:

```java
//: generics/HijackedInterface.java
// {CompileTimeError} (Won't compile)

//ComparablePet是一个Comparable<Pet>接口实现类

class Cat extends ComparablePet implements Comparable<Cat>{

    // Error: Comparable cannot be inherited with
    // different arguments: <Cat> and <Pet>
  
    public int compareTo(Cat arg) {
        return 0;
    }

} ///:~
``` 
乍看起来好像没有人会多次实现同一个接口两次，我们担心的问题好像有些多余，`Cat extends ComparablePet`就已经足够。虽然看起来不优雅，但在方法本身效果上，通过`方法重写`，
可以达到新的实现效果。就上个例子而言，我们看不出有什么理由非要重新实现`Comparable<Cat>`，问题在于，对于下面这种情况， `Cat类`既不是驴，也不是马了：

```java
class SpeakingPet{

    public static <T extends Comparable<T> > void sayhello(T anyone){
    //这个调用的确很奇葩，就当作有这么荒谬的做法
        anyone.compareTo(...);
        System.out,println("hello 我是"+anyone.getClass());
    }
}
```
假如我们的`Cat.class`已由其他人继承自`ComparablePet`，因而此时其无法作为参数`T`；并且如讨论的那样，`Cat`也无法重新实现`Comparable<Cat>`。我们仍旧想让其sayhello是不可能的，
为了让它说点什么，惟一的做法添加一个通配符，`<T extends Comparable<? super T> >`,这也是和Collections.sort()方法声明如出一辙。
