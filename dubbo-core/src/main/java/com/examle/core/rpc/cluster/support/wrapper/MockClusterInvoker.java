package com.examle.core.rpc.cluster.support.wrapper;

import com.examle.core.common.URL;
import com.examle.core.rpc.Invoker;
import com.examle.core.rpc.Result;
import com.examle.core.rpc.RpcException;
import com.examle.core.rpc.cluster.Directory;
import org.aopalliance.intercept.Invocation;

public class MockClusterInvoker<T> implements Invoker<T> {

    private final Directory<T> directory;

    private final Invoker<T> invoker;

    public MockClusterInvoker(Directory<T> directory, Invoker<T> invoker) {
        this.directory = directory;
        this.invoker = invoker;
    }
    @Override
    public Result invoke(Invocation invocation) throws RpcException {
        Result result = this.invoker.invoke(invocation);
        return result;
    }

    @Override
    public Class<T> getInterface() {
       return  directory.getInterface();
    }

    @Override
    public URL getUrl() {
        return directory.getUrl();
    }
}
