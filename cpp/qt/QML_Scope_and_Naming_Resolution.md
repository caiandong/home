# 作用域和名字解析(Scope and Naming Resolution)

QML属性绑定,行内函数(inline function),和导入的JavaScript文件**都是运行在一个JavaScript作用域中**.作用域控制哪个变量可以被一个表达式访问到,并且当两个或更多名字冲突时,哪个变量优先选择.

JavaScript的内建作用域机制非常简单,因此QML增强了它,使它与QML语言扩展适配得更加自然.

## JavaScript作用域(JavaScript Scope)

QML作用域扩展不干涉JavaScript的本来的作用域.当在QML中编写函数,属性绑定,或者导入的JavaScript文件时,JavaScript编程人员可以复用它们已存在的知识.

下面的例子中,`addConstant()`方法将把13加到被传递的参数上,这就和编程人员期待的那样,而不用考虑QML对象上的`a`和`b`属性的值.

```QML
  QtObject {
      property int a: 3
      property int b: 9

      function addConstant(b) {
          var a = 13;
          return b + a;
      }
  }
```

QML遵从JavaScript的正常作用域规则,甚至在绑定中也适用.这太恶心了,一个讨人厌的绑定会把12赋值给QML对象的`a`属性.

```QML
  QtObject {
      property int a

      a: { var a = 12; a; }
  }
```

在QML中,每个JavaScript表达式,函数或文件都有它自己唯一的变量对象(variable object).被声明在一个变量对象中的本地对象将绝不会和另一个变量对象中的本地对象冲突.

## 类型名和导入的JavaScript文件(Type Names and Imported JavaScript Files)

[QML Documents](qtqml-documents-topic.html#)包含import语句,import语句定义了对文档可见的类型名和JavaScript文件.它们除了被用在QML声明自身,而在通过JavaScript代码访问附带属性和枚举类型时,类型名也可被用.

导入也会影响到QML文档中的每个属性绑定,JavaScript函数,甚至是内嵌的行内组件(inline component).下面的例子展示了一个简单的QML文件,它访问了一些枚举值并且调用了导入的JavaScript函数.

```QML
  import QtQuick 2.0
  import "code.js" as Code

  ListView {
      snapMode: ListView.SnapToItem

      delegate: Component {
          Text {
              elide: Text.ElideMiddle
              text: "A really, really long string that will require eliding."
              color: Code.defaultColor()
          }
      }
  }
```

## 绑定作用域对象(Binding Scope Object)

要知道有一个属性绑定的对象就是绑定的*作用域对象*.下面例子中,`Item`对象是绑定的作用域对象.

```QML
  Item {
      anchors.left: parent.left
  }
```

不需要任何限定(即不用通过id属性访问),绑定就可以访问作用域对象上的属性.在之前的例子中,绑定直接访问了`Item`的`parent`属性,没有用到任何形式的对象前缀.QML把更加结构化,面向对象的方式引进到JavaScript中,所以不需要使用JavaScript中的`this`属性.

当从绑定中访问附带属性时(attached properties)必须当心,因为这也需要和作用域对象有意想不到的交互.概念上来说,附带属性存在于所有对象,即使它们只能影响一小部分对象.结果就是未限定的附带属性的读取将总会解析到作用域对象上的附带属性,这可能不是编程人员的意图.

例如,`PathView`类型把内插值属性附带到它的委托属性,这些委托取决于它们的路径中的位置.

> 原文:For example, the [PathView](../qtquick/qml-qtquick-pathview.html) type attaches interpolated value properties to its delegates depending on their position in the path

因为`PathView`只是有目的地附加这些属性到委托中的根对象,任何访问它们(即附带属性)的子对象必须显式限定根对象,像下面那样:

```QML
  PathView {
      delegate: Component {
          Rectangle {
              id: root
              Image {
                  scale: root.PathView.scale
              }
          }
      }
  }
```

如果`Image`对象忽略`root`前缀,它会不经意访问到它自己的未设置的`PathView.scale`附带属性.

## 组件作用域(Component Scope)

QML文档中的每个QML组件都定义了一个逻辑作用域.每个文档都有至少一个根对象,但是也可以有其他行内子组件.**组件作用域是组件内的对象的id和组件根对象的属性的联合**.

> ~~此处只有孩子组件id,不包括孩子组件的孩子的id,在下一节会讨论组件实例作用域层次结构~~
>
> 经测验,可以访问组件内的任意孩子和孩子组件的id,即都在同一个作用域内,而不管父子关系.

```QML
  Item {
      property string title

      Text {
          id: titletype
          text: "<b>" + title + "</b>"
          font.pixelSize: 22
          anchors.top: parent.top
      }

      Text {
          text: titletype.text
          font.pixelSize: 18
          anchors.bottom: parent.bottom
      }
  }
```

上面例子展示了一个简单的QML组件,它在最上面显示了一个富文本title字符串,以及底部一个小的相同文本的副本.当产生显示文本时,第一个`Text`类型直接访问组件的`title`属性.根对象类型的属性可直接访问到,这使它很容易在整个组件上分发数据.

第二个`Text`类型使用id来直接访问第一个的text属性.id被编程人员显式指定了,所以它们总是优先于其他属性名之上(除非在当前JavaScript作用域中已有了声明).例如前面例子中,当前绑定作用域对象上有一个`titletype`属性,`titletype` id仍会优先选择.

## 组件实例层次结构(Component Instance Hierarchy)

QML中,组件实例会把它们的组件作用域连接在一起形成组件层次结构.**组件实例可直接访问祖先的组件作用域.**

有一种最简单的方式来演示.使用行内子组件,它自己的组件作用域被隐式地作为外部组件的孩子作用域.

```QML
  Item {
      property color defaultColor: "blue"

      ListView {
          delegate: Component {
              Rectangle {
                  color: defaultColor
              }
          }
      }
  }
```

组件实例层次结构允许委托组件取访问`Item`类型的`defaultColor`属性.当然,如果让delegate组件有一个叫做`defaultColor`的属性,那么这将优先采用.

组件实例作用域层次结构也扩展到了外部(out-of-line)组件.尽管`TitleText`类型在一个分隔的文件中,它仍然可以访问`title`属性,如果当它被用在`TitlePage`中的话.QML是动态作用域语言-取决于被使用的地方,`title`属性能够各自不同地解析.

```QML
  // TitlePage.qml
  import QtQuick 2.0
  Item {
      property string title

      TitleText {
          size: 22
          anchors.top: parent.top
      }

      TitleText {
          size: 18
          anchors.bottom: parent.bottom
      }
  }

  // TitleText.qml
  import QtQuick 2.0
  Text {
      property int size
      text: "<b>" + title + "</b>"
      font.pixelSize: size
  }
```

动态作用域很强大,但必须小心使用它才能防止qml代码的行为变得难以预测.一般来说,应该在两个组件是以另外一种方式紧密耦合在一起时,才这样使用.当构建可复用的组件时,要更倾向于使用属性接口,像这样:

```QML
  // TitlePage.qml
  import QtQuick 2.0
  Item {
      id: root
      property string title

      TitleText {
          title: root.title
          size: 22
          anchors.top: parent.top
      }

      TitleText {
          title: root.title
          size: 18
          anchors.bottom: parent.bottom
      }
  }

  // TitleText.qml
  import QtQuick 2.0
  Text {
      property string title
      property int size

      text: "<b>" + title + "</b>"
      font.pixelSize: size
  }
```

## 属性覆盖(Overridden Properties)

QML准许定义在一个对象声明中的属性名被扩展自它的另一个对象声明中定义的属性所覆盖.

```QML
  // Displayable.qml
  import QtQuick 2.0
  Item {
      property string title
      property string detail

      Text {
          text: "<b>" + title + "</b><br>" + detail
      }

      function getTitle() { return title }
      function setTitle(newTitle) { title = newTitle }
  }

  // Person.qml
  import QtQuick 2.0
  Displayable {
      property string title
      property string firstName
      property string lastName

      function fullName()  { return title + " " + firstName + " " + lastName }
  }
```

这里,名字`title`既给了Displayable的输出文本的头部,也给了Person对象的头衔.

一个覆盖属性根据引用它的作用域来解析.在Person组件作用域内,或者从指向Person组件实例的外部作用域,`title`会解析为声明在Person.qml文件中的属性.`fullName`函数会指向声明在Person之内的`title`属性.

在Displayable组件内,`title`指向被声明在Displayable.qml文件中声明的属性.getTitle()和setTitle()函数以及Text对象的`text`属性的绑定都将指向被声明在Displayable组件中的`title`属性.

尽管共享相同的名字,这两个属性是完全隔离的.其中一个属性的onChanged信号处理器将不会被另一个相同名字的属性的改变信号触发.一个到它们两者中其中一个的别名也将分别指向各自,而不会同时指向.

## JavaScript全局对象(JavaScript Global Object)

QML不允许类型,id以及属性名和全局对象中的属性冲突,以防止任何困惑.编程人员有理由相信`Math.min(10, 9) `将会如预期地工作.