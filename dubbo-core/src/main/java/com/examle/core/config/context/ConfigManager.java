package com.examle.core.config.context;

import com.examle.core.common.utils.StringUtils;
import com.examle.core.config.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static com.examle.core.common.Constants.DEFAULT_KEY;

public class ConfigManager {

    private static final Logger logger = LoggerFactory.getLogger(ConfigManager.class);

    private ApplicationConfig application;

    private static final ConfigManager configManager = new ConfigManager();

    private Map<String, RegistryConfig> registries = new ConcurrentHashMap<>();
    private Map<String, ProviderConfig> providers = new ConcurrentHashMap<>();


    private ConfigCenterConfig configCenter;

    public static ConfigManager getInstance() {
        return configManager;
    }

    public Optional<ProviderConfig> getDefaultProvider() {
        return Optional.ofNullable(providers.get(DEFAULT_KEY));
    }

    public void addProvider(ProviderConfig providerConfig) {
        if (providerConfig == null) {
            return;
        }

        String key = StringUtils.isNotEmpty(providerConfig.getId())
                ? providerConfig.getId()
                : (providerConfig.isDefault() == null || providerConfig.isDefault()) ? DEFAULT_KEY : null;

        if (StringUtils.isEmpty(key)) {
            throw new IllegalStateException("A ProviderConfig should either has an id or it's the default one, " + providerConfig);
        }

        if (providers.containsKey(key) && !providerConfig.equals(providers.get(key))) {
            logger.warn("Duplicate ProviderConfig found, there already has one default ProviderConfig or more than two ProviderConfigs have the same id, " +
                    "you can try to give each ProviderConfig a different id. " + providerConfig);
        } else {
            providers.put(key, providerConfig);
        }
    }


    public void setApplication(ApplicationConfig application) {
        if (application != null) {
            checkDuplicate(this.application, application);
            this.application = application;
        }
    }

    private void checkDuplicate(AbstractConfig oldOne, AbstractConfig newOne) {
        if (oldOne != null && !oldOne.equals(newOne)) {
            String configName = oldOne.getClass().getSimpleName();
            throw new IllegalStateException("Duplicate Config found for " + configName + ", you should use only one unique " + configName + " for one application.");
        }
    }

    public void setConfigCenter(ConfigCenterConfig configCenter) {
        if (configCenter != null) {
            checkDuplicate(this.configCenter, configCenter);
            this.configCenter = configCenter;
        }
    }

    public void addRegistries(List<RegistryConfig> registryConfigs) {
        if (registryConfigs != null) {
            registryConfigs.forEach(this::addRegistry);
        }
    }

    public void addRegistry(RegistryConfig registryConfig) {
        if (registryConfig == null) {
            return;
        }

        String key = StringUtils.isNotEmpty(registryConfig.getId())
                ? registryConfig.getId()
                : (registryConfig.isDefault() == null || registryConfig.isDefault()) ? DEFAULT_KEY : null;

        if (StringUtils.isEmpty(key)) {
            throw new IllegalStateException("A RegistryConfig should either has an id or it's the default one, " + registryConfig);
        }

        if (registries.containsKey(key) && !registryConfig.equals(registries.get(key))) {
            logger.warn("Duplicate RegistryConfig found, there already has one default RegistryConfig or more than two RegistryConfigs have the same id, " +
                    "you can try to give each RegistryConfig a different id. " + registryConfig);
        } else {
            registries.put(key, registryConfig);
        }
    }

}
