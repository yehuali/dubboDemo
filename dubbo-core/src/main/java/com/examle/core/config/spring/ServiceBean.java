package com.examle.core.config.spring;

import com.examle.core.common.extension.SpringExtensionFactory;
import com.examle.core.common.utils.StringUtils;
import com.examle.core.config.*;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.Map;

import static com.examle.core.config.spring.util.BeanFactoryUtils.addApplicationListener;

/**
 * 1.Spring bean的创建和销毁：实现InitializingBean，DisposableBean
 * 2.通过ApplicationContextAware拿到上下文
 * 3.ApplicationListener 接口作用：监听所有通过applicationContext.publistEvent(event)事件
 *  -->在spring启动初始化完成时会调用publistEvent发布事件
 * 4.BeanNameAware通过这个Bean可以获取自己在容器中的名字
 * 参考：https://blog.csdn.net/qbmmj/article/details/75733987
 * setApplicationContext:发布事件
 * onApplicationEvent：监听事件 --->暴露服务
 * @param <T>
 */
public class ServiceBean<T> extends ServiceConfig<T> implements InitializingBean,
        ApplicationContextAware, ApplicationListener<ContextRefreshedEvent> {

    private transient ApplicationContext applicationContext;

    private transient boolean supportedApplicationListener;


    //传入bean所在容器的引用
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        //把Spring容器传入SpringExtensionFactory
        SpringExtensionFactory.addApplicationContext(applicationContext);
        // ServiceBean也是一个监听器，并将此注入到容器中
        supportedApplicationListener = addApplicationListener(applicationContext, this);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (!isExported() && !isUnexported()) {
            //发布服务
            export();
        }
    }

    @Override
    public void export() {
        super.export();
//        publishExportEvent();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (getProvider() == null) {
            /**
             * 找到上下文中定义的ProviderConfig，同时把他们排序，载入到一个链里进行维护和管理
             */
            Map<String, ProviderConfig> providerConfigMap =
                    applicationContext == null ? null :
                            BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, ProviderConfig.class, false, false);
            if (providerConfigMap != null && providerConfigMap.size() > 0) {

            }

            if (getApplication() == null
                    && (getProvider() == null || getProvider().getApplication() == null)) {
                Map<String, ApplicationConfig> applicationConfigMap = applicationContext == null ? null :
                        BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, ApplicationConfig.class, false, false);
                if (applicationConfigMap != null && applicationConfigMap.size() > 0) {
                    ApplicationConfig applicationConfig = null;
                    for (ApplicationConfig config : applicationConfigMap.values()) {
                        if (applicationConfig != null) {
                            throw new IllegalStateException("Duplicate application configs: " + applicationConfig + " and " + config);
                        }
                        applicationConfig = config;
                    }
                    if (applicationConfig != null) {
                        setApplication(applicationConfig);
                    }
                }
                if (getModule() == null
                        && (getProvider() == null || getProvider().getModule() == null)) {
                    Map<String, ModuleConfig> moduleConfigMap = applicationContext == null ? null :
                            BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, ModuleConfig.class, false, false);
                    if (moduleConfigMap != null && moduleConfigMap.size() > 0) {

                    }
                }

                if (StringUtils.isEmpty(getRegistryIds())) {

                }

            }

            //配置中心
            if (getConfigCenter() == null) {
                Map<String, ConfigCenterConfig> configenterMap = applicationContext == null ? null :
                        BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, ConfigCenterConfig.class, false, false);
                if (configenterMap != null && configenterMap.size() == 1) {
                    super.setConfigCenter(configenterMap.values().iterator().next());
                }
            }

        }
    }


}
