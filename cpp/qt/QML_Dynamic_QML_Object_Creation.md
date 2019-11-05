#### 在QML文档中创建对象

使用loader动态加载组件.它既可以加载QML文档,也可以加载Component对象.

##### loader的大小行为

如果资源组件不是一个Item类型,loader不会应用任何指定的大小规则.如果加载可视类型的对象,它会应用以下规则:

- 如果没有显式设置大小,则它自动重置自己大小为加载的Item.
- 如果它显式指定了大小,width,height或者anchor,则应用此大小.

##### 从被加载的对象接收信号

被加载的对象发出的信号可以使用Connections类型接收.

##### 焦点和按键事件

loader是一个焦点的作用域.如果任何子对象想获取焦点,它的focus属性必须设为true.任何从被加载的item中接收的按键事件应该设置accepted属性来阻止事件传播到loader上.

##### 在视图委托中使用loader

下面,被ListView插入到delegateComponent的上下文的index属性对于Text是不可访问的,因为loader实例化它时,使用myComponent的创建上下文作为父上下文,而这个上下文链内的index不指向任何东西.

#### 在JavaScript中动态创建对象(Creating Objects Dynamically)

有两种方式动态从JavaScript创建对象.你可以要么调用Qt.createComponent()函数动态创建一个Component对象,或者使用Qt.createQmlObject()函数用QML字符串创建一个对象.创建组件是更好的方式,如果你有一个已存在的被定义在QML文档的组件而且你想要动态创建那个组件的实例.否则,当对象的QML是在运行时生成的,从一个QML字符串创建对象很有用.

步骤如下:

```
1.调用Qt.createComponent()函数.它返回一个组件类型对象.
	object createComponent(url, mode, parent)
	URL是QML文件的路径.当是相对路径时,相对的是函数执行时所在文件
	另外两个参数是可选的
2.一旦得到组件对象,你可以调用它的createObject()函数返回要创建的组件实例.
	object createObject(parent, object properties)
	第一个参数是新对象的parent.可设为null.
	第二个参数是用来赋予对象属性值.使用类似JavaScript对象声明语法
		{x: 100, y: 100}
		表示x属性值为100,y属性值为100
```

还动态创建一个字符串表示的QML对象.使用[Qt.createQmlObject()](qml-qtqml-qt.html#createQmlObject-method) 函数.

object createQmlObject(*qml*, object *parent*, [string](qml-string.html) *filepath*)

第一个参数是一个表示QML类型的字符串,parent是新对象的父对象,filepath是用来关联新对象从而用于产生错误报告.

例如下面创建了一个对象:

```QML
  var newObject = Qt.createQmlObject('import QtQuick 2.0; Rectangle {color: "red"; width: 20; height: 20}',parentItem,"dynamicSnippet1");
```

#### 管理动态创建的对象(Maintaining Dynamically Created Objects)

**创建上下文(creation context)必须确保自己存活的时间比创建的对象的时间长**.否则,如果创建上下文先被销毁,在此动态创建的对象上的已有的绑定和信号处理器将不再工作.

实际的创建上下文取决于对象如何被创建:

- 如果使用`Qt.createComponent()` , 创建上下文是方法被调用的上下文对象(QQmlContext) 
- 如果使用`Qt.createQmlObject()`,创建上下文是传递给此方法的父对象的上下文
- 如果`Component{}` 对象被定义,并且createObject() 或incubateObject() 被在此定义的组件对象上调用,上下文是Component被定义的地方.

#### 删除动态对象(Deleting Objects Dynamically)

注意,绝不要删除由方便的QML对象工厂(例如[Loader](../qtquick/qml-qtquick-loader.html) 和 [Repeater](../qtquick/qml-qtquick-repeater.html))创建的动态对象.也别删除不是你自己动态创建的对象.

基于Item的动态创建的对象可以使用它的destroy()函数删除,此函数可以提供一个可选的参数指定删除之前延迟的时间,单位是毫秒.在对象自身内部调用此函数也是安全的,引擎会选择一个合适的时间销毁对象.

只有动态创建的Item对象才能动态被销毁,即调用destroy()方法销毁,否则出错.