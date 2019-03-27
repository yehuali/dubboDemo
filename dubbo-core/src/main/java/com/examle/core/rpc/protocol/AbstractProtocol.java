package com.examle.core.rpc.protocol;

import com.examle.core.rpc.Exporter;
import com.examle.core.rpc.Protocol;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractProtocol implements Protocol {
    protected final Map<String, Exporter<?>> exporterMap = new ConcurrentHashMap<String, Exporter<?>>();
}
