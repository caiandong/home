## Standard Expression Syntax(标准表达式句法)
### Simple expressions:
 * Variable Expressions: ${...}
  
  * 利用OGNL表达式从context中获取变量值，(在springmvc中，会被替换为spel表达式)
 * Selection Variable Expressions: *{...}
  
   * 只要没有被`选中的对象`,$符号和*符号完全相同。`被选中的对象`是th:object属性的表达式结果。
  ```html
   <div th:object="${session.user}">
    <p>Name: <span th:text="*{firstName}">Sebastian</span>.</p>
    <p>Surname: <span th:text="*{lastName}">Pepper</span>.</p>
    <p>Nationality: <span th:text="*{nationality}">Saturn</span>.</p>
  </div>
  ```
  等价于
  ```html
   <div>
    <p>Name: <span th:text="${session.user.firstName}">Sebastian</span>.</p>
    <p>Surname: <span th:text="${session.user.lastName}">Pepper</span>.</p>
    <p>Nationality: <span th:text="${session.user.nationality}">Saturn</span>.</p>
   </div>
  ```
 * Message Expressions: #{...}

  
 * Link URL Expressions: @{...}

 * Fragment Expressions: ~{...}
### Conditional operators:
If-then: (if) ? (then)

If-then-else: (if) ? (then) : (else)

Default: (value) ?: (defaultvalue)
