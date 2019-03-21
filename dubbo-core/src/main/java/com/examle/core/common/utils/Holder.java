package com.examle.core.common.utils;

/**
 * 帮助类持有值
 * @param <T>
 */
public class Holder<T> {
    private volatile T value;

    public void set(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }
}
