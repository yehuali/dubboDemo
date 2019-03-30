package com.examle.core.registry.support;

import com.examle.core.common.URL;


public abstract class FailbackRegistry extends AbstractRegistry {

    public FailbackRegistry(URL url) {
        super(url);
    }
}
