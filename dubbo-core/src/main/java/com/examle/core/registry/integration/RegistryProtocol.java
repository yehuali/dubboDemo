package com.examle.core.registry.integration;

import com.examle.core.common.URL;
import com.examle.core.registry.Registry;
import com.examle.core.registry.RegistryFactory;
import com.examle.core.rpc.Exporter;
import com.examle.core.rpc.Invoker;
import com.examle.core.rpc.Protocol;
import com.examle.core.rpc.RpcException;
import com.examle.core.rpc.cluster.Cluster;

import java.util.HashMap;
import java.util.Map;

import static com.examle.core.common.Constants.*;

public class RegistryProtocol implements Protocol {
    /**
     * 根据url获取对应Registry
     */
    private RegistryFactory registryFactory;

    private Cluster cluster;

    private Protocol protocol;

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

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
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
        directory.setRegistry(registry);
        directory.setProtocol(protocol);
        //REFER_KEY的所有属性
        Map<String, String> parameters = new HashMap<String, String>(directory.getUrl().getParameters());
        URL subscribeUrl = new URL(CONSUMER_PROTOCOL, parameters.remove(REGISTER_IP_KEY), 0, type.getName(), parameters);
        if (!ANY_VALUE.equals(url.getServiceInterface()) && url.getParameter(REGISTER_KEY, true)) {
            registry.register(getRegisteredConsumerUrl(subscribeUrl, url));
        }
        directory.buildRouterChain(subscribeUrl);
        //向zookeeper订阅服务节点信息并watch变更，实现服务自动发现

        Invoker invoker = cluster.join(directory);
        return invoker;
    }

    private URL getRegisteredConsumerUrl(final URL consumerUrl, URL registryUrl) {
        if (!registryUrl.getParameter(SIMPLIFIED_KEY, false)) {
            return consumerUrl.addParameters(CATEGORY_KEY, CONSUMERS_CATEGORY,
                    CHECK_KEY, String.valueOf(false));
        }else{
            System.out.println("待开发");
            return null;
        }
    }
}
