package com.examle.core.config;

import com.examle.core.common.URL;
import com.examle.core.common.utils.StringUtils;
import com.examle.core.common.utils.UrlUtils;
import com.examle.core.config.support.Parameter;

import java.util.Map;

public class ConfigCenterConfig extends AbstractConfig {

    private String address;

    private String protocol;

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    @Parameter(excluded = true)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    @Parameter(excluded = true)
    public boolean isValid() {
        if (StringUtils.isEmpty(address)) {
            return false;
        }

        return address.contains("://") || StringUtils.isNotEmpty(protocol);
    }

    public URL toUrl() {
        return UrlUtils.parseURL(address, null);
    }
}
