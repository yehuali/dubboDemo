package com.examle.core.rpc.proxy.javassist;

import com.examle.core.common.URL;
import com.examle.core.common.bytecode.Wrapper;
import com.examle.core.rpc.Invoker;
import com.examle.core.rpc.RpcException;
import com.examle.core.rpc.proxy.AbstractProxyFactory;
import com.examle.core.rpc.proxy.AbstractProxyInvoker;

/**
 * Invoker是实体域，Dubbo的核心模型，代表一个可执行体，可向它发起invoke调用（可能是本地实现，可能是远程实现）
 */
public class JavassistProxyFactory extends AbstractProxyFactory {
    @Override
    public <T> Invoker<T> getInvoker(T proxy, Class<T> type, URL url) throws RpcException {
        final Wrapper wrapper = Wrapper.getWrapper(proxy.getClass().getName().indexOf('$') < 0 ? proxy.getClass() : type);
        return new AbstractProxyInvoker<T>(proxy, type, url) {
            @Override
            protected Object doInvoke(T proxy, String methodName,
                                      Class<?>[] parameterTypes,
                                      Object[] arguments) throws Throwable {
                return wrapper.invokeMethod(proxy, methodName, parameterTypes, arguments);
            }
        };
    }
}
