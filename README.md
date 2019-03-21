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
    - 自定义配置步骤
        - 设计配置属性和JavaBean
        - 编写XSD文件
        - 编写NamespaceHandler和BeanDefinitionParser完成解析工作
        - 编写spring.handlers和spring.schemas串联起所有部件
        - 在Bean文件中应用
        
2.  SPI(服务发现机制)
    - 本质：将接口实现类的全限定名配置在文件中 --> 由加载器读取配置文件（加载实现类） --> 运行时为接口替换实现类
    - 通过SPI机制为程序提供拓展功能，Dubbo增强了JAVA SPI机制
    - Dubbo SPI所需的配置文件需放置在META-INF/dubbo
        - 通过ExtensionLoader加载指定实现类，配置文件需放置在META-INF/dubbo
        - 支持按需加载接口实现类，还增加了IOC和AOP等特性
     ```
     配置文件：
      optimusPrime = org.apache.spi.OptimusPrime
      bumblebee = org.apache.spi.Bumblebee
     
    public class DubboSPITest {

        @Test
        public void sayHello() throws Exception {
            ExtensionLoader<Robot> extensionLoader = 
                ExtensionLoader.getExtensionLoader(Robot.class);
            Robot optimusPrime = extensionLoader.getExtension("optimusPrime");
            optimusPrime.sayHello();
            Robot bumblebee = extensionLoader.getExtension("bumblebee");
            bumblebee.sayHello();
        }
    }
    ```
    
3.  ServiceBean的源码分析
    - 参考资料：https://github.com/shuaijunlan/shuaijunlan.github.io/blob/master/images/ServiceBean.png?raw=true
    - ServiceBean类继承关系
    ![ServiceBean类继承关系](https://github.com/shuaijunlan/shuaijunlan.github.io/blob/master/images/ServiceBean.png?raw=true)
 
         
         
- 参考资料
    - Dubbo官网：http://dubbo.apache.org/zh-cn/
    - SPI机制：https://mp.weixin.qq.com/s/oQ_CludXm6ymlsZdJg_rLw            
        