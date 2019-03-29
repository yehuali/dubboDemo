package com.examle.core.rpc.protocol;

import com.examle.core.common.URL;
import com.examle.core.rpc.Exporter;
import com.examle.core.rpc.Invoker;
import com.examle.core.rpc.Protocol;
import com.examle.core.rpc.RpcException;

public class ProtocolFilterWrapper implements Protocol {

    @Override
    public <T> Exporter<T> export(Invoker<T> invoker) throws RpcException {
        return null;
    }

    @Override
    public <T> Invoker<T> refer(Class<T> type, URL url) throws RpcException {
        return null;
    }
}
