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
    - 总结
        - 简易流程图
        - 分析
           - ExtensionLoader的type为需要扩展的类，objectFactory为ExtensionFactory的扩展（源码得出为AdaptiveExtensionFactory）
3.  自定义Bean的过程
    - xml到beanDefinition过程
    ![beanDefinition生成过程](https://github.com/yehuali/dubboDemo/tree/master/images/xml到beanDefinition解析过程.jpg)        
    - beanDefinition到bean的过程
    ![bean生成过程](http://www.ibm.com/developerworks/cn/java/j-lo-spring-principle/origin_image012.gif)
        - 参考资料：https://blog.kazaff.me/2015/01/26/dubbo%E5%A6%82%E4%BD%95%E4%B8%80%E6%AD%A5%E4%B8%80%E6%AD%A5%E6%8B%BF%E5%88%B0bean/

4.  服务注册   
    - URL格式
    ![loadRegistries的map存放信息](https://github.com/yehuali/dubboDemo/tree/master/images/loadRegistries的map存放信息.jpg)
    ```
        registry://127.0.0.1:2181/org.apache.dubbo.registry.RegistryService?application=meetup-demo-provider&dubbo=2.0.2&pid=21160&registry=zookeeper&release=2.7.0&timestamp=1553236781382
    ```
    ![doExportUrlsFor1Protocol的map信息](https://github.com/yehuali/dubboDemo/tree/master/images/doExportUrlsFor1Protocol的map信息.jpg)
    - 服务注册流程图
    

4.  ServiceBean的源码分析
    - 参考资料：https://github.com/shuaijunlan/shuaijunlan.github.io/blob/master/images/ServiceBean.png?raw=true
    - ServiceBean类继承关系
    ![ServiceBean类继承关系](https://github.com/shuaijunlan/shuaijunlan.github.io/blob/master/images/ServiceBean.png?raw=true)
 
         
         
- 参考资料
    - Dubbo官网：http://dubbo.apache.org/zh-cn/
    - SPI机制：https://mp.weixin.qq.com/s/oQ_CludXm6ymlsZdJg_rLw            
        