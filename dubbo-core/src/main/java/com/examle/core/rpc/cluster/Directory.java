package com.examle.core.rpc.cluster;

import com.examle.core.common.Node;

public interface Directory<T> extends Node {
    Class<T> getInterface();
}
