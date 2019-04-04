package com.examle.core.rpc.cluster;

import com.examle.core.common.URL;
import com.examle.core.rpc.Invoker;
import com.examle.core.rpc.RpcException;
import org.aopalliance.intercept.Invocation;

import java.util.List;

public interface Router extends Comparable<Router> {

    /**
     * 使用当前路由规则筛选调用程序，只返回符合规则的调用程序
     */
    <T> List<Invoker<T>> route(List<Invoker<T>> invokers, URL url, Invocation invocation) throws RpcException;

    @Override
    default int compareTo(Router o) {
       return 0;//待完成
    }
}
