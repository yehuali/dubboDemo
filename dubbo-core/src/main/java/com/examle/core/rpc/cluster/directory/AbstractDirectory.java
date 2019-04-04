package com.examle.core.rpc.cluster.directory;

import com.examle.core.common.URL;
import com.examle.core.rpc.Invoker;
import com.examle.core.rpc.RpcException;
import com.examle.core.rpc.cluster.Directory;
import com.examle.core.rpc.cluster.RouterChain;
import org.aopalliance.intercept.Invocation;

import java.util.List;

public abstract  class AbstractDirectory<T> implements Directory<T> {

    private final URL url;

    private volatile URL consumerUrl;

    private volatile boolean destroyed = false;

    public AbstractDirectory(URL url) {
        this(url, null);
    }

    public AbstractDirectory(URL url, RouterChain<T> routerChain) {
        this(url, url, routerChain);
    }
    public AbstractDirectory(URL url, URL consumerUrl, RouterChain<T> routerChain) {
        if (url == null) {
            throw new IllegalArgumentException("url == null");
        }
        this.url = url;
    }

    @Override
    public List<Invoker<T>> list(Invocation invocation) throws RpcException {
        if (destroyed) {
            throw new RpcException("Directory already destroyed .url: " + getUrl());
        }
        return doList(invocation);
    }

    protected abstract List<Invoker<T>> doList(Invocation invocation) throws RpcException;

    public URL getConsumerUrl() {
        return consumerUrl;
    }

    public void setConsumerUrl(URL consumerUrl) {
        this.consumerUrl = consumerUrl;
    }

    @Override
    public URL getUrl() {
        return url;
    }
}
