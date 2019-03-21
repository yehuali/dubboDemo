package com.examle.core.common.bytecode;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Wrapper {
    private static final Map<Class<?>, Wrapper> WRAPPER_MAP = new ConcurrentHashMap<Class<?>, Wrapper>();

    public static Wrapper getWrapper(Class<?> c) {
        Wrapper ret = WRAPPER_MAP.get(c);
        return  ret;
    }

    abstract public String[] getMethodNames();
}
