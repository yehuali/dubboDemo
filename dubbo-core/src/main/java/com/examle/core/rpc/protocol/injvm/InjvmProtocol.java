package com.examle.core.rpc.protocol.injvm;

import com.examle.core.common.Constants;
import com.examle.core.rpc.Exporter;
import com.examle.core.rpc.Invoker;
import com.examle.core.rpc.Protocol;
import com.examle.core.rpc.RpcException;
import com.examle.core.rpc.protocol.AbstractProtocol;


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
}
