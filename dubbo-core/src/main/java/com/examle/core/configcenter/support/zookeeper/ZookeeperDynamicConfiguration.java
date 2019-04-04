package com.examle.core.configcenter.support.zookeeper;

import com.examle.core.common.Constants;
import com.examle.core.common.URL;
import com.examle.core.common.utils.StringUtils;
import com.examle.core.configcenter.ConfigurationListener;
import com.examle.core.configcenter.DynamicConfiguration;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import static org.apache.curator.framework.CuratorFrameworkFactory.newClient;

public class ZookeeperDynamicConfiguration implements DynamicConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(ZookeeperDynamicConfiguration.class);

    private CuratorFramework client;

    //最后的根路径是/configRootPath/config
    private String rootPath;
    private TreeCache treeCache;

    private CacheListener cacheListener;

    ZookeeperDynamicConfiguration(URL url) {
        RetryPolicy policy = new ExponentialBackoffRetry(1000, 3);
        int sessionTimeout = 60000;
        int connectTimeout = 10000;
        String connectString = "127.0.0.1:2181";
        //建立与zookeeper连接
        client = newClient(connectString, sessionTimeout, connectTimeout, policy);
        client.start();
        try{
            //阻塞，直到与ZooKeeper的连接可用或超过maxWaitTime为止
            boolean connected = client.blockUntilConnected(3 * connectTimeout, TimeUnit.MILLISECONDS);
            if (!connected) {
                if (url.getParameter(Constants.CONFIG_CHECK_KEY, true)) {
                    throw new IllegalStateException("Failed to connect to config center (zookeeper): "
                            + connectString + " in " + 3 * connectTimeout + "ms.");
                } else {
                    logger.warn("The config center (zookeeper) is not fully initialized in " + 3 * connectTimeout + "ms, address is: " + connectString);
                }
            }
        } catch (InterruptedException e) {
            throw new IllegalStateException("The thread was interrupted unexpectedly when trying connecting to zookeeper "
                    + connectString + " config center, ", e);
        }

        //建立本地缓存

    }

    @Override
    public Object getInternalProperty(String key) {
        ChildData childData = treeCache.getCurrentData(key);
        if (childData != null) {
            return new String(childData.getData(), StandardCharsets.UTF_8);
        }
        return null;
    }

    //对于服务治理，此实现不支持多组。所以目前没有使用group。
    @Override
    public void addListener(String key, String group, ConfigurationListener listener) {
        cacheListener.addListener(key, listener);
    }


    @Override
    public String getConfig(String key, String group, long timeout) throws IllegalStateException {
        /**
         * 当group不为空时，我们从Config Center获取启动配置
         * 例如：group=dubbo, key=dubbo.properties
         */
        if (StringUtils.isNotEmpty(group)) {
            key = group + "/" + key;
        }
        /**
         * 当group为null时，我们获取治理规则
         * 1. key=org.apache.dubbo.DemoService.configurators
         * 2. key = org.apache.dubbo.DemoService.condition-router
         */
        else{
            int i = key.lastIndexOf(".");
            key = key.substring(0, i) + "/" + key.substring(i + 1);
        }
        return (String) getInternalProperty(rootPath + "/" + key);
    }
}
