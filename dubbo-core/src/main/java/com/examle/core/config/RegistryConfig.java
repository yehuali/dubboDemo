package com.examle.core.config;

import com.examle.core.common.utils.StringUtils;
import com.examle.core.config.support.Parameter;

public class RegistryConfig extends AbstractConfig {
    public static final String NO_AVAILABLE = "N/A";
    /**
     * Whether it's default
     */
    private Boolean isDefault;

    /**
     * Register center address
     */
    private String address;

    @Parameter(excluded = true)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        if (address != null) {
            int i = address.indexOf("://");
            if (i > 0) {
                this.updateIdIfAbsent(address.substring(0, i));
            }
        }
    }

    public void updateIdIfAbsent(String value) {
        if (StringUtils.isNotEmpty(value) && StringUtils.isEmpty(id)) {
            this.id = value;
        }
    }

    public Boolean isDefault() {
        return isDefault;
    }

    public void setDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }
}
