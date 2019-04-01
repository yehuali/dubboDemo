package com.examle.core.rpc.proxy;

import com.examle.core.common.Constants;
import com.examle.core.rpc.Invoker;
import com.examle.core.rpc.ProxyFactory;
import com.examle.core.rpc.RpcException;
import com.examle.core.rpc.service.EchoService;

public abstract class AbstractProxyFactory<T> implements ProxyFactory {
    @Override
    public <T> T getProxy(Invoker<T> invoker) throws RpcException {
        return getProxy(invoker, false);
    }

    @Override
    public <T> T getProxy(Invoker<T> invoker, boolean generic) throws RpcException {
        Class<?>[] interfaces = null;
        String config = invoker.getUrl().getParameter(Constants.INTERFACES);
        if (config != null && config.length() > 0) {
            String[] types = Constants.COMMA_SPLIT_PATTERN.split(config);

        }
        if (interfaces == null) {
            interfaces = new Class<?>[]{invoker.getInterface(), EchoService.class};
        }
        return getProxy(invoker, interfaces);
    }

    public abstract <T> T getProxy(Invoker<T> invoker, Class<?>[] types);
}
