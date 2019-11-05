# qt属性系统(The Property System)

Qt provides a sophisticated property system similar to the ones supplied by some compiler vendors. However, as a compiler- and platform-independent library, Qt does not rely on non-standard compiler features like __property or [property]. The Qt solution works with *any* standard C++ compiler on every platform Qt supports. It is based on the [Meta-Object System](metaobjects.html#) that also provides inter-object communication via [signals and slots](signalsandslots.html#). 

## 声明属性的需求准备(Requirements for Declaring Properties)

为了声明一个属性(property),在一个继承自`QObject`的类中使用[Q_PROPERTY()](qobject.html#Q_PROPERTY) 宏.

```c++
  Q_PROPERTY(type name
             (READ getFunction [WRITE setFunction] |
              MEMBER memberName [(READ getFunction | WRITE setFunction)])
             [RESET resetFunction]
             [NOTIFY notifySignal]
             [REVISION int]
             [DESIGNABLE bool]
             [SCRIPTABLE bool]
             [STORED bool]
             [USER bool]
             [CONSTANT]
             [FINAL])
```

这里有些典型的属性声明的例子,它们从`QWidget`中节选的

```cpp
  Q_PROPERTY(bool focus READ hasFocus)
  Q_PROPERTY(bool enabled READ isEnabled WRITE setEnabled)
  Q_PROPERTY(QCursor cursor READ cursor WRITE setCursor RESET unsetCursor)
```

这里展示了使用`MEMBER`关键字如何导出成员变量作为Qt属性.注意,`NOTIFY`信号必须指定来允许QML属性绑定.

```cpp
      Q_PROPERTY(QColor color MEMBER m_color NOTIFY colorChanged)
      Q_PROPERTY(qreal spacing MEMBER m_spacing NOTIFY spacingChanged)
      Q_PROPERTY(QString text MEMBER m_text NOTIFY textChanged)
      ...
  signals:
      void colorChanged();
      void spacingChanged();
      void textChanged(const QString &newText);

  private:
      QColor  m_color;
      qreal   m_spacing;
      QString m_text;
```

属性的行为就像类的数据成员,但通过**元对象系统**([Meta-Object System](metaobjects.html#))还可以访问到额外的特性.

- 如果没有`MEMBER`变量被指定的话,`READ`访问器函数是必须的.定义它是为了读取属性值.理想情况下,const函数可以用于此目的,而且**必须返回要么属性的类型,要么是一个到那种类型的const引用**(此处是c++中的引用,下面出现的引用都指的是c++中的).例如,`QWidget::focus`是一个只读属性,它有一个`READ`函数:`QWidget::hasFocus()`.
- `WRITE`访问器函数是可选的.它被用来设置属性值.它**必须返回void而且必须只有一个参数,要么是属性的类型,要么是指向那个属性的指针或引用**. 例如,`QWidget::enabled`有`WRITE` 函数 `QWidget::setEnabled()`.只读属性不需要`WRITE`函数,例如,`QWidtet::enabled`没有`WRITE`函数.
- `MEMBER`变量关联在没有`READ`访问器被指定时才需要.这让那个成员变量变得可读可写,并不需要创建`READ`和`WRITE`访问器函数.如果你需要控制变量的访问,除了`MEMBER`变量关联,也是可以使用`READ`或`WRITE`访问器函数.(只能选择一种方式)
- `RESET`函数是可选的.It is for setting the property back to its context specific default value. e.g., QWidget::cursor has the typical READ and WRITE functions, QWidget::cursor() and QWidget::setCursor(), and it also has a RESET function, QWidget::unsetCursor(), since no call to QWidget::setCursor() can mean reset to the context specific cursor. The RESET function must return void and take no parameters.
- `NOTIFY`信号是可选的.如果定义了,它应该指定一个在那个类中存在的信号,无论何时属性值变化,都应该发射这个信号.`MEMBER`变量的`NOTIFY`信号必须有0个或一个参数,并且必须和属性类型一样.参数会使用属性的新值.`NOTIFY`信号只应该在当属性真正被改变时发射,例如,为了避免在QML绑定中不必要的重新计算.Qt自动会发射那个信号,当一个没有显式setter的`MEMBER`属性需要时.

- A REVISION number is optional. If included, it defines the property and its notifier signal to be used in a particular revision of the API (usually for exposure to QML). If not included, it defaults to 0.
- The DESIGNABLE attribute indicates whether the property should be visible in the property editor of GUI design tool (e.g., Qt Designer). Most properties are DESIGNABLE (default true). Instead of true or false, you can specify a boolean member function.
- The SCRIPTABLE attribute indicates whether this property should be accessible by a scripting engine (default true). Instead of true or false, you can specify a boolean member function.
- The STORED attribute indicates whether the property should be thought of as existing on its own or as depending on other values. It also indicates whether the property value must be saved when storing the object's state. Most properties are STORED (default true), but e.g., [QWidget::minimumWidth](../qtwidgets/qwidget.html#minimumWidth-prop)() has STORED false, because its value is just taken from the width component of property [QWidget::minimumSize](../qtwidgets/qwidget.html#minimumSize-prop)(), which is a [QSize](qsize.html).
- The USER attribute indicates whether the property is designated as the user-facing or user-editable property for the class. Normally, there is only one USER property per class (default false). e.g., [QAbstractButton::checked](../qtwidgets/qabstractbutton.html#checked-prop) is the user editable property for (checkable) buttons. Note that [QItemDelegate](../qtwidgets/qitemdelegate.html) gets and sets a widget's USER property.
- The presence of the CONSTANT attribute indicates that the property value is constant. For a given object instance, the READ method of a constant property must return the same value every time it is called. This constant value may be different for different instances of the object. A constant property cannot have a WRITE method or a NOTIFY signal.
- The presence of the FINAL attribute indicates that the property will not be overridden by a derived class. This can be used for performance optimizations in some cases, but is not enforced by moc. Care must be taken never to override a FINAL property.

`READ`,` WRITE`,和`RESET`函数可被继承.它们也可以是虚的.当一个类在多重继承时,它们必须是继承自第一个继承类中.

属性类型可以是任何被[QVariant](qvariant.html)支持的类型,也可以是用户定义(user-defined)的类型.这个例子中`QDate`被认为是用户定义类型.

```cpp
  Q_PROPERTY(QDate date READ getDate WRITE setDate)
```

因为`QDate`是用户定义的,在属性声明时你必须包含`<QDate>`头文件.

由于历史原因,*QMap*和*QList*作为属性类型与*QVariantMap* 和 *QVariantList*是同义词.

> 原文:	QMap* and *QList* as property types are synonym of *QVariantMap* and *QVariantList*. 
>

## 属性和自定义类型(Properties and Custom Types)

被属性使用的自定义类型需要使用`Q_DECLARE_METATYPE()`宏注册，以便它们的值可以存储在`QVariant`对象中。这使得它们既适用于在类定义中使用[Q_PROPERTY](qobject.html#Q_PROPERTY)() 宏声明的静态属性，也适用于在运行时创建的动态属性。

### Q_DECLARE_METATYPE(*Type*)

此宏让类型***Type***对`QMetaType`可知,前提是***Type***提供了**公有的默认构造函数**,**公有复制构造函数**和**公有析构函数**.在`QVariant`中,需要使用类型*Type*作为一个自定义类型.

此宏要求该类型在使用时是完全定义的类型。对于指针类型，它还要求完全定义被指向类型。与`Q_DECLARE_OPAQUE_POINTER()`一起使用，来注册前向声明类型的指针.

> 原文:Use in conjunction with [Q_DECLARE_OPAQUE_POINTER](qmetatype.html#Q_DECLARE_OPAQUE_POINTER)() to register pointers to forward declared types.

理想情况下，**这个宏应该放在类或结构的声明下面**。如果这是不可能的，它可以放在一个私有头文件中，每次在`Qvariant`中使用该类型时都必须包含该文件。

添加`Q_DECLARE_METATYPE()` 使所有基于模板的函数都知道该类型,包括`QVariant`.注意,如果你打算使用`queued`类型的信号和槽的连接(即connect函数的第五个参数)或者在QObject属性系统中使用,还必须调用[qRegisterMetaType](qmetatype.html#qRegisterMetaType-1)() ，因为这些名称在运行时被解析。

```cpp
  struct MyStruct
  {
      int i;
      ...
  };

  Q_DECLARE_METATYPE(MyStruct)
```

既然`MyStruct`现在已被[QMetaType](qmetatype.html)知道,它可以被用在[QVariant](qmetatype.html#Type-enum):

```cpp
  MyStruct s;
  QVariant var;
  var.setValue(s); // copy s into the variant

  ...

  // retrieve the value
  MyStruct s2 = var.value<MyStruct>();
```

一些类型被自动注册,不需要此宏:

- 指向继承自 [QObject](qobject.html)类的类的指针
- [QList](qlist.html)\<T>, [QVector](qvector.html)\<T>, [QQueue](qqueue.html)\<T>, [QStack](qstack.html)\<T>, [QSet](qset.html)\<T> or [QLinkedList](qlinkedlist.html)\<T> .T需要是已注册的元类型
- [QHash](qhash.html#qhash)<T1, T2>, [QMap](qmap.html)<T1, T2> or [QPair](qpair.html)<T1, T2> T1,T2已被注册为元类型
- [QPointer](qpointer.html)\<T>, [QSharedPointer](qsharedpointer.html)\<T>, [QWeakPointer](qweakpointer.html)\<T>, T是继承自 [QObject](qobject.html)的类型
- 使用 [Q_ENUM](qobject.html#Q_ENUM) or [Q_FLAG](qobject.html#Q_FLAG)注册的枚举类型
- 有 [Q_GADGET](qobject.html#Q_GADGET) 宏的类(class)