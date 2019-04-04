package com.examle.core.configcenter.support.zookeeper;

import com.examle.core.configcenter.ConfigurationListener;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class CacheListener implements TreeCacheListener {

    private Map<String, Set<ConfigurationListener>> keyListeners = new ConcurrentHashMap<>();

    @Override
    public void childEvent(CuratorFramework curatorFramework, TreeCacheEvent treeCacheEvent) throws Exception {

    }

    public void addListener(String key, ConfigurationListener configurationListener) {
        Set<ConfigurationListener> listeners = this.keyListeners.computeIfAbsent(key, k -> new CopyOnWriteArraySet<>());
        listeners.add(configurationListener);
    }
}
