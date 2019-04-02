package com.examle.core.rpc.cluster;

import com.examle.core.common.URL;
import com.examle.core.common.extension.ExtensionLoader;

import java.util.stream.Collectors;

public class RouterChain<T> {

    private RouterChain(URL url) {
    }

    public static <T> RouterChain<T> buildChain(URL url) {
        return new RouterChain<>(url);
    }
}
