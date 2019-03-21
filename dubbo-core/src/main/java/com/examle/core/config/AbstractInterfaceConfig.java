package com.examle.core.config;

import com.examle.core.common.URL;
import com.examle.core.config.context.ConfigManager;
import com.examle.core.config.support.Parameter;

import java.util.ArrayList;
import java.util.List;

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


    @Parameter(excluded = true)
    public String getRegistryIds() {
        return registryIds;
    }

    protected List<URL> loadRegistries(boolean provider) {
        List<URL> registryList = new ArrayList<URL>();
        return registryList;
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
        }
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
}
