package com.examle.core.config;

import com.examle.core.common.utils.ConfigUtils;

public class PropertiesConfiguration  extends AbstractPrefixConfiguration{
    public PropertiesConfiguration(String prefix, String id) {
        super(prefix, id);
    }

    public PropertiesConfiguration() {
        this(null, null);
    }

    @Override
    public Object getInternalProperty(String key) {
        return ConfigUtils.getProperty(key);
    }
}
