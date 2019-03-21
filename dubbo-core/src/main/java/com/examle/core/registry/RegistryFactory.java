package com.examle.core.registry;

import com.examle.core.common.URL;
import com.examle.core.common.extension.Adaptive;

public interface RegistryFactory {
    @Adaptive({"protocol"})
    Registry getRegistry(URL url);
}
