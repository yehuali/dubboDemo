package com.examle.core.config;

import com.examle.core.common.Constants;
import com.examle.core.common.bytecode.Wrapper;
import com.examle.core.common.utils.ConfigUtils;
import com.examle.core.common.utils.StringUtils;
import com.examle.core.config.context.ConfigManager;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class ReferenceConfig<T> extends AbstractReferenceConfig {
    private String interfaceName;

    private Class<?> interfaceClass;

    protected String mock;

    private ConsumerConfig consumer;

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
    }

    public void checkAndUpdateSubConfigs() {
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
