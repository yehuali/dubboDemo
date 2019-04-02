package com.examle.core.rpc.proxy.wrapper;

import com.examle.core.common.Constants;
import com.examle.core.common.URL;
import com.examle.core.rpc.Invoker;
import com.examle.core.rpc.Protocol;
import com.examle.core.rpc.ProxyFactory;
import com.examle.core.rpc.RpcException;
import com.examle.core.rpc.service.GenericService;

public class StubProxyFactoryWrapper implements ProxyFactory {

    private final ProxyFactory proxyFactory;

    private Protocol protocol;

    public StubProxyFactoryWrapper(ProxyFactory proxyFactory) {
        this.proxyFactory = proxyFactory;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    @Override
    public <T> Invoker<T> getInvoker(T proxy, Class<T> type, URL url) throws RpcException {
        return null;
    }

    @Override
    public <T> T getProxy(Invoker<T> invoker) throws RpcException {
        T proxy = proxyFactory.getProxy(invoker);
        if (GenericService.class != invoker.getInterface()) {
            String stub = invoker.getUrl().getParameter(Constants.STUB_KEY, invoker.getUrl().getParameter(Constants.LOCAL_KEY));
        }
        return proxy;
    }

    @Override
    public <T> T getProxy(Invoker<T> invoker, boolean generic) throws RpcException {
        return null;
    }
}
