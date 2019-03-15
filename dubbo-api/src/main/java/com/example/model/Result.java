package com.example.model;

import java.io.Serializable;

public class Result implements Serializable {
    private String userName;
    private String msg;

    public Result(String userName, String msg) {
        this.userName = userName;
        this.msg = msg;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
