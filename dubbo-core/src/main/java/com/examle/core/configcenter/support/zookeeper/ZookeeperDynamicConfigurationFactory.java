package com.examle.core.configcenter.support.zookeeper;

import com.examle.core.common.URL;
import com.examle.core.configcenter.AbstractDynamicConfigurationFactory;
import com.examle.core.configcenter.DynamicConfiguration;

public class ZookeeperDynamicConfigurationFactory extends AbstractDynamicConfigurationFactory {
    @Override
    protected DynamicConfiguration createDynamicConfiguration(URL url) {
        return new ZookeeperDynamicConfiguration(url);
    }
}
