package com.examle.core.config;

public class SystemConfiguration extends AbstractPrefixConfiguration {
    public SystemConfiguration(String prefix, String id) {
        super(prefix, id);
    }
    @Override
    public Object getInternalProperty(String key) {
        return System.getProperty(key);
    }

}
