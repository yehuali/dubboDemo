package com.examle.core.rpc.cluster.support;

import com.examle.core.common.URL;
import com.examle.core.rpc.Invoker;
import com.examle.core.rpc.Result;
import com.examle.core.rpc.RpcException;
import com.examle.core.rpc.cluster.Directory;
import org.aopalliance.intercept.Invocation;

public abstract class AbstractClusterInvoker<T> implements Invoker<T> {

    protected final Directory<T> directory;

    public AbstractClusterInvoker(Directory<T> directory) {
        this(directory, directory.getUrl());
    }

    public AbstractClusterInvoker(Directory<T> directory, URL url) {
        if (directory == null) {
            throw new IllegalArgumentException("service directory == null");
        }

        this.directory = directory;
        //sticky: invoker.isAvailable() should always be checked before using when availablecheck is true.
//        this.availablecheck = url.getParameter(Constants.CLUSTER_AVAILABLE_CHECK_KEY, Constants.DEFAULT_CLUSTER_AVAILABLE_CHECK);
    }

    @Override
    public Result invoke(Invocation invocation) throws RpcException {
        return null;
    }

    @Override
    public Class<T> getInterface() {
        return directory.getInterface();
    }

    @Override
    public URL getUrl() {
        return directory.getUrl();
    }
}
