package com.examle.core.rpc.support;

import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;

public class RpcUtils {
    public static boolean hasFutureReturnType(Method method) {
        return CompletableFuture.class.isAssignableFrom(method.getReturnType());
    }
}
