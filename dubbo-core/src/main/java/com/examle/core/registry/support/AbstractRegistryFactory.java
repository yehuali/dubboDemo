package com.examle.core.registry.support;

import com.examle.core.common.Constants;
import com.examle.core.common.URL;
import com.examle.core.registry.Registry;
import com.examle.core.registry.RegistryFactory;
import com.examle.core.registry.RegistryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public abstract  class AbstractRegistryFactory implements RegistryFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRegistryFactory.class);

    private static final ReentrantLock LOCK = new ReentrantLock();

    private static final Map<String, Registry> REGISTRIES = new HashMap<>();

    public static Collection<Registry> getRegistries() {
        return Collections.unmodifiableCollection(REGISTRIES.values());
    }

    @Override
    public Registry getRegistry(URL url) {
        url = url.setPath(RegistryService.class.getName())
                .addParameter(Constants.INTERFACE_KEY, RegistryService.class.getName())
                .removeParameters(Constants.EXPORT_KEY, Constants.REFER_KEY);

        String key = url.toServiceStringWithoutResolving();
        //锁定注册表访问过程，以确保注册表的单个实例
        LOCK.lock();
        try {
            Registry registry = REGISTRIES.get(key);
            if (registry != null) {
                return registry;
            }
            registry = createRegistry(url);
            if (registry == null) {
                throw new IllegalStateException("Can not create registry " + url);
            }
            REGISTRIES.put(key, registry);
            return registry;
        }finally {
            LOCK.unlock();
        }
    }

    protected abstract Registry createRegistry(URL url);

    public static void destroyAll() {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Close all registries " + getRegistries());
        }
        // Lock up the registry shutdown process
        LOCK.lock();
        try {
            for (Registry registry : getRegistries()) {
                try {
//                    registry.destroy();
                } catch (Throwable e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
            REGISTRIES.clear();
        } finally {
            // Release the lock
            LOCK.unlock();
        }
    }
}
