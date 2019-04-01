package com.examle.core.registry.integration;

import com.examle.core.common.Constants;
import com.examle.core.common.URL;
import com.examle.core.common.utils.StringUtils;
import com.examle.core.rpc.cluster.directory.AbstractDirectory;

import java.util.Map;

public class RegistryDirectory<T> extends AbstractDirectory<T> {

    private final Class<T> serviceType;
    private final String serviceKey;
    private final Map<String, String> queryMap;

    private final URL directoryUrl;//在构造时初始化，断言不为空，并始终分配非空值
    private volatile URL overrideDirectoryUrl;//在构造时初始化，断言不为空，并始终分配非空值

    public RegistryDirectory(Class<T> serviceType, URL url) {
        super(url);
        if (serviceType == null) {
            throw new IllegalArgumentException("service type is null.");
        }
        if (url.getServiceKey() == null || url.getServiceKey().length() == 0) {
            throw new IllegalArgumentException("registry serviceKey is null.");
        }

        this.serviceType = serviceType;
        this.serviceKey = url.getServiceKey();
        this.queryMap = StringUtils.parseQueryString(url.getParameterAndDecoded(Constants.REFER_KEY));
        this.overrideDirectoryUrl = this.directoryUrl = turnRegistryUrlToConsumerUrl(url);
    }

    private URL turnRegistryUrlToConsumerUrl(URL url) {
        //在registry中保存对新url有用的任何参数
        String isDefault = url.getParameter(Constants.DEFAULT_KEY);
        if (StringUtils.isNotEmpty(isDefault)) {
            queryMap.put(Constants.REGISTRY_KEY + "." + Constants.DEFAULT_KEY, isDefault);
        }
        return url.setPath(url.getServiceInterface())
                .clearParameters()
                .addParameters(queryMap)
                .removeParameter(Constants.MONITOR_KEY);
    }

    @Override
    public URL getUrl() {
        return this.overrideDirectoryUrl;
    }

    @Override
    public Class<T> getInterface() {
        return serviceType;
    }
}
