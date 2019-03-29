package com.examle.core.rpc.protocol.injvm;

import com.examle.core.common.Constants;
import com.examle.core.common.URL;
import com.examle.core.common.extension.ExtensionLoader;
import com.examle.core.rpc.Exporter;
import com.examle.core.rpc.Invoker;
import com.examle.core.rpc.Protocol;
import com.examle.core.rpc.RpcException;
import com.examle.core.rpc.protocol.AbstractProtocol;

import java.util.Map;


public class InjvmProtocol extends AbstractProtocol implements Protocol {
    public static final String NAME = Constants.LOCAL_PROTOCOL;

    private static InjvmProtocol INSTANCE;

    public InjvmProtocol() {
        INSTANCE = this;
    }

    @Override
    public <T> Exporter<T> export(Invoker<T> invoker) throws RpcException {
        return new InjvmExporter<T>(invoker, invoker.getUrl().getServiceKey(), exporterMap);
    }

    @Override
    public <T> Invoker<T> refer(Class<T> type, URL url) throws RpcException {
        return null;
    }

    public static InjvmProtocol getInjvmProtocol() {
        if (INSTANCE == null) {
            ExtensionLoader.getExtensionLoader(Protocol.class).getExtension(InjvmProtocol.NAME); // load
        }
        return INSTANCE;
    }

    public boolean isInjvmRefer(URL url) {
        final boolean isJvmRefer;
        String scope = url.getParameter(Constants.SCOPE_KEY);
        //由于injvm协议是显式配置的，我们不需要设置任何额外的标志，使用普通的reference process
        if (Constants.LOCAL_PROTOCOL.toString().equals(url.getProtocol())) {
            isJvmRefer = false;
        }else if (Constants.SCOPE_LOCAL.equals(scope) || (url.getParameter(Constants.LOCAL_PROTOCOL, false))) {
            /**
             * 如果它被声明为本地引用
             * “scope=local”相当于“injvm=true”，在将来的版本中将不推荐使用injvm
             */

            isJvmRefer = true;
        }else if (Constants.SCOPE_REMOTE.equals(scope)) {
            isJvmRefer = false;
        }else if (url.getParameter(Constants.GENERIC_KEY, false)) {
            //泛型调用不是本地引用
            isJvmRefer = false;
        }else if (getExporter(exporterMap, url) != null) {
            //默认情况下，如果有本地公开的服务，则通过本地引用
            isJvmRefer = true;
        }else {
            isJvmRefer = false;
        }
        return isJvmRefer;
    }

    static Exporter<?> getExporter(Map<String, Exporter<?>> map, URL key) {
        Exporter<?> result = null;
        if (!key.getServiceKey().contains("*")) {
            result = map.get(key.getServiceKey());
        }else{
            result = null;//待写
        }
        if (result == null) {
            return null;
        }
        return null;//后续
    }

}
