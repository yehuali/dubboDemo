package com.examle.core.config.spring.schema;

import com.examle.core.config.ApplicationConfig;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * 1.NamespaceHandler会根据schema和节点名找到某个BeanDefinitionParser
 * 2.BeanDefinitionParser完成具体的解析工作
 */
public class DubboNamespaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {
        registerBeanDefinitionParser("application", new DubboBeanDefinitionParser(ApplicationConfig.class, true));
    }
}
