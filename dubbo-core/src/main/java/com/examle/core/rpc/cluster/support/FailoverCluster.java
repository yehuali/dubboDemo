package com.examle.core.rpc.cluster.support;

import com.examle.core.rpc.Invoker;
import com.examle.core.rpc.RpcException;
import com.examle.core.rpc.cluster.Cluster;
import com.examle.core.rpc.cluster.Directory;

public class FailoverCluster implements Cluster {
    public final static String NAME = "failover";


    @Override
    public <T> Invoker<T> join(Directory<T> directory) throws RpcException {
        return new FailoverClusterInvoker<T>(directory);
    }
}
