package com.examle.core.registry;

import com.examle.core.common.URL;

public interface RegistryService {
    /**
     * 注册数据，如:提供者服务、使用者地址、路由规则、覆盖规则等数据。
     * @param url
     */
    void register(URL url);
}
