package com.examle.core.rpc.cluster.support;

import com.examle.core.rpc.cluster.Directory;

public class FailoverClusterInvoker<T> extends AbstractClusterInvoker<T>{
    public FailoverClusterInvoker(Directory<T> directory) {
        super(directory);
    }
}
