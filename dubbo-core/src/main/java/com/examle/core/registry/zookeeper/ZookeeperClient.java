package com.examle.core.registry.zookeeper;

import com.examle.core.remoting.zookeeper.StateListener;

public interface ZookeeperClient {

    boolean isConnected();

    void addStateListener(StateListener listener);
}
