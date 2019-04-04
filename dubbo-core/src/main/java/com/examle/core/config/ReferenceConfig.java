package com.examle.core.config;

import com.examle.core.common.Constants;
import com.examle.core.common.URL;
import com.examle.core.common.Version;
import com.examle.core.common.bytecode.Wrapper;
import com.examle.core.common.extension.ExtensionLoader;
import com.examle.core.common.utils.ConfigUtils;
import com.examle.core.common.utils.NetUtils;
import com.examle.core.common.utils.StringUtils;
import com.examle.core.config.context.ConfigManager;
import com.examle.core.rpc.Invoker;
import com.examle.core.rpc.Protocol;
import com.examle.core.rpc.ProxyFactory;
import com.examle.core.rpc.protocol.injvm.InjvmProtocol;

import java.util.*;

public class ReferenceConfig<T> extends AbstractReferenceConfig {
    private String interfaceName;

    private Class<?> interfaceClass;

    protected String mock;

    private ConsumerConfig consumer;

    /**
     * The invoker of the reference service
     */
    private transient volatile Invoker<?> invoker;
    /**
     * The url for peer-to-peer invocation
     */
    private String url;

    /**
     * The url of the reference service
     */
    private final List<URL> urls = new ArrayList<URL>();

    private static final Protocol refprotocol = ExtensionLoader.getExtensionLoader(Protocol.class).getAdaptiveExtension();

    private static final ProxyFactory proxyFactory = ExtensionLoader.getExtensionLoader(ProxyFactory.class).getAdaptiveExtension();

    /**
     * The method configs
     */
    private List<MethodConfig> methods;

    //接口代理引用
    private transient volatile T ref;

    private transient volatile boolean initialized;

    public Class<?> getInterfaceClass() {
        if (interfaceClass != null) {
            return interfaceClass;
        }
        try {
            if (interfaceName != null && interfaceName.length() > 0) {
                this.interfaceClass = Class.forName(interfaceName, true, Thread.currentThread()
                        .getContextClassLoader());
            }
        }catch (ClassNotFoundException t) {
            throw new IllegalStateException(t.getMessage(), t);
        }
        return interfaceClass;
    }

    public synchronized T get() {
        checkAndUpdateSubConfigs();

        if (ref == null) {
            init();
        }
        return ref;
    }

    public void checkAndUpdateSubConfigs() {
        startConfigCenter();
        // get consumer's global configuration
        checkDefault();
    }

    private void checkDefault() {
        createConsumerIfAbsent();
    }

    private void createConsumerIfAbsent() {
        if (consumer != null) {
            return;
        }
        setConsumer(
                ConfigManager.getInstance()
                        .getDefaultConsumer()
                        .orElseGet(() -> {
                            ConsumerConfig consumerConfig = new ConsumerConfig();
                            consumerConfig.refresh();
                            return consumerConfig;
                        })
        );
    }

    public ConsumerConfig getConsumer() {
        return consumer;
    }

    public void setConsumer(ConsumerConfig consumer) {
        ConfigManager.getInstance().addConsumer(consumer);
        this.consumer = consumer;
    }

    private void init() {
        if (initialized) {
            return;
        }
        initialized = true;
        checkStubAndLocal(interfaceClass);
        Map<String, String> map = new HashMap<String, String>();
        map.put(Constants.SIDE_KEY, Constants.CONSUMER_SIDE);
        appendRuntimeParameters(map);
        if (!isGeneric()) {
            String[] methods = Wrapper.getWrapper(interfaceClass).getMethodNames();
            if (methods.length == 0) {

            }else{
                map.put("methods", StringUtils.join(new HashSet<String>(Arrays.asList(methods)), ","));
            }
        }
        map.put(Constants.INTERFACE_KEY, interfaceName);
        appendParameters(map, application);
        appendParameters(map, module);
        appendParameters(map, consumer, Constants.DEFAULT_KEY);
        appendParameters(map, this);
        Map<String, Object> attributes = null;
        if (methods != null && !methods.isEmpty()) {

        }
        String hostToRegistry = ConfigUtils.getSystemProperty(Constants.DUBBO_IP_TO_REGISTRY);
        if (hostToRegistry == null || hostToRegistry.length() == 0) {
            hostToRegistry = NetUtils.getLocalHost();
        }
        map.put(Constants.REGISTER_IP_KEY, hostToRegistry);
        ref = createProxy(map);
    }

    private T createProxy(Map<String, String> map) {
        URL tmpUrl = new URL("temp", "localhost", 0, map);
        final boolean isJvmRefer;
        if (isInjvm() == null) {
            if (url != null && url.length() > 0) {
                isJvmRefer = false;
            }else{
                //默认情况下，如果有本地服务，则引用它
                isJvmRefer = InjvmProtocol.getInjvmProtocol().isInjvmRefer(tmpUrl);
            }
        }else{
            isJvmRefer = false;//待写
        }

        if (isJvmRefer) {

        }else{
            if (url != null && url.length() > 0) {

            }else{//从注册中心的配置中组装URL
                List<URL> us = loadRegistries(false);
                if (us != null && !us.isEmpty()) {
                    for (URL u : us) {
                        urls.add(u.addParameterAndEncoded(Constants.REFER_KEY, StringUtils.toQueryString(map)));
                    }
                }
                if (urls.isEmpty()) {
                    throw new IllegalStateException("No such any registry to reference " + interfaceName + " on the consumer " + NetUtils.getLocalHost() + " use dubbo version " + Version.getVersion() + ", please config <dubbo:registry address=\"...\" /> to your spring config.");
                }
            }

            if (urls.size() == 1) {
                invoker = refprotocol.refer(interfaceClass, urls.get(0));
            }
        }
        // create service proxy
        return (T) proxyFactory.getProxy(invoker);
    }

    void checkStubAndLocal(Class<?> interfaceClass) {

    }

    void checkMock(Class<?> interfaceClass) {
        if (ConfigUtils.isEmpty(mock)) {
            return;
        }
    }

    /**
     * 解析器根据get.set方法去获取属性
     * @return
     */
    public String getInterface() {
        return interfaceName;
    }

    public void setInterface(String interfaceName) {
        this.interfaceName = interfaceName;
        if (id == null || id.length() == 0) {
            id = interfaceName;
        }
    }
}
