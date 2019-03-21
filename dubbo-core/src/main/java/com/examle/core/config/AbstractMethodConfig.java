package com.examle.core.config;

import java.util.Map;

public abstract class AbstractMethodConfig extends AbstractConfig {
    protected Map<String, String> parameters;

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }
}
