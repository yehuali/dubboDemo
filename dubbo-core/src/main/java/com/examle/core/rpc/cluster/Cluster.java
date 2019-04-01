package com.examle.core.rpc.cluster;

import com.examle.core.common.extension.Adaptive;
import com.examle.core.common.extension.SPI;
import com.examle.core.rpc.Invoker;
import com.examle.core.rpc.RpcException;
import com.examle.core.rpc.cluster.support.FailoverCluster;

/**
 * Cluster. (SPI, Singleton, ThreadSafe)
 */

@SPI(FailoverCluster.NAME)
public interface Cluster {

    /**
     *将目录调用程序合并到虚拟调用程序中
     * @param directory
     * @param <T>
     * @return
     * @throws RpcException
     */
    @Adaptive
    <T> Invoker<T> join(Directory<T> directory) throws RpcException;
}
