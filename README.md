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
    package com.examle.core.rpc;
    import com.examle.core.common.extension.ExtensionLoader;
    public class ProxyFactory$Adaptive implements com.examle.core.rpc.ProxyFactory {
        public com.examle.core.rpc.Invoker getInvoker(java.lang.Object arg0, java.lang.Class arg1, com.examle.core.common.URL arg2) throws com.examle.core.rpc.RpcException {
            if (arg2 == null) throw new IllegalArgumentException("url == null");
            com.examle.core.common.URL url = arg2;
            String extName = url.getParameter("proxy", "javassist");
            if(extName == null) throw new IllegalStateException("Fail to get extension(com.examle.core.rpc.ProxyFactory) name from url(" + url.toString() + ") use keys([proxy])");
            com.examle.core.rpc.ProxyFactory extension = (com.examle.core.rpc.ProxyFactory)ExtensionLoader.getExtensionLoader(com.examle.core.rpc.ProxyFactory.class).getExtension(extName);
            return extension.getInvoker(arg0, arg1, arg2);
       }
   }
    ```
    
### 总结
- 简易流程图
  ![spi简易图](https://github.com/yehuali/dubboDemo/blob/master/images/SPI%E7%AE%80%E6%98%93%E5%9B%BE.png)
    - 分析
       - ExtensionLoader的type为需要扩展的类，objectFactory为ExtensionFactory的扩展（源码得出为AdaptiveExtensionFactory）
       - 顺序： 1.通过配置文件加载adaptive类（如果没有采用动态编程的方式） 2.通过adpative类去代理扩展类（从配置文件加载）
       - 通过adpative类去代理扩展类： 1.通过URL获取key 2.通过key找到相应的扩展类
    - @SPI、@Activate、@Adaptive注解分析
        - 需要扩展的类加上@SPI注解
        - 扩展的实现类加上@Activate注解
        - @Adaptive为标记实现一个适配器类，并且动态生成（通过代理生成）
            - 参考资料：https://blog.csdn.net/qq924862077/article/details/77510121
                
                
                
          ```
          EXTENSION_LOADERS 例如protocol和对应ExtensionLoader
            1.ExtensionLoader（new创建）
              type 扩展接口名
          	objectFactory：ExtensionFactory的AdaptiveExtension --->AdaptiveExtensionFactory 
          cachedAdaptiveInstance:存放代理类
            1.代理类含有需要代理的类（扩展类）
              -->cachedClasses:存放扩展类（从配置文件获取）
          	1.1 需要扩展的类需要加上SPI注解，注解上的值为默认扩展
            2.如果从配置文件没有加载代理类，需要单独创建
              2.1  单独创建需要通过动态编程的方式实现 
          	
          3.通过代理类加载扩展类
            3.1 在EXTENSION_LOADERS中根据type查找ExtensionLoader
            3.2 根据url中的name在cachedInstances(<String, Holder>)中查找实例（刚开始为空）
               3.2.1 根据扩展类名在EXTENSION_INSTANCES中查找、
          	       --->如果没有，则clazz.newInstance()后放入其中
               3.2.2 遍历扩展类实例的set方法，对其实例属性进行注入
          		  --->objectFactory.getExtension(pt, property)//根据ExtensionFactory的代理类获取待注入类
          				--->根据ExtensionFactory的扩展类去加载（没有则添加）
          				
          ```      
3.  自定义Bean的过程
    - xml到beanDefinition过程
    ![beanDefinition生成过程](https://github.com/yehuali/dubboDemo/tree/master/images/xml到beanDefinition解析过程.jpg)  
        - 通过id（>name>interface属性>类名）在容器里注册beanDefinition      
    - beanDefinition到bean的过程
    ![bean生成过程](http://www.ibm.com/developerworks/cn/java/j-lo-spring-principle/origin_image012.gif)
        - 参考资料：https://blog.kazaff.me/2015/01/26/dubbo%E5%A6%82%E4%BD%95%E4%B8%80%E6%AD%A5%E4%B8%80%E6%AD%A5%E6%8B%BF%E5%88%B0bean/
    - serviceBean的afterPropertiesSet方法对相关组件进行加载
    
4.  动态编程

5.  服务导出   
    - URL格式
    ![loadRegistries的map存放信息](https://github.com/yehuali/dubboDemo/blob/master/images/loadRegistries%E7%9A%84map%E5%AD%98%E6%94%BE%E4%BF%A1%E6%81%AF.jpg)
    ```
        registry://127.0.0.1:2181/org.apache.dubbo.registry.RegistryService?application=meetup-demo-provider&dubbo=2.0.2&pid=21160&registry=zookeeper&release=2.7.0&timestamp=1553236781382
    ```
    ![doExportUrlsFor1Protocol的map信息](https://github.com/yehuali/dubboDemo/blob/master/images/doExportUrlsFor1Protocol%E7%9A%84map%E4%BF%A1%E6%81%AF.jpg)
    - 服务注册流程图
    ![服务注册流程图](https://github.com/yehuali/dubboDemo/blob/master/images/%E6%9C%8D%E5%8A%A1%E6%B3%A8%E5%86%8C%E6%B5%81%E7%A8%8B%E5%9B%BE.png)
    - Invoker 
        - 创建Wrapper(通过操作字节码生成Wrapper的子类)
            ```
                ClassGenerator cc = ClassGenerator.newInstance(cl);
                cc.setClassName //类名
                cc.setSuperClass   //父类名
                cc.addDefaultConstructor(); //默认无参构造
                cc.addField("") //添加字段
                cc.addMethod //添加方法
                Class<?> wc = cc.toClass(); //转换为class类
                wc.newInstance() //实例化
                
            ```
         - 封装成Invoker 
            - Invoker.invoker最终调用 Wrapper.invokeMethod 
    - 注册中心
    
6.  服务引用
    - 引用时机
        - Spring容器调用ReferenceBean的afterPropertiesSet引用服务（饿汉式）
        - 在ReferenceBean对应的服务被注入到其他类中引用（懒汉式）
        - 默认情况下，Dubbo使用懒汉式。如果需要饿汉式，通过配置<dubbo:reference>的init属性开启
    - 引用入口
        - ReferenceBean的getObject方法
            - 当服务被注入其他类时,Spring会第一时间调用getObject方法
                - 不管哪种引用，最后会得到一个Invoker实例，但并不能暴露给用户使用，通过代理工厂类ProxyFactory为服务接口生成代理类
     

7.  ServiceBean的源码分析
    - 参考资料：https://github.com/shuaijunlan/shuaijunlan.github.io/blob/master/images/ServiceBean.png?raw=true
    - ServiceBean类继承关系
    ![ServiceBean类继承关系](https://github.com/shuaijunlan/shuaijunlan.github.io/blob/master/images/ServiceBean.png?raw=true)
 
         
         
- 参考资料
    - Dubbo官网：http://dubbo.apache.org/zh-cn/
    - SPI机制：https://mp.weixin.qq.com/s/oQ_CludXm6ymlsZdJg_rLw            
        