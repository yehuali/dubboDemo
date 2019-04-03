package com.examle.core.registry.zookeeper;

import com.examle.core.common.URL;
import com.examle.core.registry.support.FailbackRegistry;
import com.examle.core.remoting.zookeeper.StateListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZookeeperRegistry extends FailbackRegistry {

    private final static Logger logger = LoggerFactory.getLogger(ZookeeperRegistry.class);

    private final ZookeeperClient zkClient;

    public ZookeeperRegistry(URL url, ZookeeperTransporter zookeeperTransporter) {
        super(url);
        zkClient = zookeeperTransporter.connect(url);
        zkClient.addStateListener(new StateListener() {

            @Override
            public void stateChanged(int state) {
                if (state == RECONNECTED) {
                    try {
//                        recover();
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                }
            }
        });
    }

    @Override
    public void register(URL url) {

    }
}
