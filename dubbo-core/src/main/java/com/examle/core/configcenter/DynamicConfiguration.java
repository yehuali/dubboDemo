package com.examle.core.configcenter;

import com.examle.core.config.Configuration;
import com.examle.core.config.Environment;

import java.util.Optional;

import static com.examle.core.common.extension.ExtensionLoader.getExtensionLoader;

public interface DynamicConfiguration extends Configuration {

    String DEFAULT_GROUP = "dubbo";

    /**
     * Find DynamicConfiguration instance
     * @return DynamicConfiguration instance
     */
    static DynamicConfiguration getDynamicConfiguration() {
        Optional<Configuration> optional = Environment.getInstance().getDynamicConfiguration();
        return (DynamicConfiguration) optional.orElseGet(() -> getExtensionLoader(DynamicConfigurationFactory.class)
                .getDefaultExtension()
                .getDynamicConfiguration(null));
    }

    default void addListener(String key, ConfigurationListener listener) {
        addListener(key, DEFAULT_GROUP, listener);
    }

    void addListener(String key, String group, ConfigurationListener listener);

    default String getConfig(String key) {
        return getConfig(key, null, 0L);
    }

    /**
     * 获取映射到给定键和给定组的配置。如果
     * 配置超时后无法获取，将抛出IllegalStateException。
     */
    String getConfig(String key, String group, long timeout) throws IllegalStateException;
}
