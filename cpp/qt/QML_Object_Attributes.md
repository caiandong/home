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

一个值可能被赋予一个对象类型的id属性，***来让那个对象是唯一标识然后被其他对象指向引用***。id必须以小写字母或下划线开头，不能包含非字母，数字和下划线。

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

一个property也许被定义成c++中的类型，通过注册一个类的[Q_PROPERTY](qthelp://org.qt-project.qtdoc.597/qtcore/qobject.html#Q_PROPERTY) ，然后此类通过QML类型系统被注册。另外，一个对象类型的自定义property也许会被定义到QML文档中的对象声明，通过下面的语法:

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
- 在命令式赋值(an imperative value assignment)

两种情况下，值要么是一个静态(*static*)值，要么是一个绑定表达式(*binding expression*)值.

##### 初始化时赋值

语法是:

```qml
  <propertyName> : <value>
```

如果你想的话,初始化赋值可以与在对象声明中的属性定义结合起来。在那种情况下，属性定义的语法变成:

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

##### 命令式赋值

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

一些property类型没有本身的值来表示，对于这些property类型,QML引擎自动执行字符串到目标类型值(string-to-typed-value)转换.所以，例如，即使color类型的properties存储colors而非string，你还是能够赋值字符串"red"给color属性，而没有错误报告出来。

参见 [QML Basic Types](qthelp://org.qt-project.qtdoc.597/qtqml/qtqml-typesystem-basictypes.html)获得默认支持的属性类型的列表。另外，任何可用的 [QML Basic Types](qthelp://org.qt-project.qtdoc.597/qtqml/qtqml-typesystem-basictypes.html)也可以被用作property类型。

#### 特定属性类型

##### 对象列表属性(Object List Property)

QML对象类型(object-type)列表可以赋值给`list`类型.定义对象列表值的语法是方括号包围一个由逗号分隔的列表:

```
  [ <item 1>, <item 2>, ... ]
```

例如,`Item`类型有一个`states`属性用来持有`State`类型对象的列表.下面的代码初始化此属性值为三个`State`对象的列表.

```
  import QtQuick 2.0

  Item {
      states: [
          State { name: "loading" },
          State { name: "running" },
          State { name: "stopped" }
      ]
  }
```

如果列表包含单个条目,方括号可以省略:

```QML
  import QtQuick 2.0

  Item {
      states: State { name: "running" }
  }
```

`list`类型属性可以在对象声明内指定,使用下面的语法:

```QML
  [default] property list<<objectType>> propertyName	
```

像其他属性声明一样,属性初始化可以与属性定义结合,使用下面的语法:

```QML
  [default] property list<<objectType>> propertyName: <value>
```

下面是一个列表属性声明的例子:

```QML
  import QtQuick 2.0

  Rectangle {
      // declaration without initialization
      property list<Rectangle> siblingRects

      // declaration with initialization
      property list<Rectangle> childRects: [
          Rectangle { color: "red" },
          Rectangle { color: "blue"}
      ]
  }
```

如果你希望声明一个属性来存储一个不需要是QML对象类型(object-type)值的列表时,你应该声明`var`属性替代.

##### 分组属性(Grouped Properties)

在一些情况下,属性包含了一个逻辑组`子属性`(sub-property)属性.这些`子属性`属性可以使用点导航或组导航方式来赋值.

例如,`Text`类型有一个`font`组属性.如下面所示,第一个`Text`对象使用点导航方式初始化它的*font*值,然而,第二个使用组导航方式:

```QML
  Text {
      //dot notation
      font.pixelSize: 12
      font.b: true
  }

  Text {
      //group notation
      font { pixelSize: 12; b: true }
  }
```

分组属性类型是有子属性的基本类型.其中有些这样的基本类型被QML语言所提供,然而其他的一些类型仅当Qt Quick模块被导入才可以使用.参见关于[QML Basic Types](qtqml-typesystem-basictypes.html#)文档获得更多信息.

##### 属性别名(Property Aliases)

`属性别名`是持有指向另一个属性引用的属性.不像普通的属性定义,即分配给属性自己新的,唯一的存储空间,一个属性别名连接最新声明的属性(可以被称作别名属性)作为到一个已存在属性(被别名属性)的直接引用.

属性别名声明看起来像一个普通属性定义,除了它需要`alias`关键字而不是属性类型,并且它的属性声明的右边必须是合法的别名引用(alias reference):

```QML
  [default] property alias <name>: <alias reference>
```

不像普通属性,一个别名有以下限制:

- 它只能指向一个对象,或者对象的property,此property要在别名被声明的类型(type)作用域之内.
- 它不能包含任意JavaScript表达式.
- 它不能指向它自己的类型作用域以外声明的对象.
- *alias reference*是必须的,不像默认值是可选的一般property;别名引用必须在第一次别名被声明时提供
- 它不能指向`attached properties`
- 它不能指向分组属性(grouped properties);下面的代码不会奏效:

```QML
  property alias color: rectangle.border.color

  Rectangle {
      id: rectangle
  }
```

然而,到一个`value type`的properties是可以的:

```QML
  property alias rectX: object.rectProperty.x

  Item {
      id: object
      property rect rectProperty
  }
```

例如,下面是一个`Button`类型,它有一个已别名property `buttonText`被连接到`Text`孩子的`text`对象:

```QML
  // Button.qml
  import QtQuick 2.0

  Rectangle {
      property alias buttonText: textItem.text

      width: 100; height: 30; color: "yellow"

      Text { id: textItem }
  }
```

下面的代码会创建以上声明类型的一个`Button对象`,以及一个定义好的文本字符串给孩子`Text`对象:

```QML
  Button { buttonText: "Click Me" }
```

这里,改变`buttonText`直接修改了`textItem.text`的值;它不会改变后来会更新`textItem.text`的其他值(ps:这里可能指的是property binding,下面详谈).但如果`buttonText`不是一个别名,改变它的值实际上根本不改变显示的文本,因为属性绑定(property bindings)不是双向的:如果`textItem.text`被改变时`buttonText`就会改变,但反过来不行.

> 注意,这里有些莫名其妙,可能想说明的是:要区分别名声明和一般的属性声明.
>
> 假如把alias关键字改为其它类型声明例如string,它就会成为一个属性绑定:`buttonText`绑定到`textItem.text`.改变`textItem.text`将会自动更新`buttonText`,但反过来不行.这就是最后一句的意思:属性绑定是单向的.
>
> ~~然而,属性别名并不是这样.当改变~~

##### 属性别名的考虑(Considerations for Property Aliases)

别名只有在一个组件已被完全初始化后才被激活.当一个未初始化的别名被引用到时会生成错误.类似的,取别名一个`别名属性`将也会导致错误.

```QML
  property alias widgetLabel: label

  //will generate an error
  //widgetLabel.text: "Initial text"

  //will generate an error
  //property alias widgetLabelText: widgetLabel.text

  Component.onCompleted: widgetLabel.text = "Alias completed Initialization"
```

但是,当在根对象中使用属性别名导入一个[QML object type](qtqml-typesystem-objecttypes.html#)时,这样的property可以和正常的Qt属性一样出现,并且随后,可以被使用在别名引用上.

有可能别名属性的名字和已存在的属性名相同,实际上会覆盖已存在的属性.例如,下面的QML类型有一个`color`别名属性,被取成和内建`Rectangle::color`属性名一样:

```QML
  Rectangle {
      id: coloredrectangle
      property alias color: bluerectangle.color
      color: "red"

      Rectangle {
          id: bluerectangle
          color: "#1234ff"
      }

      Component.onCompleted: {
          console.log (coloredrectangle.color)    //prints "#1234ff"
          setInternalColor()
          console.log (coloredrectangle.color)    //prints "#111111"
          coloredrectangle.color = "#884646"
          console.log (coloredrectangle.color)    //prints #884646
      }

      //internal function that has access to internal properties
      function setInternalColor() {
          color = "#111111"
      }
  }
```

任何使用这个类型的对象,指向`color`属性的引用都会指向别名而不是普通的`Rectangle::color`属性.然而,在内部,rectangle可以正确设置它的`color`属性,并且指向实际定义的属性而非别名.

> ps 这段不明白啊....

##### 属性别名和类型(Property Aliases and Types)

属性别名没有显式类型规范.属性别名的类型是它指向的属性或对象的被声明类型.因此,如果你创建一个到通过id引用的对象的别名,此对象内联声明了额外属性,额外属性不会通过别名访问到:

// MyItem.qml

```QML
  Item {
      property alias inner: innerItem

      Item {
          id: innerItem
          property int extraProperty
      }
  }
```

你不能从组件外部初始化*inner.extraProperty*,因为inner只是一个*Item*:

 // main.qml

```QML
  MyItem {
      inner.extraProperty: 5 // fails
  }
```

然而,如果你用一个专门的.qml文件提取内部对象到一个分隔的组件中,相反你可以实例化那个组件,然后通过别名能够访问它的所以属性:

```QML
  // MainItem.qml
  Item {
      // Now you can access inner.extraProperty, as inner is now an ExtraItem
      property alias inner: innerItem

      ExtraItem {
          id: innerItem
      }
  }

  // ExtraItem.qml
  Item {
      property int extraProperty
  }
```

> ps 个人理解是,组件内声明的子对象并没有被导出为类型,所以,只有那些被导出为类型的组件才可以访问其属性

##### 默认属性(Default Properties)

一个对象定义能够有单个的一个默认属性(*default* property).默认属性是这样一个属性:如果一个对象被声明在另一个对象定义中,并且没有把它声明作为外部对象某个特别的属性值时,一个值会被赋予这个外部对象的这样的属性.

使用可选的`default`关键字声明一个属性可以把它标记成默认属性.例如,假如有一个MyLabel.qml文件有默认属性`someText`:

```QML
  // MyLabel.qml
  import QtQuick 2.0

  Text {
      default property var someText

      text: "Hello, " + someText.text
  }
```

`someText`值可以在一个`MyLabel`对象定义中赋值,像这样:

```QML
  MyLabel {
      Text { text: "world!" }
  }
```

这和下面的效果实际上一样的:

```QML
  MyLabel {
      someText: Text { text: "world!" }
  }
```

然而,因为`someText`属性已被标记成默认属性,没有必要再显式赋值`Text`对象到这个属性了.

你会注意到孩子对象(child objects)可以被添加到任何基于`Item`的类型而不需要显式添加他们到`children`属性.这是因为`Item`默认属性是它的`data`属性(是一个list),并且任何被添加到此`Item`列表的条目是自动添加到它的`children`列表.

默认属性对于重新赋值一个条目的孩子很有用.参见[TabWidget Example](../qtquick/qtquick-customitems-tabwidget-example.html),它使用一个默认属性自动重新赋值TabWidget的孩子作为一个内部`ListView`.参见[Extending QML](../qtquick/qtquick-codesamples.html#extending-qml).

##### 只读属性(Read-Only Properties)

一个对象声明可以使用`readonly`关键字定义只读属性,使用如下语法:

```QML
  readonly property <propertyType> <propertyName> : <initialValue>
```

只读属性必须在初始化赋予一个值.在被初始化后,就不可能再给它一个值,无论是否从命令式代码或者其他方式.

例如,下面在`Component.onComplete`代码块内是不合法的:

```QML
  Item {
      readonly property int someNumber: 10

      Component.onCompleted: someNumber = 20  // doesn't work, causes an error
  }
```

注意:只读属性也不能是默认属性.

##### 属性修改器对象(Property Modifier Objects)

属性可以有一个关联到它们的[property value modifier objects](qtqml-cppintegration-definetypes.html#property-modifier-types) .声明一个关联某个特定属性的属性修改器类型的实例的语法如下:

```QML
  <PropertyModifierTypeName> on <propertyName> {
      // attributes of the object instance
  }
```

要知道以上语法实际上是一个对象声明(object declaration),它将会实例化一个对象,这个对象的行为都是在那个已存在属性上的.

某些属性修改器类型只能被用在特定属性类型上,然而这不是语言所强迫的.例如,由`QtQuick`提供的`NumberAnimation`类型将只能以数字类型(numeric-type)属性(例如int或real)生成动画.尝试使用一个非数字属性的`NumberAnimation`将导致错误,而且,非数字属性也不会产生动画效果.当与一个特别的属性类型相关联时,属性修改器类型的行为被它的实现所定义.

### 信号属性(Signal Attributes)

当一些事件发生时一个对象会发出通知,这个通知就是信号:例如,一个属性已发生过变化,一个动画已开始或停止,或者当一个图片已经被下载了.例如当用户在鼠标区域点击时,`MouseArea`类型会有一个`clicked`信号被发射.

无论何时一个特殊信号被发射,一个对象都可以通过它的信号处理器(`signal handler`)被通知到.信号处理器使用  *on\<Signal>* 语法声明 *\<Signal>* 是信号名的首字母大写.信号处理器必须被声明在发射此信号的对象定义内,并且处理器应该包含JavaScript代码块,它将在信号处理器被调用时执行.

例如,下面被声明在`MouseArea`对象定义之内的*onClicked*信号处理器,当`MouseArea`被点击时它被调用,引起控制带消息被打印:

```QML
  import QtQuick 2.0

  Item {
      width: 100; height: 100

      MouseArea {
          anchors.fill: parent
          onClicked: {
              console.log("Click!")
          }
      }
  }
```

##### 定义信号属性(Defining Signal Attributes)

信号可以被定义成c++中的类型,这是通过注册一个类的`Q_SIGNAL`,然后通过QML类型系统被注册.作为替代方式,一个对象类型的自定义信号也可以被定义在QML文档中的对象声明中,使用如下语法:

```QML
  signal <signalName>[([<type> <parameter name>[, ...]])]
```

在同一个类型块声明中,尝试声明两个相同名称的信号或方法是一个错误.但新信号可以复用类型中已存在的信号名.(这应该小心行事,因为已存在的信号会被隐藏且不可访问)

这里是三个信号声明的例子:

```QML
  import QtQuick 2.0

  Item {
      signal clicked
      signal hovered()
      signal actionPerformed(string action, var actionResult)
  }
```

如果信号没有参数,括号可以省略.如果参数被用到,参数类型必须声明,正如以上`actionPerformed`信号的`string`和`var`参数.可用的参数类型与当前页面中位于 [Defining Property Attributes](#defining-property-attributes)之下所列出的类型相同.

要发射信号,就把它作为一个方法调用.任何相关的信号处理器都会在信号被发射时调用,并且处理器可用使用已定义的信号参数来访问相应的参数.

##### 属性改变信号(Property Change Signals)

QML类型也提供了内建的**属性改变信号**(*property change signals*),无论何时属性改变,它们都会被发射,这些属性正如之前在`property attributes`部分描述的.

### 信号处理器属性(Signal Handler Attributes)

信号处理器是有点像一个特别的**方法属性**(*method attribute*),无论什么时候关联的信号被发射,这些方法实现会都被QML引擎调用.添加一个信号到QML的对象定义中将会自动添加关联的信号处理器到那个对象,默认情况下,这是一个空的实现.客户可以提供实现来实现编程逻辑.

考虑下面`SquareButton`类型,这些定义被提供在`SquareButton.qml`文件中,如下所示,还有`activated`和`deactivated`信号:

```QML
  // SquareButton.qml
  Rectangle {
      id: root

      signal activated(real xPosition, real yPosition)
      signal deactivated

      property int side: 100
      width: side; height: side

      MouseArea {
          anchors.fill: parent
          onPressed: root.activated(mouse.x, mouse.y)
          onReleased: root.deactivated()
      }
  }
```

这些信号可以相同目录下另一个QML文件中的任何`SquareButton`对象接收到,信号处理器实现由客户提供:

```QML
  // myapplication.qml
  SquareButton {
      onActivated: console.log("Activated at " + xPosition + "," + yPosition)
      onDeactivated: console.log("Deactivated!")
  }
```

参见[Signal and Handler Event System](qtqml-syntax-signals.html#) 获取更多信号的使用细节.

##### 属性改变信号处理器(Property Change Signal Handlers)

属性改变信号的信号处理器采用语法:*on\<Property>Changed*,*\<Property>*是属性名首字母大写.例如,尽管`TextInput`类型文档不会记录`textChange`信号,这个信号还是隐式可用的,基于`TextInput`有一个`text`属性,所以可以写一个`onTextChanged`信号处理器在任何时候属性改变时被调用:

```QML
  import QtQuick 2.0

  TextInput {
      text: "Change this!"

      onTextChanged: console.log("Text has changed to:", text)
  }
```

### 方法属性(Method Attributes)

对象类型的方法是一个函数,他可以被调用来执行一些处理或者触发更多事件.方法可以被连接到信号以便无论何时信号被发射它都可以被自动调用.参见[Signal and Handler Event System](qtqml-syntax-signals.html#)获取更多细节.

##### 定义方法属性(Defining Method Attributes)

方法也可以被定义成c++中的类型,通过把一个类的方法打上[Q_INVOKABLE](../qtcore/qobject.html#Q_INVOKABLE)标签,然后使用QML类型系统注册,或者把它注册成此类的`Q_SLOT`.作为替代方式,使用下面的语法可以添加一个自定义方法到QML文档中的对象声明中:

```QML
  function <functionName>([<parameterName>[, ...]]) { <body> }
```

方法可以被添加到一个QML类型来定义标准独立,可复用的JavaScript代码块.这些方法可以被内部和外部对象调用.

不像信号,方法参数类型不非要被声明,因为它们默认是`var`类型.

和信号类型类似,尝试在同一个类型块中使用相同名称声明两个方法或信号也是一个错误.以及新方法会复用已存在方法名.

下面是一个带有`calculateHeight()`方法的`Rectangle`,当赋值给`height`时方法被调用:

```QML
  import QtQuick 2.0
  Rectangle {
      id: rect

      function calculateHeight() {
          return rect.width / 2;
      }

      width: 100
      height: calculateHeight()
  }
```

如果方法有参数,它们可以在方法内通过名字访问.下面,当`MouseArea`被点击时它会调用`moveTo()`方法,此方法可以指向接收到的`newX`和`newY`参数来重新定位文本:

```QML
  import QtQuick 2.0

  Item {
      width: 200; height: 200

      MouseArea {
          anchors.fill: parent
          onClicked: label.moveTo(mouse.x, mouse.y)
      }

      Text {
          id: label

          function moveTo(newX, newY) {
              label.x = newX;
              label.y = newY;
          }

          text: "Move me!"
      }
  }
```

##### 附带属性和附带信号处理器(Attached Properties and Attached Signal Handlers)

附带属性和附带信号处理器是一种机制,让对象能够使用额外的属性或信号处理器标注自己,但对对象本身不可用.尤其是它们允许对象访问与个别对象特别相关的属性或信号.

QML类型实现可以选择在c++中使用特殊的属性和信号创建附带类型.这种类型的实例随后在运行时可以被附带到指定对象上,允许这些对象取访问附带类型的属性和信号.这些可以通过在属性和各自信号处理器之前前缀附带类型名被访问.

到附带属性和处理器的引用采用下面语法形式:

```QML
  <AttachingType>.<propertyName>
  <AttachingType>.on<SignalName>
```

例如,`ListView`类型有一个附带属性`ListView.isCurrentItem`,它对每一个`ListView`中的委托对象都是可用的.这可以被每一个独立委托对象用来决定是否自己是视图中当前被选中的条目:

```QML
  import QtQuick 2.0

  ListView {
      width: 240; height: 320
      model: 3
      delegate: Rectangle {
          width: 100; height: 30
          color: ListView.isCurrentItem ? "red" : "yellow"
      }
  }
```

在这个例子中,*附带类型*名是`ListView`并且询问的属性是`inCurrentItem`,因此附带属性被指为`ListView.isCurrentItem`.

一个附带信号处理器用相同的方式指向.例如,`Component.onCompleted`附带信号处理器是被用来执行一些JavaScript代码,当组件的创建过程完成时.在下面的例子中,一旦`ListModel`完整地被创建,它的`Component.onCompleted`信号处理器将会自动被调用填充模型:

```QML
  import QtQuick 2.0

  ListView {
      width: 240; height: 320
      model: ListModel {
          id: listModel
          Component.onCompleted: {
              for (var i = 0; i < 10; i++)
                  listModel.append({"Name": "Item " + i})
          }
      }
      delegate: Text { text: index }
  }
```

因为附带类型名是`Component`并且类型有一个`completed`信号,附带信号处理器被指向作为`Component.onCompleted`.

##### 访问附带属性和信号处理器注意(A Note About Accessing Attached Properties and Signal Handlers)

通常的错误是假定附带属性和信号处理器可以从已经附带属性的对象的孩子中访问.不是这样的.附带类型的实例只能被附带到指定对象上,而不是对象和对象的孩子.

例如,下面是一个调用附带属性的早先例子的修改过的版本.这一次,委托是一个`Item`并且有颜色的`Rectangle`是那个条目的孩子:

```QML
  import QtQuick 2.0

  ListView {
      width: 240; height: 320
      model: 3
      delegate: Item {
          width: 100; height: 30

          Rectangle {
              width: 100; height: 30
              color: ListView.isCurrentItem ? "red" : "yellow"    // WRONG! This won't work.
          }
      }
  }
```

这不会和期待的那样工作,因为`ListView.isCurrentItem`是只被附带到根的delegate对象上,而非它的孩子.因为`Rectangle`是delegate的一个孩子,而不是delegate自身,它不能访问`isCurrentItem`附带属性以`ListView.isCurrentItem`.所以相反,rectangle应该通过根的delegate访问`isCurrentItem`.

```QML
  ListView {
      //....
      delegate: Item {
          id: delegateItem
          width: 100; height: 30

          Rectangle {
              width: 100; height: 30
              color: delegateItem.ListView.isCurrentItem ? "red" : "yellow"   // correct
          }
      }
  }
```

现在`delegateItem.ListView.isCurrentItem`正确指向delegate的附带属性`isCurrentItem`.

### 枚举属性(Enumeration Attributes)

枚举提供了一个固定的已命名的选择集合.在QML中,它们可以使用**`enum`**关键字声明:

```QML
  // MyText.qml
  Text {
      enum TextType {
          Normal,
          Heading
      }
  }
```

如上所示,枚举类型(TextType)和它的值(Normal)必须以大写字母开头.

值可以通过 \<Type>.\<EnumerationType>.\<Value> 指向,或者\<Type>.\<Value>.

```QML
  // MyText.qml
  Text {
      enum TextType {
          Normal,
          Heading
      }

      property int textType: MyText.TextType.Normal

      font.bold: textType == MyText.TextType.Heading
      font.pixelSize: textType == MyText.TextType.Heading ? 24 : 12
  }
```

关于在QML中使用枚举的更多信息可以在[QML Basic Types](qtqml-typesystem-basictypes.html#) [enumeration](qml-enumeration.html)文档中找到.

在QML中声明枚举的能力是在Qt 5.10中被引进的.