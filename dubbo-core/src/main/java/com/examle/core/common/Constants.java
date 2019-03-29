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

    public static final String SIDE_KEY = "side";

    public static final String PROVIDER_SIDE = "provider";

    public static final String SCOPE_KEY = "scope";

    public static final String SCOPE_REMOTE = "remote";

    public static final String SCOPE_LOCAL = "local";

    public static final String SCOPE_NONE = "none";

    public static final String CONSUMER_SIDE = "consumer";

    public static final String GENERIC_SERIALIZATION_NATIVE_JAVA = "nativejava";

    public static final String GENERIC_SERIALIZATION_DEFAULT = "true";

    public static final String GENERIC_SERIALIZATION_BEAN = "bean";

    public static final String DUBBO_PROPERTIES_KEY = "dubbo.properties.file";

    public static final String DEFAULT_DUBBO_PROPERTIES = "dubbo.properties";

    public static final String DUBBO_IP_TO_REGISTRY = "DUBBO_IP_TO_REGISTRY";

    public static final String REGISTER_IP_KEY = "register.ip";

    public static final String GENERIC_KEY = "generic";

    public static final String DEFAULT_REGISTRY = "dubbo";

    public static final String CLIENT_KEY = "client";

    public static final String TRANSPORTER_KEY = "transporter";
}
