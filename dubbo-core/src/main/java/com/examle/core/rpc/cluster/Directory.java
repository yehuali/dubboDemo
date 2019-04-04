package com.examle.core.rpc.cluster;

import com.examle.core.common.Node;
import com.examle.core.rpc.Invoker;
import com.examle.core.rpc.RpcException;
import org.aopalliance.intercept.Invocation;

import java.util.List;

public interface Directory<T> extends Node {
    Class<T> getInterface();

    List<Invoker<T>> list(Invocation invocation) throws RpcException;
}
