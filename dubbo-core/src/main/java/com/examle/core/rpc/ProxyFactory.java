package com.examle.core.rpc;

import com.examle.core.common.Constants;
import com.examle.core.common.URL;
import com.examle.core.common.extension.Adaptive;
import com.examle.core.common.extension.SPI;

@SPI("javassist")
public interface ProxyFactory {

    @Adaptive({Constants.PROXY_KEY})
    <T> Invoker<T> getInvoker(T proxy, Class<T> type, URL url) throws RpcException;
}
