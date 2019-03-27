package com.examle.core.config;

import com.examle.core.config.support.Parameter;

import java.util.Map;

public class ProtocolConfig extends AbstractConfig {
    private Map<String, String> parameters;
    /**
     * Protocol name
     */
    private String name;

    private Boolean isDefault;

    @Parameter(excluded = true)
    public String getName() {
        return name;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public Boolean isDefault() {
        return isDefault;
    }

    public void setDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }


}
