package com.examle.core.config;

import java.util.List;

public class ApplicationConfig extends AbstractConfig{
    private List<RegistryConfig> registries;

    public List<RegistryConfig> getRegistries() {
        return registries;
    }

    public void setRegistries(List<RegistryConfig> registries) {
        this.registries = registries;
    }
}
