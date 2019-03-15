package com.example.impl;

import com.example.api.DemoService;
import com.example.model.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component(value = "demoService")
public class DemoServiceImpl implements DemoService {

    public static final Logger logger = LoggerFactory.getLogger(DemoServiceImpl.class);

    @Value("${service.user.name}")
    private String name;

    @Override
    public Result sayHello(String localName) {
        logger.info("[" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "] Hello " + name + ", request from consumer: " + RpcContext
                .getContext().getRemoteAddress());
        return new Result(name, "Hello " + localName + ", response from provider: " + RpcContext.getContext().getLocalAddress());
    }
}
