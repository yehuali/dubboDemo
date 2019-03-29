package com.examle.core.registry.zookeeper;

import com.examle.core.common.URL;
import com.examle.core.registry.Registry;
import com.examle.core.registry.support.AbstractRegistryFactory;

public class ZookeeperRegistryFactory extends AbstractRegistryFactory {

    private ZookeeperTransporter zookeeperTransporter;

    public void setZookeeperTransporter(ZookeeperTransporter zookeeperTransporter) {
        this.zookeeperTransporter = zookeeperTransporter;
    }

    @Override
    protected Registry createRegistry(URL url) {
        return new ZookeeperRegistry(url, zookeeperTransporter);
    }
}
