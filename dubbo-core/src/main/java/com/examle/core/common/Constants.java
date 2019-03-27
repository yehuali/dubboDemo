package com.examle.core.common;

import java.util.regex.Pattern;

public class Constants {

    public static final String DUBBO = "dubbo";

    public static final String INTERFACE_KEY = "interface";

    public static final String EXPORT_KEY = "export";

    public static final String REFER_KEY = "refer";

    public static final String GROUP_KEY = "group";

    public static final String VERSION_KEY = "version";

    public static final String DEFAULT_KEY_PREFIX = "default.";

    public static final String DEFAULT_KEY = "default";

    public static final String METHODS_KEY = "methods";

    public static final String LOCAL_PROTOCOL = "injvm";

    public static final String CONFIG_CHECK_KEY = "config.check";

    public static final String PROXY_KEY = "proxy";

    public static final String ANYHOST_VALUE = "0.0.0.0";

    public static final String DUBBO_VERSION_KEY = "dubbo";

    public static final String RELEASE_KEY = "release";

    public static final String TIMESTAMP_KEY = "timestamp";

    public static final String PID_KEY = "pid";

    public static final Pattern REGISTRY_SPLIT_PATTERN = Pattern
            .compile("\\s*[|;]+\\s*");

    public static final String REGISTRY_KEY = "registry";

    public static final String REGISTRY_PROTOCOL = "registry";

    public static final String REGISTER_KEY = "register";

    public static final String SUBSCRIBE_KEY = "subscribe";

}
