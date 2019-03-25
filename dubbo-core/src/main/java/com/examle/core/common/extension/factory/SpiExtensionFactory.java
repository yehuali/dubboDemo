package com.examle.core.common.extension.factory;

import com.examle.core.common.extension.ExtensionFactory;
import com.examle.core.common.extension.ExtensionLoader;
import com.examle.core.common.extension.SPI;

public class SpiExtensionFactory implements ExtensionFactory {
    @Override
    public <T> T getExtension(Class<T> type, String name) {
        if (type.isInterface() && type.isAnnotationPresent(SPI.class)) {
            ExtensionLoader<T> loader = ExtensionLoader.getExtensionLoader(type);
            if (!loader.getSupportedExtensions().isEmpty()) {
                return loader.getAdaptiveExtension();
            }
        }
        return null;
    }
}
