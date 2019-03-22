package com.examle.core.rpc;

import com.examle.core.common.Node;
import org.aopalliance.intercept.Invocation;

public interface Invoker<T> extends Node {
    Result invoke(Invocation invocation) throws RpcException;
}
