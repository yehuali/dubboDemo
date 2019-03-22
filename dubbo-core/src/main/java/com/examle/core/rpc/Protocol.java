package com.examle.core.rpc;

import com.examle.core.common.extension.Adaptive;
import com.examle.core.common.extension.SPI;

@SPI("dubbo")
public interface Protocol {
    @Adaptive
    <T> Exporter<T> export(Invoker<T> invoker) throws RpcException;
}
