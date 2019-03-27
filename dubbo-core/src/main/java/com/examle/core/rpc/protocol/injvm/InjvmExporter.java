package com.examle.core.rpc.protocol.injvm;

import com.examle.core.rpc.Exporter;
import com.examle.core.rpc.Invoker;
import com.examle.core.rpc.protocol.AbstractExporter;

import java.util.Map;

public class InjvmExporter<T> extends AbstractExporter<T> {
    private final String key;

    private final Map<String, Exporter<?>> exporterMap;

    InjvmExporter(Invoker<T> invoker, String key, Map<String, Exporter<?>> exporterMap) {
        super(invoker);
        this.key = key;
        this.exporterMap = exporterMap;
        exporterMap.put(key, this);
    }
}
