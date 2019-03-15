1.  Dubbo基于Spring 的Schema扩展进行加载
    - 元素介绍
        - complexType:元素定义复杂类型
            - sequence：要求组中的元素以指定顺序出现在包含元素中，每个子元素可出现0到任意次
        - annotation：顶级元素，规定schema的注释
        ```
        <annotation
        id=ID
        any attributes
        >
        
        (appinfo|documentation)*
        
        </annotation>
        ```
        