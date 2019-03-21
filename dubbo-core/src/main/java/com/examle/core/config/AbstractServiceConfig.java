package com.examle.core.config;

import java.util.List;

public abstract class AbstractServiceConfig extends AbstractInterfaceConfig {
    /**
     * Whether to export the service
     */
    protected Boolean export;

    /**
     * The service group
     */
    protected String group;

    /**
     * The service version
     */
    protected String version;

    /**
     * The protocol list the service will export with
     */
    protected List<ProtocolConfig> protocols;

    public Boolean getExport() {
        return export;
    }
}
