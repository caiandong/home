数据通过已命名的数据角色提供给委托，委托可以绑定这些数据角色。这是一个具有type和age两个角色的`ListModel`，以及一个带有绑定到这些角色以显示角色值的委托的`ListView`

```QML
  import QtQuick 2.0

  Item {
      width: 200; height: 250

      ListModel {
          id: myModel
          ListElement { type: "Dog"; age: 8 }
          ListElement { type: "Cat"; age: 5 }
      }

      Component {
          id: myDelegate
          Text { text: type + ", " + age }
      }

      ListView {
          anchors.fill: parent
          model: myModel
          delegate: myDelegate
      }
  }
```

如果模型的属性和委托的属性之间存在命名冲突，则可以使用限定的*`model`*名称来访问角色。例如，如果一个`Text`类型有`type`或`age`的属性，在上述示例中的文本将显示自己的属性值,而不是模型项(model item)的`type`和`age`。在这种情况下，可以将属性引用为`model.type`和`type`或`age`，以确保委托显示模型项中的属性值。

委托还可以使用一个特殊的*index*角色，该角色包含模型中的项(item)的索引。注意，如果项从模型中删除，则此索引被置为-1。如果您绑定到index角色，请确保逻辑考虑到index为-1的可能性，即项不再有效。(通常项将很快被销毁，但在一些视图中可以通过一个`delayRemove`附加属性来延迟委托销毁。)

一个没有已经命名的角色的模型（例如下面显示的ListModel）将会有被*modelData*角色提供的据。只有一个角色的模型也会有*modelData*角色。在这种情况下，*modelData*角色包含与命名角色相同的数据。

QML在内置的QML类型集合中提供了好几种类型的数据模型。另外，可以使用Qt c++创建模型，然后通过QML组件(components)将其提供给`QQmlEngine`使用。有关创建这些模型的信息，请访问与`Qt Quick Views`一起使用c++模型和创建QML类型的文章。