package com.examle.core.rpc.cluster.router.mock;

import com.examle.core.common.URL;
import com.examle.core.rpc.Invoker;
import com.examle.core.rpc.RpcException;
import com.examle.core.rpc.cluster.Router;
import com.examle.core.rpc.cluster.router.AbstractRouter;
import org.aopalliance.intercept.Invocation;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class MockInvokersSelector extends AbstractRouter {
    @Override
    public <T> List<Invoker<T>> route(List<Invoker<T>> invokers, URL url, Invocation invocation) throws RpcException {
        if (CollectionUtils.isEmpty(invokers)) {
            return invokers;
        }
        return invokers;
    }

}
