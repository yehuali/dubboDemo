package com.examle.core.config.spring;

import com.examle.core.config.ReferenceConfig;
import org.springframework.beans.factory.FactoryBean;

/**
 * 1.FactoryBean 用来创建比较复杂的bean
 * @param <T>
 */
public class ReferenceBean<T> extends ReferenceConfig<T> implements FactoryBean {



    @Override
    public Object getObject() throws Exception {
        return get();
    }

    @Override
    public Class<?> getObjectType() {
        return getInterfaceClass();
    }


}
