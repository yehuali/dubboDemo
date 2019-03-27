package com.examle.core.config;

import com.examle.core.common.Constants;
import com.examle.core.common.URL;
import com.examle.core.common.bytecode.Wrapper;
import com.examle.core.common.extension.ExtensionLoader;
import com.examle.core.common.utils.StringUtils;
import com.examle.core.config.context.ConfigManager;
import com.examle.core.config.support.Parameter;
import com.examle.core.rpc.Exporter;
import com.examle.core.rpc.Protocol;
import com.examle.core.rpc.ProxyFactory;
import com.examle.core.rpc.model.ApplicationModel;
import com.examle.core.rpc.model.ProviderModel;

import java.util.*;

import static com.examle.core.common.utils.NetUtils.LOCALHOST;

public class ServiceConfig<T> extends AbstractServiceConfig {

    /**
     * Whether the provider has been exported
     */
    private transient volatile boolean exported;

    //标记服务是否已未导出，如果调用了未导出的方法，则该值为true
    private transient volatile boolean unexported;

    private ProviderConfig provider;

    /**
     * The service name
     */
    private String path;


    /**
     * The reference of the interface implementation
     */
    private T ref;//通过xml属性注入，根据get set方法

    /**
     * The interface class of the exported service
     */
    private Class<?> interfaceClass;//根据interfaceName反射加载

    /**
     * The interface name of the exported service
     */
    private String interfaceName;//通过xml属性注入，根据get set方法

    private static final Protocol protocol = ExtensionLoader.getExtensionLoader(Protocol.class).getAdaptiveExtension();

    private static final ProxyFactory proxyFactory = ExtensionLoader.getExtensionLoader(ProxyFactory.class).getAdaptiveExtension();


    @Parameter(excluded = true)
    public boolean isExported() {
        return exported;
    }

    @Parameter(excluded = true)
    public boolean isUnexported() {
        return unexported;
    }

    public synchronized void export() {
        checkAndUpdateSubConfigs();
        if (provider != null) {
            if (export == null) {
                export = provider.getExport();
            }
        }
        if (export != null && !export) {
            return;
        }
        if(false){//延时发布

        }else{
            doExport();
        }

    }

    protected synchronized void doExport() {
        if (unexported) {
            throw new IllegalStateException("Already unexported!");
        }
        if (exported) {
            return;
        }
        exported = true;
        if (path == null || path.length() == 0) {
            path = interfaceName;
        }
        ProviderModel providerModel = new ProviderModel(getUniqueServiceName(), ref, interfaceClass);
        ApplicationModel.initProviderModel(getUniqueServiceName(), providerModel);
        doExportUrls();
    }

    private void doExportUrls() {
        List<URL> registryURLs = loadRegistries(true);
        for (ProtocolConfig protocolConfig : protocols) {
            doExportUrlsFor1Protocol(protocolConfig, registryURLs);
        }
    }

    private void doExportUrlsFor1Protocol(ProtocolConfig protocolConfig, List<URL> registryURLs) {
        String name = protocolConfig.getName();
        if (name == null || name.length() == 0) {
            name = Constants.DUBBO;
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put(Constants.SIDE_KEY, Constants.PROVIDER_SIDE);
        appendRuntimeParameters(map);
        appendParameters(map, application);
        appendParameters(map, module);
        appendParameters(map, provider, Constants.DEFAULT_KEY);
        appendParameters(map, protocolConfig);
        appendParameters(map, this);
        if(false){

        }else{
            String[] methods = Wrapper.getWrapper(interfaceClass).getMethodNames();
            if(methods.length == 0){
            }else{
                map.put(Constants.METHODS_KEY, StringUtils.join(new HashSet<String>(Arrays.asList(methods)), ","));
            }
        }

        String host = "192.168.2.206";
        Integer port = 20880;
        URL url = new URL(name, host, port, path, map);

        String scope = null;
        //如果配置不是远程的，导出到本地(只有在配置是远程的时候才导出到远程)
        exportLocal(url);
    }

    @Parameter(excluded = true)
    public String getUniqueServiceName() {
        StringBuilder buf = new StringBuilder();
        if (group != null && group.length() > 0) {
            buf.append(group).append("/");
        }
        buf.append(StringUtils.isNotEmpty(path) ? path : interfaceName);
        if (version != null && version.length() > 0) {
            buf.append(":").append(version);
        }
        return buf.toString();
    }

    public void checkAndUpdateSubConfigs() {
        //启动注册中心
        startConfigCenter();
        checkDefault();
        if(false){

        }else{
            try {
                interfaceClass = Class.forName(interfaceName, true, Thread.currentThread()
                        .getContextClassLoader());
            }catch (ClassNotFoundException e){
                throw new IllegalStateException(e.getMessage(), e);
            }
        }
    }

    private void checkDefault() {
        createProviderIfAbsent();
    }

    private void createProviderIfAbsent() {
        if (provider != null) {
            return;
        }
        setProvider (
                ConfigManager.getInstance()
                        .getDefaultProvider()
                        .orElseGet(() -> {
                            ProviderConfig providerConfig = new ProviderConfig();
                            providerConfig.refresh();
                            return providerConfig;
                        })
        );
    }

    public ProviderConfig getProvider() {
        return provider;
    }

    public void setProvider(ProviderConfig provider) {
        ConfigManager.getInstance().addProvider(provider);
        this.provider = provider;
    }

    private void exportLocal(URL url) {
        if (!Constants.LOCAL_PROTOCOL.equalsIgnoreCase(url.getProtocol())) {
//            Exporter<?> exporter = protocol.export(
//                    proxyFactory.getInvoker(ref, (Class) interfaceClass, local));
            /**
             * 将协议改为injvm，IP:127.0.0.1,端口为0
             */
            URL local = URL.valueOf(url.toFullString())
                    .setProtocol(Constants.LOCAL_PROTOCOL)
                    .setHost(LOCALHOST)
                    .setPort(0);
            Exporter<?> exporter = protocol.export(
                    proxyFactory.getInvoker(ref, (Class) interfaceClass, local));
        }
    }

    public T getRef() {
        return ref;
    }

    public void setRef(T ref) {
        this.ref = ref;
    }

    public Class<?> getInterfaceClass() {
        if (interfaceClass != null) {
            return interfaceClass;
        }
//        if (ref instanceof GenericService) {
//            return GenericService.class;
//        }
        try {
            if (interfaceName != null && interfaceName.length() > 0) {
                this.interfaceClass = Class.forName(interfaceName, true, Thread.currentThread()
                        .getContextClassLoader());
            }
        } catch (ClassNotFoundException t) {
            throw new IllegalStateException(t.getMessage(), t);
        }
        return interfaceClass;
    }

    public void setInterfaceClass(Class<?> interfaceClass) {
        setInterface(interfaceClass);
    }

    public String getInterface() {
        return interfaceName;
    }

    public void setInterface(Class<?> interfaceClass) {
        if (interfaceClass != null && !interfaceClass.isInterface()) {
            throw new IllegalStateException("The interface class " + interfaceClass + " is not a interface!");
        }
        this.interfaceClass = interfaceClass;
        setInterface(interfaceClass == null ? null : interfaceClass.getName());
    }

    public void setInterface(String interfaceName) {
        this.interfaceName = interfaceName;
        if (id == null || id.length() == 0) {
            id = interfaceName;
        }
    }
}
