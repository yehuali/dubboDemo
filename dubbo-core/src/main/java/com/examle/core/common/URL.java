package com.examle.core.common;

import com.examle.core.common.utils.NetUtils;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class URL implements Serializable {
    private final String protocol;

    private final String username;

    private final String password;

    // by default, host to registry
    private final String host;

    // by default, port to registry
    private final int port;

    private final String path;

    private final Map<String, String> parameters;

    // ==== cache ====

    private volatile transient Map<String, Number> numbers;

    private volatile transient Map<String, URL> urls;

    private volatile transient String ip;

    private volatile transient String full;

    private volatile transient String identity;

    private volatile transient String parameter;

    private volatile transient String string;


    protected URL() {
        this.protocol = null;
        this.username = null;
        this.password = null;
        this.host = null;
        this.port = 0;
        this.path = null;
        this.parameters = null;
    }

    public URL(String protocol, String host, int port, Map<String, String> parameters) {
        this(protocol, null, null, host, port, null, parameters);
    }

    public URL(String protocol, String username, String password, String host, int port, String path, Map<String, String> parameters) {
        if ((username == null || username.length() == 0)
                && password != null && password.length() > 0) {
            throw new IllegalArgumentException("Invalid url, password without username!");
        }
        this.protocol = protocol;
        this.username = username;
        this.password = password;
        this.host = host;
        this.port = (port < 0 ? 0 : port);
        // trim the beginning "/"
        while (path != null && path.startsWith("/")) {
            path = path.substring(1);
        }
        this.path = path;
        if (parameters == null) {
            parameters = new HashMap<String, String>();
        } else {
            parameters = new HashMap<String, String>(parameters);
        }
        this.parameters = Collections.unmodifiableMap(parameters);
    }

    public URL(String protocol, String host, int port, String path, Map<String, String> parameters) {
        this(protocol, null, null, host, port, path, parameters);
    }


    public URL addParameter(String key, String value) {
        if (key == null || key.length() == 0
                || value == null || value.length() == 0) {
            return this;
        }
        // if value doesn't change, return immediately
        if (value.equals(getParameters().get(key))) { // value != null
            return this;
        }

        Map<String, String> map = new HashMap<String, String>(getParameters());
        map.put(key, value);
        return new URL(protocol, username, password, host, port, path, map);
    }

    public URL removeParameters(String... keys) {
        if (keys == null || keys.length == 0) {
            return this;
        }
        Map<String, String> map = new HashMap<String, String>(getParameters());
        for (String key : keys) {
            map.remove(key);
        }
        if (map.size() == getParameters().size()) {
            return this;
        }
        return new URL(protocol, username, password, host, port, path, map);
    }

    public String toServiceStringWithoutResolving() {
        return buildString(true, false, false, true);
    }

    private String buildString(boolean appendUser, boolean appendParameter, boolean useIP, boolean useService, String... parameters) {
        StringBuilder buf = new StringBuilder();
        if (protocol != null && protocol.length() > 0) {
            buf.append(protocol);
            buf.append("://");
        }
        if (appendUser && username != null && username.length() > 0) {
            buf.append(username);
            if (password != null && password.length() > 0) {
                buf.append(":");
                buf.append(password);
            }
            buf.append("@");
        }
        String host;
        if (useIP) {
            host = getIp();
        } else {
            host = getHost();
        }
        if (host != null && host.length() > 0) {
            buf.append(host);
            if (port > 0) {
                buf.append(":");
                buf.append(port);
            }
        }
        String path;
        if (useService) {
            path = getServiceKey();
        } else {
            path = getPath();
        }
        if (path != null && path.length() > 0) {
            buf.append("/");
            buf.append(path);
        }

        if (appendParameter) {
            buildParameters(buf, true, parameters);
        }
        return buf.toString();
    }

    public String getHost() {
        return host;
    }

    private void buildParameters(StringBuilder buf, boolean concat, String[] parameters) {
        if (getParameters() != null && getParameters().size() > 0) {
            List<String> includes = (parameters == null || parameters.length == 0 ? null : Arrays.asList(parameters));
            boolean first = true;
            for (Map.Entry<String, String> entry : new TreeMap<String, String>(getParameters()).entrySet()) {
                if (entry.getKey() != null && entry.getKey().length() > 0
                        && (includes == null || includes.contains(entry.getKey()))) {
                    if (first) {
                        if (concat) {
                            buf.append("?");
                        }
                        first = false;
                    } else {
                        buf.append("&");
                    }
                    buf.append(entry.getKey());
                    buf.append("=");
                    buf.append(entry.getValue() == null ? "" : entry.getValue().trim());
                }
            }
        }
    }

    public String getServiceKey() {
        String inf = getServiceInterface();
        if (inf == null) {
            return null;
        }
        StringBuilder buf = new StringBuilder();
        String group = getParameter(Constants.GROUP_KEY);
        if (group != null && group.length() > 0) {
            buf.append(group).append("/");
        }
        buf.append(inf);
        String version = getParameter(Constants.VERSION_KEY);
        if (version != null && version.length() > 0) {
            buf.append(":").append(version);
        }
        return buf.toString();
    }

    public String getServiceInterface() {
        return getParameter(Constants.INTERFACE_KEY, path);
    }

    public String getParameter(String key, String defaultValue) {
        String value = getParameter(key);
        if (value == null || value.length() == 0) {
            return defaultValue;
        }
        return value;
    }

    public int getParameter(String key, int defaultValue) {
        Number n = getNumbers().get(key);
        if (n != null) {
            return n.intValue();
        }
        String value = getParameter(key);
        if (value == null || value.length() == 0) {
            return defaultValue;
        }
        int i = Integer.parseInt(value);
        getNumbers().put(key, i);
        return i;
    }

    private Map<String, Number> getNumbers() {
        if (numbers == null) { // concurrent initialization is tolerant
            numbers = new ConcurrentHashMap<String, Number>();
        }
        return numbers;
    }

    public String getBackupAddress() {
        return getBackupAddress(0);
    }

    public String getBackupAddress(int defaultPort) {
        StringBuilder address = new StringBuilder(appendDefaultPort(getAddress(), defaultPort));
        String[] backups = getParameter(Constants.BACKUP_KEY, new String[0]);
        if (backups != null && backups.length > 0) {
            for (String backup : backups) {
                address.append(",");
                address.append(appendDefaultPort(backup, defaultPort));
            }
        }
        return address.toString();
    }

    public String[] getParameter(String key, String[] defaultValue) {
        String value = getParameter(key);
        if (value == null || value.length() == 0) {
            return defaultValue;
        }
        return Constants.COMMA_SPLIT_PATTERN.split(value);
    }

    private String appendDefaultPort(String address, int defaultPort) {
        if (address != null && address.length() > 0
                && defaultPort > 0) {
            int i = address.indexOf(':');
            if (i < 0) {
                return address + ":" + defaultPort;
            } else if (Integer.parseInt(address.substring(i + 1)) == 0) {
                return address.substring(0, i + 1) + defaultPort;
            }
        }
        return address;
    }

    public boolean getParameter(String key, boolean defaultValue) {
        String value = getParameter(key);
        if (value == null || value.length() == 0) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value);
    }

    public String getParameter(String key) {
        String value = parameters.get(key);
        if (value == null || value.length() == 0) {
            value = parameters.get(Constants.DEFAULT_KEY_PREFIX + key);
        }
        return value;
    }

    public List<String> getParameter(String key, List<String> defaultValue) {
        String value = getParameter(key);
        if (value == null || value.length() == 0) {
            return defaultValue;
        }
        String[] strArray = Constants.COMMA_SPLIT_PATTERN.split(value);
        return Arrays.asList(strArray);
    }

    public String getIp() {
        if (ip == null) {
            ip = NetUtils.getIpByHost(host);
        }
        return ip;
    }


    public String getPath() {
        return path;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public URL setPath(String path) {
        return new URL(protocol, username, password, host, port, path, getParameters());
    }

    public static URL valueOf(String url) {
        if (url == null || (url = url.trim()).length() == 0) {
            throw new IllegalArgumentException("url == null");
        }
        String protocol = null;
        String username = null;
        String password = null;
        String host = null;
        int port = 0;
        String path = null;
        Map<String, String> parameters = null;
        int i = url.indexOf("?"); // seperator between body and parameters
        if (i >= 0) {
            String[] parts = url.substring(i + 1).split("\\&");
            parameters = new HashMap<String, String>();
            for (String part : parts) {
                part = part.trim();
                if (part.length() > 0) {
                    int j = part.indexOf('=');
                    if (j >= 0) {
                        parameters.put(part.substring(0, j), part.substring(j + 1));
                    } else {
                        parameters.put(part, part);
                    }
                }
            }
            url = url.substring(0, i);
        }
        i = url.indexOf("://");
        if (i >= 0) {
            if (i == 0) {
                throw new IllegalStateException("url missing protocol: \"" + url + "\"");
            }
            protocol = url.substring(0, i);
            url = url.substring(i + 3);
        }else{
            protocol = "";
            url = "";
        }

        i = url.indexOf("/");
        if (i >= 0) {
            path = url.substring(i + 1);
            url = url.substring(0, i);
        }

        i = url.lastIndexOf("@");

        i = url.lastIndexOf(":");
        if (i >= 0 && i < url.length() - 1) {
            port = Integer.parseInt(url.substring(i + 1));
            url = url.substring(0, i);
        }

        if (url.length() > 0) {
            host = url;
        }

        return new URL(protocol, username, password, host, port, path, parameters);
    }

    public String getProtocol() {
        return protocol;
    }

    public URL setProtocol(String protocol) {
        return new URL(protocol, username, password, host, port, path, getParameters());
    }

    public String getUsername() {
        return username;
    }

    public URL setUsername(String username) {
        return new URL(protocol, username, password, host, port, path, getParameters());
    }

    public String getPassword() {
        return password;
    }

    public URL setPassword(String password) {
        return new URL(protocol, username, password, host, port, path, getParameters());
    }
    public int getPort() {
        return port;
    }

    public URL setPort(int port) {
        return new URL(protocol, username, password, host, port, path, getParameters());
    }

    public String toFullString() {
        if (full != null) {
            return full;
        }
        return full = buildString(true, true);
    }
    private String buildString(boolean appendUser, boolean appendParameter, String... parameters) {
        return buildString(appendUser, appendParameter, false, false, parameters);
    }

    public URL setHost(String host) {
        return new URL(protocol, username, password, host, port, path, getParameters());
    }


    public URL addParameterAndEncoded(String key, String value) {
        if (value == null || value.length() == 0) {
            return this;
        }
        return addParameter(key, encode(value));
    }

    public static String encode(String value) {
        if (value == null || value.length() == 0) {
            return "";
        }
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public URL removeParameter(String key) {
        if (key == null || key.length() == 0) {
            return this;
        }
        return removeParameters(key);
    }

    public String getAddress() {
        return port <= 0 ? host : host + ":" + port;
    }

    public String getParameterAndDecoded(String key) {
        return getParameterAndDecoded(key, null);
    }

    public String getParameterAndDecoded(String key, String defaultValue) {
        return decode(getParameter(key, defaultValue));
    }
    public static String decode(String value) {
        if (value == null || value.length() == 0) {
            return "";
        }
        try {
            return URLDecoder.decode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public URL clearParameters() {
        return new URL(protocol, username, password, host, port, path, new HashMap<String, String>());
    }

    public URL addParameters(Map<String, String> parameters) {
        if (parameters == null || parameters.size() == 0) {
            return this;
        }

        boolean hasAndEqual = true;
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            String value = getParameters().get(entry.getKey());
            if (value == null) {
                if (entry.getValue() != null) {
                    hasAndEqual = false;
                    break;
                }
            } else {
                if (!value.equals(entry.getValue())) {
                    hasAndEqual = false;
                    break;
                }
            }
        }
        // return immediately if there's no change
        if (hasAndEqual) {
            return this;
        }

        Map<String, String> map = new HashMap<String, String>(getParameters());
        map.putAll(parameters);
        return new URL(protocol, username, password, host, port, path, map);
    }

    public URL addParameters(String... pairs) {
        if (pairs == null || pairs.length == 0) {
            return this;
        }
        if (pairs.length % 2 != 0) {
            throw new IllegalArgumentException("Map pairs can not be odd number.");
        }
        Map<String, String> map = new HashMap<String, String>();
        int len = pairs.length / 2;
        for (int i = 0; i < len; i++) {
            map.put(pairs[2 * i], pairs[2 * i + 1]);
        }
        return addParameters(map);
    }
}
