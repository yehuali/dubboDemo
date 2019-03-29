package com.examle.core.registry;

import com.examle.core.common.URL;
import com.examle.core.common.extension.Adaptive;
import com.examle.core.common.extension.SPI;

@SPI("dubbo")
public interface RegistryFactory {
    @Adaptive({"protocol"})
    Registry getRegistry(URL url);
}
