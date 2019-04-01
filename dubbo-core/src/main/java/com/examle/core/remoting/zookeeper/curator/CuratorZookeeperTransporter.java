package com.examle.core.remoting.zookeeper.curator;

import com.examle.core.common.URL;
import com.examle.core.registry.zookeeper.ZookeeperClient;
import com.examle.core.remoting.zookeeper.support.AbstractZookeeperTransporter;

public class CuratorZookeeperTransporter extends AbstractZookeeperTransporter {

    public ZookeeperClient createZookeeperClient(URL url) {
        return new CuratorZookeeperClient(url);
    }
}
