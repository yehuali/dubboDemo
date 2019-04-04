package com.examle.core.registry.integration;

import com.examle.core.common.Constants;
import com.examle.core.common.URL;
import com.examle.core.common.Version;
import com.examle.core.common.utils.NetUtils;
import com.examle.core.common.utils.StringUtils;
import com.examle.core.registry.Registry;
import com.examle.core.rpc.Invoker;
import com.examle.core.rpc.Protocol;
import com.examle.core.rpc.RpcException;
import com.examle.core.rpc.cluster.RouterChain;
import com.examle.core.rpc.cluster.directory.AbstractDirectory;
import com.examle.core.rpc.model.ApplicationModel;
import org.aopalliance.intercept.Invocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RegistryDirectory<T> extends AbstractDirectory<T> {

    private static final Logger logger = LoggerFactory.getLogger(RegistryDirectory.class);

    private final Class<T> serviceType;
    private final String serviceKey;
    private final Map<String, String> queryMap;
    private volatile boolean forbidden = false;

    private Protocol protocol; // Initialization at the time of injection, the assertion is not null
    private Registry registry;

    protected RouterChain<T> routerChain;

    private final URL directoryUrl;//在构造时初始化，断言不为空，并始终分配非空值
    private volatile URL overrideDirectoryUrl;//在构造时初始化，断言不为空，并始终分配非空值

    private static final ConsumerConfigurationListener consumerConfigurationListener = new ConsumerConfigurationListener();

    private static class ConsumerConfigurationListener extends AbstractConfiguratorListener {
        List<RegistryDirectory> listeners = new ArrayList<>();

        ConsumerConfigurationListener() {
            this.initWith(ApplicationModel.getApplication() + Constants.CONFIGURATORS_SUFFIX);
        }

        void addNotifyListener(RegistryDirectory listener) {
            this.listeners.add(listener);
        }

    }

    public RegistryDirectory(Class<T> serviceType, URL url) {
        super(url);
        if (serviceType == null) {
            throw new IllegalArgumentException("service type is null.");
        }
        if (url.getServiceKey() == null || url.getServiceKey().length() == 0) {
            throw new IllegalArgumentException("registry serviceKey is null.");
        }

        this.serviceType = serviceType;
        this.serviceKey = url.getServiceKey();
        this.queryMap = StringUtils.parseQueryString(url.getParameterAndDecoded(Constants.REFER_KEY));
        this.overrideDirectoryUrl = this.directoryUrl = turnRegistryUrlToConsumerUrl(url);
    }

    private URL turnRegistryUrlToConsumerUrl(URL url) {
        //在registry中保存对新url有用的任何参数
        String isDefault = url.getParameter(Constants.DEFAULT_KEY);
        if (StringUtils.isNotEmpty(isDefault)) {
            queryMap.put(Constants.REGISTRY_KEY + "." + Constants.DEFAULT_KEY, isDefault);
        }
        return url.setPath(url.getServiceInterface())
                .clearParameters()
                .addParameters(queryMap)
                .removeParameter(Constants.MONITOR_KEY);
    }

    public void setRegistry(Registry registry) {
        this.registry = registry;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    @Override
    protected List<Invoker<T>> doList(Invocation invocation) throws RpcException {
        if (forbidden) {
            // 1.没有服务提供者 2.禁用服务提供者
            throw new RpcException(RpcException.FORBIDDEN_EXCEPTION, "No provider available from registry " +
                    getUrl().getAddress() + " for service " + getConsumerUrl().getServiceKey() + " on consumer " +
                    NetUtils.getLocalHost() + " use dubbo version " + Version.getVersion() +
                    ", please check status of providers(disabled, not registered or in blacklist).");
        }

        List<Invoker<T>> invokers = null;
        try {
            // 从缓存中获取调用器，只执行运行时路由器
            invokers = routerChain.route(getConsumerUrl(), invocation);
        } catch (Throwable t) {
            logger.error("Failed to execute router: " + getUrl() + ", cause: " + t.getMessage(), t);
        }

        return invokers;
    }

    @Override
    public URL getUrl() {
        return this.overrideDirectoryUrl;
    }

    @Override
    public Class<T> getInterface() {
        return serviceType;
    }

    public void buildRouterChain(URL url) {
        this.setRouterChain(RouterChain.buildChain(url));
    }

    public void setRouterChain(RouterChain<T> routerChain) {
        this.routerChain = routerChain;
    }

    public void subscribe(URL url) {
        setConsumerUrl(url);
        consumerConfigurationListener.addNotifyListener(this);
    }
}
