package com.examle.core.config;

import com.examle.core.common.Constants;
import com.examle.core.common.utils.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Environment {

    private static final Environment INSTANCE = new Environment();

    private Map<String, SystemConfiguration> systemConfigs = new ConcurrentHashMap<>();

    private Configuration dynamicConfiguration;

    private boolean configCenterFirst = true;

    public static Environment getInstance() {
        return INSTANCE;
    }


    /**
     * 为每个调用创建实例，因为它是在启动时调用，所以我认为潜在的成本并不大
     * 否则，如果使用缓存，我们应该确保每个配置都有一个唯一的id,这很难保证，因为是在用户这边
     * @param prefix
     * @param id
     * @return
     */
    public CompositeConfiguration getConfiguration(String prefix, String id) {
        CompositeConfiguration compositeConfiguration = new CompositeConfiguration();
        /**
         * 配置中心有更高的权限
         * AppExternalConfig、ExternalConfig、PropertiesConfig待加入
         */
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

    public void setDynamicConfiguration(Configuration dynamicConfiguration) {
        this.dynamicConfiguration = dynamicConfiguration;
    }

    public boolean isConfigCenterFirst() {
        return configCenterFirst;
    }

}
