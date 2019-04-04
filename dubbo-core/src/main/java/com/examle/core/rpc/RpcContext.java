package com.examle.core.rpc;

import com.examle.core.common.threadlocal.InternalThreadLocal;

import java.util.HashMap;
import java.util.Map;

public class RpcContext {

    private final Map<String, String> attachments = new HashMap<String, String>();

    /**
     * 使用内部线程本地来提高性能
     */
    private static final InternalThreadLocal<RpcContext> LOCAL = new InternalThreadLocal<RpcContext>() {
        @Override
        protected RpcContext initialValue() {
            return new RpcContext();
        }
    };

    public static RpcContext getContext() {
        return LOCAL.get();
    }

    public Map<String, String> getAttachments() {
        return attachments;
    }

    public RpcContext setAttachment(String key, String value) {
        if (value == null) {
            attachments.remove(key);
        } else {
            attachments.put(key, value);
        }
        return this;
    }
}
