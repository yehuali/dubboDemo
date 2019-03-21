package com.examle.core.config;

import com.examle.core.common.Constants;
import com.examle.core.common.utils.StringUtils;
import com.examle.core.config.support.Parameter;

import java.io.Serializable;

public abstract class AbstractConfig implements Serializable {

    private static final String[] SUFFIXES = new String[]{"Config", "Bean"};

    protected String id;
    protected String prefix;

    @Parameter(excluded = true)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Parameter(excluded = true)
    public String getPrefix() {
        return StringUtils.isNotEmpty(prefix) ? prefix : (Constants.DUBBO + "." + getTagName(this.getClass()));
    }

    private static String getTagName(Class<?> cls) {
        String tag = cls.getSimpleName();
        for (String suffix : SUFFIXES) {
            if (tag.endsWith(suffix)) {
                tag = tag.substring(0, tag.length() - suffix.length());
                break;
            }
        }
        return StringUtils.camelToSplitName(tag, "-");
    }

    public void refresh() {
        CompositeConfiguration compositeConfiguration = Environment.getInstance().getConfiguration(getPrefix(), getId());
    }
}
