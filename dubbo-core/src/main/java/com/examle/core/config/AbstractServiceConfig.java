package com.examle.core.config;

import com.examle.core.config.context.ConfigManager;
import com.examle.core.config.support.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public abstract class AbstractServiceConfig extends AbstractInterfaceConfig {

    protected static final Logger logger = LoggerFactory.getLogger(AbstractConfig.class);
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

    protected String protocolIds;

    public Boolean getExport() {
        return export;
    }

    public List<ProtocolConfig> getProtocols() {
        return protocols;
    }

    @Parameter(excluded = true)
    public String getProtocolIds() {
        return protocolIds;
    }

    public void setProtocols(List<? extends ProtocolConfig> protocols) {
        ConfigManager.getInstance().addProtocols((List<ProtocolConfig>) protocols);
        this.protocols = (List<ProtocolConfig>) protocols;
    }
}
