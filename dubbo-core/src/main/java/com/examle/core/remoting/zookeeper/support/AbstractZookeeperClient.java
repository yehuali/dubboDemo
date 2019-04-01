package com.examle.core.remoting.zookeeper.support;

import com.examle.core.registry.zookeeper.ZookeeperClient;
import com.examle.core.remoting.zookeeper.StateListener;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public abstract  class AbstractZookeeperClient<TargetChildListener> implements ZookeeperClient {

    private final Set<StateListener> stateListeners = new CopyOnWriteArraySet<StateListener>();

    public Set<StateListener> getSessionListeners() {
        return stateListeners;
    }

    protected void stateChanged(int state) {
        for (StateListener sessionListener : getSessionListeners()) {
            sessionListener.stateChanged(state);
        }
    }

    @Override
    public void addStateListener(StateListener listener) {
        stateListeners.add(listener);
    }
}
