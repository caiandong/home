# 属性绑定(Property Binding )

一个对象的属性(property)可以被赋予一个静态值,它会保存不变直到被显式地赋予一个新值.然而,为了最大程度利用QML和它的对动态对象行为的内建支持,大多数QML对象使用**属性绑定**(`property binding`)

属性绑定是QML核心特性,可以让开发者指定不同对象属性之间的关系.

在这场景后面,QML引擎监视属性的依赖(即是绑定表达式中的变量).当某个改变被检测到,QML引擎重新计算绑定表达式,并且把新结果应用到此属性上.

### 总览(Overview)

为了创建一个属性绑定,一个属性就要被赋予一个可以计算出想要的值的JavaScript表达式.最简单的情况,一个绑定可能是另一个属性的引用.看下面的例子,蓝色`Rectangle`的高度被绑定到它的父的高度:

```QML
  Rectangle {
      width: 200; height: 200

      Rectangle {
          width: 100
          height: parent.height
          color: "blue"
      }
  }
```

无论何时父rectangle的高度产生变化,蓝色rectangle的高度自动更需你成相同的值.

一个绑定可以包含任何合法的JavaScript表达式或语句,因为QML使用一个高度标准的JavaScript引擎.绑定可以访问对象属性,调用方法,使用内建JavaScript对象例如`Date`和`Math`.下面是前面例子中其他的可能的绑定:

```QML
  height: parent.height / 2

  height: Math.min(parent.width, parent.height)

  height: parent.height > 100 ? parent.height : parent.height/2

  height: {
      if (parent.height > 100)
          return parent.height
      else
          return parent.height / 2
  }

  height: someMethodThatReturnsHeight()
```

下面是更复杂的调用更多对象和类型的例子:

```QML
  Column {
      id: column
      width: 200
      height: 200

      Rectangle {
          id: topRect
          width: Math.max(bottomRect.width, parent.width/2)
          height: (parent.height / 3) + 10
          color: "yellow"

          TextInput {
              id: myTextInput
              text: "Hello QML!"
          }
      }

      Rectangle {
          id: bottomRect
          width: 100
          height: 50
          color: myTextInput.text.length <= 10 ? "red" : "blue"
      }
  }
```

前面的例子中,

- topRect.width 取决于 bottomRect.width 和 column.width

- topRect.height 取决于 column.height
- bottomRect.color 取决于 myTextInput.text.length

语法上来说,绑定无论多复杂都被允许.然而,如果绑定过于复杂-例如调用多行,或过多循环-这可能意味着绑定的目的可能不仅仅在于描述属性间关系.复杂的绑定会减少代码的性能,可读性,可维护性.重新设计有复杂绑定的组件,或者至少将绑定因素分解到一个分隔的函数中.作为一般的规则,用户不应该依赖绑定的计算顺序.

### 从JavaScript中创建属性绑定(Creating Property Bindings from JavaScript)

一个绑定的属性在必要时会自动被更新.然而,如果这个属性稍后从一个JavaScript语句中被赋予一个静态值,那么绑定会被移除.

例如,下面`Rectangle`开始时确保它的`height`总是它的`width`的两倍.然而,当space键被按下时,当前值是`3*width`,然后结果被作为一个静态值赋给`height`.在那之后`height`将保持这个值固定不变,即使`width`发生变化.静态值赋值会移除绑定.

```QML
  import QtQuick 2.0

  Rectangle {
      width: 100
      height: width * 2

      focus: true
      Keys.onSpacePressed: {
          height = width * 3
      }
  }
```

如果本来打算的是给rectangle一个固定高度并且阻止自动更新,那么这没问题.然而,如果本打算是建立新的`width`与`height`之间的关系,那么反而新绑定表达式必须用Qt.binding()函数包装.

```QML
  import QtQuick 2.0

  Rectangle {
      width: 100
      height: width * 2

      focus: true
      Keys.onSpacePressed: {
          height = Qt.binding(function() { return width * 3 })
      }
  }
```

现在,在space被按下之后,rectangle的高度将继续自动更新为它自己的三倍宽度.

#### 如何debug绑定覆盖问题(Debugging overwriting of bindings)

QML应用中,一般的bug原因是不小心偶然从JavaScript语句中使用静态值覆盖了绑定.为了帮助开发者向下跟踪这类问题,无论什么时候由于命令赋值而丢失了绑定,QML引擎都能够发出消息.

为了生成这样的消息,你需要启用通知类型的输出到`qt.qml.binding.removal`日志类别,例如通过如下调用:

```QML
QLoggingCategory::setFilterRules(QStringLiteral("qt.qml.binding.removal.info=true"));
```

请参考[QLoggingCategory](../qtcore/qloggingcategory.html)文档获取更多关于启用来自日志类别的输出.

注意在一些情况下覆盖绑定是相当合理的.任何由QML引擎生成的消息都应该被作为诊断帮助或者在没有更多调查前不必作为证据,来对待.

#### 与属性绑定一起使用this(Using this with Property Binding)

当从JavaScript中创建属性绑定时,`this`关键字可以被用来指向接受绑定的对象.这在解析有歧义的属性名时非常有用.

例如,下面`Component.onCompleted`处理器被定义在`Item`的作用域内.在这个作用域,`width`指向`Item`的宽度,而不是`Rectangle`的宽度.为了绑定`Rectangle`的`height`到它自己的`width`,绑定表达式必须显式指向`this.width`(或者替换为`rect.width`).

```QML
  Item {
      width: 500
      height: 500

      Rectangle {
          id: rect
          width: 100
          color: "yellow"
      }

      Component.onCompleted: {
          rect.height = Qt.binding(function() { return this.width * 2 })
          console.log("rect.height = " + rect.height) // prints 200, not 1000
      }
  }
```

**注意**:this值在属性绑定之外是未定义的.参见 [JavaScript Environment Restrictions](qtqml-javascript-hostenvironment.html#javascript-environment-restrictions)获取更多细节.

**参见** [Positioning with Anchors](../qtquick/qtquick-positioning-anchors.html). 