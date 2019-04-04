package com.examle.core.rpc;

public class RpcException  extends RuntimeException{

    public static final int FORBIDDEN_EXCEPTION = 4;
    private int code;

    public RpcException(String message) {
        super(message);
    }

    public RpcException(int code, String message) {
        super(message);
        this.code = code;
    }
}
