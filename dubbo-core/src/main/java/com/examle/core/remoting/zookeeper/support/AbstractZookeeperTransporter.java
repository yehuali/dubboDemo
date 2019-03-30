package com.examle.core.remoting.zookeeper.support;

import com.examle.core.common.URL;
import com.examle.core.registry.zookeeper.ZookeeperClient;
import com.examle.core.registry.zookeeper.ZookeeperTransporter;

public abstract class AbstractZookeeperTransporter implements ZookeeperTransporter {
    @Override
    public ZookeeperClient connect(URL url) {
        return null;
    }
}
