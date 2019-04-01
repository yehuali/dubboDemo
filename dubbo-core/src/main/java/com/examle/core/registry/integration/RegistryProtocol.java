package com.examle.core.registry.integration;

import com.examle.core.common.URL;
import com.examle.core.registry.Registry;
import com.examle.core.registry.RegistryFactory;
import com.examle.core.rpc.Exporter;
import com.examle.core.rpc.Invoker;
import com.examle.core.rpc.Protocol;
import com.examle.core.rpc.RpcException;
import com.examle.core.rpc.cluster.Cluster;

import static com.examle.core.common.Constants.DEFAULT_REGISTRY;
import static com.examle.core.common.Constants.REGISTRY_KEY;

public class RegistryProtocol implements Protocol {
    /**
     * 根据url获取对应Registry
     */
    private RegistryFactory registryFactory;

    private Cluster cluster;

    /**
     * 代理会遍历set方法并进行注入
     * @param registryFactory
     */
    public void setRegistryFactory(RegistryFactory registryFactory) {
        this.registryFactory = registryFactory;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    @Override
    public <T> Exporter<T> export(Invoker<T> invoker) throws RpcException {
        return null;
    }

    @Override
    public <T> Invoker<T> refer(Class<T> type, URL url) throws RpcException {
        url = url.setProtocol(url.getParameter(REGISTRY_KEY, DEFAULT_REGISTRY)).removeParameter(REGISTRY_KEY);
        Registry registry = registryFactory.getRegistry(url);
        return doRefer(cluster, registry, type, url);
    }

    private <T> Invoker<T> doRefer(Cluster cluster, Registry registry, Class<T> type, URL url) {
        RegistryDirectory<T> directory = new RegistryDirectory<T>(type, url);
        Invoker invoker = cluster.join(directory);
        return invoker;
    }
}
