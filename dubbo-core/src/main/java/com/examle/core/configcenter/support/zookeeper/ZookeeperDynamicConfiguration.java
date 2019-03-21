package com.examle.core.configcenter.support.zookeeper;

import com.examle.core.common.Constants;
import com.examle.core.common.URL;
import com.examle.core.configcenter.DynamicConfiguration;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static org.apache.curator.framework.CuratorFrameworkFactory.newClient;

public class ZookeeperDynamicConfiguration implements DynamicConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(ZookeeperDynamicConfiguration.class);

    private CuratorFramework client;

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
}
