#### 数据所有权(Data Ownership)

当数据从C ++传输到QML时，数据所有权始终由C ++保留。此规则的例外情况是从显式C ++方法调用返回QObject时：在这种情况下，除非通过调用`QQmlEngine.setObjectOwnership()`,指定参数`QQmlEngine :: CppOwnership`,将对象的所有权显式设置为与C ++，否则QML引擎将假定该对象的所有权.

此外，QML引擎尊从Qt C ++对象的常规QObject父所有权语义，并且永远不会删除具有父对象的QObject实例。

默认情况下，QML识别以下Qt数据类型，当从C ++传递到QML时，它们会自动转换为相应的QML基本类型，反之亦然:

| Qt Type                                                      | QML Basic Type                                               |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| bool                                                         | [bool](qml-bool.html)                                        |
| unsigned int, int                                            | [int](qml-int.html)                                          |
| double                                                       | [double](qml-double.html)                                    |
| float, qreal                                                 | [real](qml-real.html)                                        |
| [QString](../qtcore/qstring.html)                            | [string](qml-string.html)                                    |
| [QUrl](../qtcore/qurl.html)                                  | [url](qml-url.html)                                          |
| [QColor](../qtgui/qcolor.html)                               | [color](../qtquick/qml-color.html)                           |
| [QFont](../qtgui/qfont.html)                                 | [font](../qtquick/qml-font.html)                             |
| [QDateTime](../qtcore/qdatetime.html)                        | [date](qml-date.html)                                        |
| [QPoint](../qtcore/qpoint.html), [QPointF](../qtcore/qpointf.html) | [point](qml-point.html)                                      |
| [QSize](../qtcore/qsize.html), [QSizeF](../qtcore/qsizef.html) | [size](qml-size.html)                                        |
| [QRect](../qtcore/qrect.html), [QRectF](../qtcore/qrectf.html) | [rect](qml-rect.html)                                        |
| [QMatrix4x4](../qtgui/qmatrix4x4.html)                       | [matrix4x4](../qtquick/qml-matrix4x4.html)                   |
| [QQuaternion](../qtgui/qquaternion.html)                     | [quaternion](../qtquick/qml-quaternion.html)                 |
| [QVector2D](../qtgui/qvector2d.html), [QVector3D](../qtgui/qvector3d.html), [QVector4D](../qtgui/qvector4d.html) | [vector2d](../qtquick/qml-vector2d.html), [vector3d](../qtquick/qml-vector3d.html), [vector4d](../qtquick/qml-vector4d.html) |
| Enums declared with [Q_ENUM](../qtcore/qobject.html#Q_ENUM)() or Q_ENUMS() | [enumeration](qml-enumeration.html)                          |

注意：Qt GUI模块提供的类，例如QColor，QFont，QQuaternion和QMatrix4x4，仅在导入`Qt Quick`模块时,才可用于QML中

为方便起见，以上类型中许多类型,可以在QML中通过字符串值或`QtQml :: Qt`对象提供的相关方法来指定。例如，`Image :: sourceSize`属性的大小为size（将自动转换为QSize类型），并且可以由格式为“**width**x**height**”的字符串值或`Qt.size()`函数指定：

```QML
  Item {
      Image { sourceSize: "100x200" }
      Image { sourceSize: Qt.size(100,200) }
  }
```

#### 继承自QObject的类型(QObject-derived Types)

可以将任何QObject派生的类用作QML和C ++之间数据交换的类型，前提是该类已在QML类型系统中注册。

引擎允许可实例化和不可实例化类型的注册。一旦将一个类注册为QML类型，就可以将其用作在QML和C ++之间交换数据的数据类型。有关类型注册的更多详细信息，请参见在QML类型系统中注册C ++类型。