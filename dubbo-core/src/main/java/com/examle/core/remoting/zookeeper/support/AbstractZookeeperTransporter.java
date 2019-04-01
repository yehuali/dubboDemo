package com.examle.core.remoting.zookeeper.support;

import com.examle.core.common.Constants;
import com.examle.core.common.URL;
import com.examle.core.registry.zookeeper.ZookeeperClient;
import com.examle.core.registry.zookeeper.ZookeeperTransporter;
import com.examle.core.remoting.zookeeper.StateListener;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public abstract class AbstractZookeeperTransporter implements ZookeeperTransporter {

    private static final Logger logger = LoggerFactory.getLogger(ZookeeperTransporter.class);
    private final Map<String, ZookeeperClient> zookeeperClientMap = new ConcurrentHashMap<>();

    @Override
    public ZookeeperClient connect(URL url) {
        ZookeeperClient zookeeperClient;
        List<String> addressList = getURLBackupAddress(url);
        //字段定义zookeeper服务器，包括协议、主机、端口、用户名和密码
        if ((zookeeperClient = fetchAndUpdateZookeeperClientCache(addressList)) != null && zookeeperClient.isConnected()) {
            logger.info("find valid zookeeper client from the cache for address: " + url);
            return zookeeperClient;
        }
        //避免创建太多的连接,所以加锁
        synchronized (zookeeperClientMap) {
            if ((zookeeperClient = fetchAndUpdateZookeeperClientCache(addressList)) != null && zookeeperClient.isConnected()) {
                logger.info("find valid zookeeper client from the cache for address: " + url);
                return zookeeperClient;
            }
            zookeeperClient = createZookeeperClient(toClientURL(url));
            logger.info("No valid zookeeper client found from cache, therefore create a new client for url. " + url);
            writeToClientMap(addressList, zookeeperClient);
        }
        return zookeeperClient;
    }



    protected abstract ZookeeperClient createZookeeperClient(URL url);

    /**
     * 重新定义zookeeper的url。只需保留协议、用户名、密码、主机、端口和单个参数
     * @param url
     * @return
     */
    URL toClientURL(URL url) {
        Map<String, String> parameterMap = new HashMap<>();
        return new URL(url.getProtocol(), url.getUsername(), url.getPassword(), url.getHost(), url.getPort(),
                ZookeeperTransporter.class.getName(), parameterMap);
    }


    /**
     *从缓存中获取ZookeeperClient，必须连接ZookeeperClient
     * @param addressList
     * @return
     */
    ZookeeperClient fetchAndUpdateZookeeperClientCache(List<String> addressList) {
        ZookeeperClient zookeeperClient = null;
        for (String address : addressList) {
            if ((zookeeperClient = zookeeperClientMap.get(address)) != null && zookeeperClient.isConnected()) {
                break;
            }
        }

        if (zookeeperClient != null && zookeeperClient.isConnected()) {
            writeToClientMap(addressList, zookeeperClient);
        }
        return zookeeperClient;
    }

    void writeToClientMap(List<String> addressList, ZookeeperClient zookeeperClient) {
        for (String address : addressList) {
            zookeeperClientMap.put(address, zookeeperClient);
        }
    }

    /**
     * get all zookeeper urls (such as :zookeeper://127.0.0.1:2181?127.0.0.1:8989,127.0.0.1:9999)
     * @param url
     * @return
     */
    List<String> getURLBackupAddress(URL url) {
        List<String> addressList = new ArrayList<String>();
        addressList.add(url.getAddress());
        addressList.addAll(url.getParameter(Constants.BACKUP_KEY, Collections.EMPTY_LIST));
        return addressList;
    }
}
