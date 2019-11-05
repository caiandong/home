# 从c++中定义QML类型(Defining QML Types from C++ )

### 注册不可实例化类型(Registering Non-Instantiable Types)

有时候一个继承自QObject的类可能需要被注册到QML类型系统,但不能作为一个可实例化的类型.例如,如果一个类:

- 是不应该实例化的接口类型
- 是不需要暴露给QML的基类类型(base class type)
- 声明了需要从QML中访问的枚举,但它不应该被实例化
- 是一个为QML提供的单实例类型,不该从QML中实例化

`Qt QML`模块提供了几个方法来注册不可实例化类型:

- `qmlRegisterType()`(不带参数)注册一个不可实例化的类型,也不能从QML中指向. This enables the engine to coerce any inherited types that are instantiable from QML.
- `qmlRegisterInterface()`注册已有的qt接口类型.这种类型不可从QML中实例化,你不能用它声明QML属性(property).然而,从QML中使用这种类型的c++属性就是做期待的接口转型.
- `qmlRegisterUncreatableType()`注册一个不可实例化的有名字的c++类型但对于QML类型系统是一个可辨别的类型.如果要从QML中访问一个类型的枚举或附带属性,但类型本身不要被实例化,就可用到它.
- `qmlRegisterSingletonType()`注册一个单例类型,可以从QML中导入它.

注意所有注册到QML类型系统的c++类型必须是继承自QObject的,即使是不可实例化的类型.

#### Registering Singleton Objects with a Singleton Type