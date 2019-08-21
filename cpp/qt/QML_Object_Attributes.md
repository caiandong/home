每个QML对象类型都一组已定义的属性(attribute)集合。一个对象类型的每一个实例被创建时都有一组已被定义为这种类型的属性的集合 。有几种不同类型的属性在以下被指定与描述。

## 对象声明中的属性

在QML文档中的一个对象声明定义 了一个新类型。它也声明了一个将被实例化的对象层次结构，should an instance of that newly defined type be created.

这组QML对象类型(object-type)属性类型如下:

- the *id* attribute
- property attributes
- signal attributes
- signal handler attributes
- method attributes
- attached properties and attached signal handler attributes

以下讨论了这些属性的细节。

### *id* 属性

每个QML对象类型都有一个确定的id属性.此属性由语言自身提供，而且也不能被任何QML对象类型重新定义或者覆盖。

一个值可能被赋予一个对象类型的id属性，***来让那个对象是唯一标识以及被其他对象指向引用***。id必须以小写字母或下划线开头，不能包含非字母，数字和下划线。

下面是一个TextInput对象和一个Text对象,TextInput对象的id值被设置成"myTextInput"。Text对象设置它的text属性的值和TextInput的Text属性值相同，这是通过指向myTextInput.text。现在，两个条目将显示相同的文本:

```qml
  import QtQuick 2.0

  Column {
      width: 200; height: 200

      TextInput { id: myTextInput; text: "Hello World" }

      Text { text: myTextInput.text }
  }
```

一个对象可以在它被声明的 *组件作用域*( *component scope*)任何地方通过它自己的id属性被指向引用。因此，id属性必须总是在它的组件作用域唯一。参见[Scope and Naming Resolution](qthelp://org.qt-project.qtdoc.597/qtqml/qtqml-documents-scope.html) 获得更多信息。

一旦一个对象实例被创建，它的id属性不能被改变。尽管它可能看起来像一个很平常的属性，并不是这样，而且还有特别的语义适用于它;例如，以上例子中是不可能访问 `myTextInput.id`的。

### Property 属性

一个property是可以被赋予静态值或者被绑定到动态表达式的一个对象的一个属性。一个property的值可以被其他对象读取。一般来说，它可以被其他对象改变，除非一个特别的QML类型已经被显示禁止对其某个指定property这样做。

#### 定义 Property 属性

一个property也许被定义成c++中的类型，通过注册一个类的[Q_PROPERTY](qthelp://org.qt-project.qtdoc.597/qtcore/qobject.html#Q_PROPERTY) ，然后此类被QML类型系统注册。另外，一个对象类型的自定义property也许会被定义到QML文档中的对象声明，通过下面的语法:

```qml
  [default] property <propertyType> <propertyName>
```

在这种方法中，一个对象类型可能暴露一个特别的值给外部对象或者更容易地维护一些内部状态。

Property名必须以小写字母开始并且仅能包含祖母，数字，下划线。JavaScript保留关键字也不能是一个合法的属性名。default关键字是可选的，它修改被声明的属性语义。参见后面有关default properties部分获得更多关于default property modifier。



声明一个自定义属性隐式创建了一个那个property的value-change信号，以及一个相关联的信号处理器([signal handler](qthelp://org.qt-project.qtdoc.597/qtqml/qtqml-syntax-objectattributes.html#signal-handler-attributes))，叫做:*on<PropertyName>Changed*,*<PropertyName>* 是属性名，首字母大写。

例如，下面的对象声明定义了一个源自Rectangle基本类型的新类型。它有两个新property，以及新属性中的其中某个属性的一个信号处理器实现。

```qml
Rectangle {
      property color previousColor
      property color nextColor
      onNextColorChanged: console.log("The next color will be: " + nextColor.toString())
  }
```

##### 自定义property定义中的合法类型

任何[QML Basic Types](qthelp://org.qt-project.qtdoc.597/qtqml/qtqml-typesystem-basictypes.html)类型除了[enumeration](qthelp://org.qt-project.qtdoc.597/qtqml/qml-enumeration.html)类型都可以被用作自定义property类型。例如，有一个都是合法的property声明。

```qml
  Item {
      property int someNumber
      property string someString
      property url someUrl
  }
```

(Enumeration 值是简单的整个都是数字值并且可以被int类型指向引用)

一些QtQuick模块提供的基本类型还不能被用作property类型除非那个模块已被导入。

注意var基本类型是一个泛型(generic)占位符类型,可以持有任何类型的值，包括lists和objects；

```qml
 property var someNumber: 1.5
  property var someString: "abc"
  property var someBool: true
  property var someList: [1, 2, "three", "four"]
  property var someObject: Rectangle { width: 100; height: 100; color: "red" }
```

另外，任何QML对象类型都可以被用作property类型。例如:

```qml
  property Item someItem
  property Rectangle someRectangle
```

这也适用于**自定义QML类型**。如果一个QML类型被定义在一个名为ColorfulButton.qml的文件(此文件放在一个目录且目录已被客户端imported)，那么ColorfulButton类型的property也是合法的。

#### 赋值给Property属性

一个对象实例的property值可以被用两种分隔的方式指定:

- 在初始化时赋值
- 在必要时赋值(an imperative value assignment)

两种情况下，值要么是一个静态(*static*)值，要么是一个绑定表达式(*binding expression*)值.

##### 初始化时赋值

语法是:

```qml
  <propertyName> : <value>
```

初始化赋值可以与在对象声明中的属性定义结合起来，如果想要这样做的话。在那种情况下，属性定义的语法变成:

```qml
  [default] property <propertyType> <propertyName> : <value>
```

一个属性值初始化的例子如下:

```qml
  import QtQuick 2.0

  Rectangle {
      color: "red"
      property color nextColor: "blue" // combined property declaration and initialization
  }
```

##### 必要时赋值

必要时赋值是一个property值被用来赋值给另一个property，使用命令的JavaScript代码。这种语法就是JavaScript赋值操作符，如下所示:

```qml
 [<objectId>.]<propertyName> = value
```

一个例子如下:

```qml
 import QtQuick 2.0

  Rectangle {
      id: rect
      Component.onCompleted: {
          rect.color = "red"
      }
  }
```

#### 静态值与绑定表达式值

正如前面提到的，有两种值可以被赋值给一个property:静态值和绑定表达式值。后一种也是熟知的属性绑定(property bindings)。

| Kind               | Semantics                                                    |
| :----------------- | :----------------------------------------------------------- |
| Static Value       | 不依赖其他property的常量值.                                  |
| Binding Expression | 描述了一个property与其他properties的关系的JavaScript表达式。表达式中的变量被称作property的依赖。QML引擎强迫property与它的依赖之间的关系。当任何依赖值改变时，QML引擎自动重新计算绑定表达式的值并且赋值新的结果给这个property。 |

这里有个例子展示了两种值被赋值给properties情况:

```qml
  import QtQuick 2.0

  Rectangle {
      // both of these are static value assignments on initialization
      width: 400
      height: 200

      Rectangle {
          // both of these are binding expression value assignments on initialization
          width: parent.width / 2
          height: parent.height
      }
  }
```

**注意**:对于命令式赋值绑定表达式，绑定表达式必须被包含在一个函数中，此函数被传进Qt.bingding()里，然后从Qt.bingding()中返回的值再被赋值给property。作为对比，当在初始化时赋值绑定表达式时，Qt.bingding()禁止再被使用。参见Property Binding获取更多信息。

#### 类型安全

properties是类型安全的。property只能被赋予匹配此property类型的值。

例如，如果一个property是实数(real),且如果你尝试赋值字符串给它，你会得到一个错误:

```qml
 property int volume: "four"  // generates an error; the property's object will not be loaded
```

同样的如果在运行期间property被赋予错误类型的值，新值不会被赋予，也会生成错误。

一些property类型没有初始值表示，对于这些property类型QML引擎自动执行字符串到目标类型值(string-to-typed-value)转换.所以，例如，即使color类型的properties存储colors而非string，你还是能够赋值字符串"red"给color属性，而没有错误报告出来。

参见 [QML Basic Types](qthelp://org.qt-project.qtdoc.597/qtqml/qtqml-typesystem-basictypes.html)获得默认支持的属性类型的列表。另外，任何可用的 [QML Basic Types](qthelp://org.qt-project.qtdoc.597/qtqml/qtqml-typesystem-basictypes.html)也可以被用作property类型。

#### 特定属性类型

##### Object List Property属性