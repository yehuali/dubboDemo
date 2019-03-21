package com.examle.core.config;

import com.examle.core.common.utils.StringUtils;

/**
 * 这是为Dubbo检索属性的序列特别定制的抽象
 */
public abstract class AbstractPrefixConfiguration implements Configuration {

    protected String id;
    protected String prefix;

    public AbstractPrefixConfiguration(String prefix, String id) {
        super();
        if (StringUtils.isNotEmpty(prefix) && !prefix.endsWith(".")) {
            this.prefix = prefix + ".";
        } else {
            this.prefix = prefix;
        }
        this.id = id;
    }
}
