package com.examle.core.config;

import com.examle.core.config.support.Parameter;

/**
 * 当 ProtocolConfig 和 ServiceConfig 某属性没有配置时，采用此缺省值，可选
 */
public class ProviderConfig extends AbstractServiceConfig{
    private Boolean isDefault;

    @Parameter(excluded = true)
    public Boolean isDefault() {
        return isDefault;
    }
}
