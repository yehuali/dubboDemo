package com.examle.core.remoting.zookeeper.curator;

import com.examle.core.common.Constants;
import com.examle.core.common.URL;
import com.examle.core.remoting.zookeeper.StateListener;
import com.examle.core.remoting.zookeeper.support.AbstractZookeeperClient;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.RetryNTimes;

public class CuratorZookeeperClient extends AbstractZookeeperClient<CuratorWatcher> {

    private final CuratorFramework client;

    public CuratorZookeeperClient(URL url) {
        try{
            int timeout = url.getParameter(Constants.TIMEOUT_KEY, 5000);
            CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
                    .connectString(url.getBackupAddress())
                    .retryPolicy(new RetryNTimes(1, 1000))
                    .connectionTimeoutMs(timeout);
            client = builder.build();
            client.getConnectionStateListenable().addListener(new ConnectionStateListener() {

                @Override
                public void stateChanged(CuratorFramework curatorFramework, ConnectionState state) {
                    if (state == ConnectionState.LOST) {
                        CuratorZookeeperClient.this.stateChanged(StateListener.DISCONNECTED);
                    }else if (state == ConnectionState.CONNECTED) {
                        CuratorZookeeperClient.this.stateChanged(StateListener.CONNECTED);
                    } else if (state == ConnectionState.RECONNECTED) {
                        CuratorZookeeperClient.this.stateChanged(StateListener.RECONNECTED);
                    }
                }
            });
            client.start();
        }catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }

    }

    @Override
    public boolean isConnected() {
        return client.getZookeeperClient().isConnected();
    }
}
