package com.examle.core.config.spring;

import com.examle.core.config.ConfigCenterConfig;

import java.util.Map;

public class ConfigCenterBean extends ConfigCenterConfig {
    private Map<String, String> parameters;

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }
}
