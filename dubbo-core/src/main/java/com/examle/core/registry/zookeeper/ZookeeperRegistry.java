package com.examle.core.registry.zookeeper;

import com.examle.core.common.URL;
import com.examle.core.registry.support.FailbackRegistry;

public class ZookeeperRegistry extends FailbackRegistry {

    private final ZookeeperClient zkClient;

    public ZookeeperRegistry(URL url, ZookeeperTransporter zookeeperTransporter) {
        super(url);
        zkClient = zookeeperTransporter.connect(url);
    }
}
