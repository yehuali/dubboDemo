package com.examle.core.config;

import com.examle.core.common.Constants;
import com.examle.core.common.URL;
import com.examle.core.common.Version;
import com.examle.core.common.extension.ExtensionLoader;
import com.examle.core.common.utils.ConfigUtils;
import com.examle.core.common.utils.StringUtils;
import com.examle.core.common.utils.UrlUtils;
import com.examle.core.config.context.ConfigManager;
import com.examle.core.config.support.Parameter;
import com.examle.core.configcenter.DynamicConfiguration;
import com.examle.core.configcenter.DynamicConfigurationFactory;
import com.examle.core.registry.RegistryService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract  class AbstractInterfaceConfig extends AbstractMethodConfig {

    protected ConfigCenterConfig configCenter;

    /**
     * The application info
     */
    protected ApplicationConfig application;

    /**
     * The module info
     */
    protected ModuleConfig module;

    protected String registryIds;

    /**
     * Registry centers
     */
    protected List<RegistryConfig> registries;


    @Parameter(excluded = true)
    public String getRegistryIds() {
        return registryIds;
    }

    protected List<URL> loadRegistries(boolean provider) {
        List<URL> registryList = new ArrayList<URL>();
        if (registries != null && !registries.isEmpty()) {
            for (RegistryConfig config : registries) {
                String address = config.getAddress();
                if (StringUtils.isEmpty(address)) {
                    address = Constants.ANYHOST_VALUE;
                }

                if (!RegistryConfig.NO_AVAILABLE.equalsIgnoreCase(address)) {
                    Map<String, String> map = new HashMap<String, String>();
                    appendParameters(map, application);
                    appendParameters(map, config);
                    map.put("path", RegistryService.class.getName());
                    appendRuntimeParameters(map);
                    if (!map.containsKey("protocol")) {
                        map.put("protocol", "dubbo");
                    }
                    List<URL> urls = UrlUtils.parseURLs(address, map);
                    for (URL url : urls) {
                        url = url.addParameter(Constants.REGISTRY_KEY, url.getProtocol());
                        url = url.setProtocol(Constants.REGISTRY_PROTOCOL);
                        if ((provider && url.getParameter(Constants.REGISTER_KEY, true))
                                || (!provider && url.getParameter(Constants.SUBSCRIBE_KEY, true))) {
                            registryList.add(url);
                        }
                    }
                }
            }
        }
        return registryList;
    }

    static void appendRuntimeParameters(Map<String, String> map) {
        map.put(Constants.DUBBO_VERSION_KEY, Version.getProtocolVersion());
        map.put(Constants.RELEASE_KEY, Version.getVersion());
        map.put(Constants.TIMESTAMP_KEY, String.valueOf(System.currentTimeMillis()));
        if (ConfigUtils.getPid() > 0) {
            map.put(Constants.PID_KEY, String.valueOf(ConfigUtils.getPid()));
        }
    }


    public ConfigCenterConfig getConfigCenter() {
        return configCenter;
    }

    public void setConfigCenter(ConfigCenterConfig configCenter) {
        ConfigManager.getInstance().setConfigCenter(configCenter);
        this.configCenter = configCenter;
    }

    void startConfigCenter() {
        if (this.configCenter != null) {
            this.configCenter.refresh();
            prepareEnvironment();
        }
    }

    private void prepareEnvironment() {
        if (configCenter.isValid()) {
            DynamicConfiguration dynamicConfiguration = getDynamicConfiguration(configCenter.toUrl());
        }
    }

    private DynamicConfiguration getDynamicConfiguration(URL url) {
        DynamicConfigurationFactory factories = ExtensionLoader
                .getExtensionLoader(DynamicConfigurationFactory.class)
                .getExtension(url.getProtocol());
        DynamicConfiguration configuration = factories.getDynamicConfiguration(url);
        Environment.getInstance().setDynamicConfiguration(configuration);
        return configuration;
    }

    public ModuleConfig getModule() {
        return module;
    }

    public ApplicationConfig getApplication() {
        return application;
    }

    public void setApplication(ApplicationConfig application) {
        ConfigManager.getInstance().setApplication(application);
        this.application = application;
    }

    public List<RegistryConfig> getRegistries() {
        return registries;
    }

    public void setRegistries(List<? extends RegistryConfig> registries) {
        ConfigManager.getInstance().addRegistries((List<RegistryConfig>) registries);
        this.registries = (List<RegistryConfig>) registries;
    }

}
