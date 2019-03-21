package com.examle.core.config;

import com.examle.core.config.support.Parameter;

public class ProtocolConfig extends AbstractConfig {
    /**
     * Protocol name
     */
    private String name;

    @Parameter(excluded = true)
    public String getName() {
        return name;
    }
}
