# QML基本语法

### 导入语句声明(Import Statements)

一个qml文档可以有一个或多个导入声明放置在顶部

- 一个有版本的名称空间，类型已经被注册到名称空间了(例如通过插件注册)
- 一个相对路径目录，它包含了类型定义的qml文档。
- 一个 JavaScript 文件

JavaScript文件导入需要被限定，以便属性方法可以被访问到（类似于原本JavaScript的导入语句）

- `import Namespace VersionMajor.VersionMinor`
- `import Namespace VersionMajor.VersionMinor as SingletonTypeIdentifier`
- `import "directory"`
- ` import "file.js" as ScriptIdentifier`

例如：

- `import QtQuick 2.0`
- `import QtQuick.LocalStorage 2.0 as Database`
- `import "../privateComponents"`
- `import "somefile.js" as Script`

### Object Declarations

语法上说，QML代码块定义要创建的QML对象树。树对象使用*object declarations*语法来定义，它描述了要创建的

对象类型以及要被赋予对象的属性值。一个对象也可能通过内嵌对象声明语法声明子对象。

对象声明由它的对象类型的名字后跟一组花括号组成。所有属性和子对象都被声明在花括号内。

下面是一个简单的对象声明：

```qml
 Rectangle {
      width: 100
      height: 100
      color: "red"
  }
```

它声明了一个[Rectangle](qthelp://org.qt-project.qtqml.5124/qtquick/qml-qtquick-rectangle.html)类型的对象，后跟一组花括号，包含定义为那个对象的属性。[Rectangle](qthelp://org.qt-project.qtqml.5124/qtquick/qml-qtquick-rectangle.html)是一个由QtQuick模块生成的可用的类型。被定义在这个例子的属性是此矩形宽，高和颜色。

以上对象可以被引擎加载如果它是[QML document](qthelp://org.qt-project.qtqml.5124/qtqml/qtqml-documents-topic.html)一部分。这就是说，以上源码如果补全一个导入QtQuick模块的*import* statement：

```
  import QtQuick 2.0

  Rectangle {
      width: 100
      height: 100
      color: "red"
  }
```

当被放到a `.qml`文件中并且被QML引擎加载，以上代码会创建一个Rectangle对象，使用QtQuick模块提供的Rectangle类型

**注意:** 如果一个对象定义仅有少数定义属性，它可以写成如下所示的由分号分割的一行。

```
  Rectangle { width: 100; height: 100; color: "red" }
```

### Child Objects

由于任何对象声明都可以通过内嵌对象声明语法声明子对象，**任何对象都隐式声明一个可能包含任意数量的子对象的对象树**.

例如,  下面[Rectangle](qthelp://org.qt-project.qtqml.5124/qtquick/qml-qtquick-rectangle.html) object declaration包含了 [Gradient](qthelp://org.qt-project.qtqml.5124/qtquick/qml-qtquick-gradient.html) object declaration, 它又按顺序包含了两个[GradientStop](qthelp://org.qt-project.qtqml.5124/qtquick/qml-qtquick-gradientstop.html) declarations:

```
  import QtQuick 2.0

  Rectangle {
      width: 100
      height: 100

      gradient: Gradient {
          GradientStop { position: 0.0; color: "yellow" }
          GradientStop { position: 1.0; color: "green" }
      }
  }
```

当此代码被加载，它创建一个对象树，Rectangle对象在root上，此对象有两个Gradient子对象，子对象依次有两个GradientStop。

然而要注意，这是一个在QML对象树上下文内的父子关系，而不是可视场景下的上下文内。可视场景下的父子关系概念被来自QtQuick模块的*item*类型提供，它是大多数QML类型的基本类型，因为大多数QML对象都是打算被可视化渲染的。Rectangle和Text都是基于item类型的例子，下面一个Text对象已被声明作为Rectangle对象一个可视化子对象：

```
import QtQuick 2.0

  Rectangle {
      width: 200
      height: 200
      color: "red"

      Text {
          anchors.centerIn: parent
          text: "Hello, QML!"
      }
  }
```

当在以上代码中Text对象指向它的parent值时，它是指向它的可视父对象而不是对象树中的父对象。在这个例子中他们是同一个。然而尽管parent属性可以被修改来改变可视父对象，但对象树上下文中的父对象不能从QML中改变。

(另外，注意到Text对象已被声明，但没有把它赋值到Rectangle属性，这不像早先的例子，那把Gradient对象赋值到矩形的gradient属性上。这是因为item类型children属性已被设置作为类型的*默认属性*达成这种更方便的语法)

