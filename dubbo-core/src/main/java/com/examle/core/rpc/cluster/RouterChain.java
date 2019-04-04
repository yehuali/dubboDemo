package com.examle.core.rpc.cluster;

import com.examle.core.common.URL;
import com.examle.core.rpc.Invoker;
import org.aopalliance.intercept.Invocation;

import java.util.Collections;
import java.util.List;

public class RouterChain<T> {

    //注册表中地址的完整列表，按方法名称分类
    private List<Invoker<T>> invokers = Collections.emptyList();

    //包含所有路由器，每次'route://' url更改时都要重新构造。
    private volatile List<Router> routers = Collections.emptyList();

    private RouterChain(URL url) {
    }

    public static <T> RouterChain<T> buildChain(URL url) {
        return new RouterChain<>(url);
    }

    public List<Invoker<T>> route(URL url, Invocation invocation) {
        List<Invoker<T>> finalInvokers = invokers;
        for (Router router : routers) {
            finalInvokers = router.route(finalInvokers, url, invocation);
        }
        return finalInvokers;
    }
}
