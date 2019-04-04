package com.examle.core.registry.integration;

import com.examle.core.common.utils.StringUtils;
import com.examle.core.configcenter.ConfigChangeEvent;
import com.examle.core.configcenter.ConfigChangeType;
import com.examle.core.configcenter.ConfigurationListener;
import com.examle.core.configcenter.DynamicConfiguration;
import com.examle.core.rpc.cluster.Configurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

public  abstract class AbstractConfiguratorListener implements ConfigurationListener {

    private static final Logger logger = LoggerFactory.getLogger(AbstractConfiguratorListener.class);

    protected List<Configurator> configurators = Collections.emptyList();

    protected final void initWith(String key) {
        DynamicConfiguration dynamicConfiguration = DynamicConfiguration.getDynamicConfiguration();
        dynamicConfiguration.addListener(key, this);
        String rawConfig = dynamicConfiguration.getConfig(key);
        if (!StringUtils.isEmpty(rawConfig)) {
            process(new ConfigChangeEvent(key, rawConfig));
        }
    }


    @Override
    public void process(ConfigChangeEvent event) {
        if (logger.isInfoEnabled()) {
            logger.info("Notification of overriding rule, change type is: " + event.getChangeType() +
                    ", raw config content is:\n " + event.getValue());
        }

        if (event.getChangeType().equals(ConfigChangeType.DELETED)) {
            configurators.clear();
        }else{
            try {
                //parseConfigurators将自动识别应用程序/服务配置。

            } catch (Exception e) {
                logger.error("Failed to parse raw dynamic config and it will not take effect, the raw config is: " +
                        event.getValue(), e);
                return;
            }
        }
    }
}
