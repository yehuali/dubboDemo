package com.examle.core.rpc.proxy.javassist;

import com.examle.core.common.URL;
import com.examle.core.common.bytecode.Proxy;
import com.examle.core.common.bytecode.Wrapper;
import com.examle.core.rpc.Invoker;
import com.examle.core.rpc.RpcException;
import com.examle.core.rpc.proxy.AbstractProxyFactory;
import com.examle.core.rpc.proxy.AbstractProxyInvoker;
import com.examle.core.rpc.proxy.InvokerInvocationHandler;

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

    /**
     * 每个代理类都要关联一个InvocationHandler，并且InvocationHandler接口只有一个invoke方法
     *  -->当触发被代理类的任意一个接口的时候,实际调用的是InvokerInvocationHandler实现类的invoker方法
     *  public Object newInstance(java.lang.reflect.InvocationHandler h){
     * 	    return new com.examle.core.common.bytecode.proxy0($1);
     * }
     * @param invoker
     * @param interfaces
     * @return
     */
    @Override
    public Object getProxy(Invoker invoker, Class[] interfaces) {
        return  Proxy.getProxy(interfaces).newInstance(new InvokerInvocationHandler(invoker));
    }
}
