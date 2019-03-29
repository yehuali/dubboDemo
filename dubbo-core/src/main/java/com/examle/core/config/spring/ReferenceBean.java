package com.examle.core.config.spring;

import com.examle.core.common.extension.SpringExtensionFactory;
import com.examle.core.common.utils.StringUtils;
import com.examle.core.config.ReferenceConfig;
import com.examle.core.config.RegistryConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 1.FactoryBean 用来创建比较复杂的bean
 * @param <T>
 */
public class ReferenceBean<T> extends ReferenceConfig<T> implements FactoryBean, ApplicationContextAware, InitializingBean {
    private transient ApplicationContext applicationContext;

    @Override
    public Object getObject() throws Exception {
        return get();
    }



    @Override
    public Class<?> getObjectType() {
        return getInterfaceClass();
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        if ((getRegistries() == null || getRegistries().isEmpty())
                && (getConsumer() == null || getConsumer().getRegistries() == null || getConsumer().getRegistries().isEmpty())
                && (getApplication() == null || getApplication().getRegistries() == null || getApplication().getRegistries().isEmpty())) {
            Map<String, RegistryConfig> registryConfigMap = applicationContext == null ? null : BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, RegistryConfig.class, false, false);
            if (registryConfigMap != null && registryConfigMap.size() > 0) {
                List<RegistryConfig> registryConfigs = new ArrayList<>();
                if (StringUtils.isNotEmpty(registryIds)) {


                }

                if (registryConfigs.isEmpty()) {
                    for (RegistryConfig config : registryConfigMap.values()) {
                        if (StringUtils.isEmpty(registryIds)) {
                            registryConfigs.add(config);
                        }
                    }
                }

                if (!registryConfigs.isEmpty()) {
                    super.setRegistries(registryConfigs);
                }
            }

        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        SpringExtensionFactory.addApplicationContext(applicationContext);
    }
}
