package com.examle.core.rpc.cluster.directory;

import com.examle.core.common.URL;
import com.examle.core.rpc.cluster.Directory;
import com.examle.core.rpc.cluster.RouterChain;

public abstract  class AbstractDirectory<T> implements Directory<T> {

    private final URL url;
    public AbstractDirectory(URL url) {
        this(url, null);
    }

    public AbstractDirectory(URL url, RouterChain<T> routerChain) {
        this(url, url, routerChain);
    }
    public AbstractDirectory(URL url, URL consumerUrl, RouterChain<T> routerChain) {
        if (url == null) {
            throw new IllegalArgumentException("url == null");
        }
        this.url = url;
    }

    @Override
    public URL getUrl() {
        return url;
    }
}
