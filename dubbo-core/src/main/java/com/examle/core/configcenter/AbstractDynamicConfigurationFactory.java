package com.examle.core.configcenter;

import com.examle.core.common.URL;

public abstract class AbstractDynamicConfigurationFactory implements DynamicConfigurationFactory {

    private volatile DynamicConfiguration dynamicConfiguration;

    @Override
    public DynamicConfiguration getDynamicConfiguration(URL url) {
        if (dynamicConfiguration == null) {
            synchronized (this) {
                if (dynamicConfiguration == null) {
                    dynamicConfiguration = createDynamicConfiguration(url);
                }
            }
        }
        return dynamicConfiguration;
    }

    protected abstract DynamicConfiguration createDynamicConfiguration(URL url);
}
