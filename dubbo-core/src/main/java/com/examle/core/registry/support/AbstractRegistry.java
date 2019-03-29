package com.examle.core.registry.support;

import com.examle.core.common.URL;
import com.examle.core.registry.Registry;

public abstract class AbstractRegistry implements Registry {
    private URL registryUrl;
    @Override
    public URL getUrl() {
        return registryUrl;
    }
}
