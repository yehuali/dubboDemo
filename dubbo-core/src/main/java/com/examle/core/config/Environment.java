package com.examle.core.config;

import com.examle.core.common.Constants;
import com.examle.core.common.utils.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Environment {

    private static final Environment INSTANCE = new Environment();

    private Map<String, SystemConfiguration> systemConfigs = new ConcurrentHashMap<>();

    public static Environment getInstance() {
        return INSTANCE;
    }

    public CompositeConfiguration getConfiguration(String prefix, String id) {
        CompositeConfiguration compositeConfiguration = new CompositeConfiguration();
        compositeConfiguration.addConfiguration(this.getSystemConfig(prefix, id));
        return compositeConfiguration;
    }

    public SystemConfiguration getSystemConfig(String prefix, String id) {
        return systemConfigs.computeIfAbsent(toKey(prefix, id), k -> new SystemConfiguration(prefix, id));
    }

    private static String toKey(String prefix, String id) {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotEmpty(prefix)) {
            sb.append(prefix);
        }
        if (StringUtils.isNotEmpty(id)) {
            sb.append(id);
        }
        if (sb.length() > 0 && sb.charAt(sb.length() - 1) != '.') {
            sb.append(".");
        }
        if (sb.length() > 0) {
            return sb.toString();
        }
        return Constants.DUBBO;

    }

}
