package com.examle.core.rpc.protocol;

import com.examle.core.rpc.Exporter;
import com.examle.core.rpc.Invoker;

public abstract class AbstractExporter<T> implements Exporter<T> {
    private final Invoker<T> invoker;

    public AbstractExporter(Invoker<T> invoker) {
        if (invoker == null) {
            throw new IllegalStateException("service invoker == null");
        }
        if (invoker.getInterface() == null) {
            throw new IllegalStateException("service type == null");
        }
        if (invoker.getUrl() == null) {
            throw new IllegalStateException("service url == null");
        }
        this.invoker = invoker;
    }
}
