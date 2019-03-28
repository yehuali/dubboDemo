package com.examle.core.config;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 基于内存的配置
 */
public class InmemoryConfiguration  extends AbstractPrefixConfiguration{
    //存储配置键值对
    private Map<String, String> store = new LinkedHashMap<>();

    public InmemoryConfiguration(String prefix, String id) {
        super(prefix, id);
    }

    public void addProperties(Map<String, String> properties) {
        if (properties != null) {
            this.store.putAll(properties);
        }
    }

    @Override
    public Object getInternalProperty(String key) {
        return store.get(key);
    }
}
