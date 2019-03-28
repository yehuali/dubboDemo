package com.example.dubbo.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("spring/dubbo-consumer.xml")
public class DubboConsumerApplicaiton {
    public static void main(String[] args) {
        SpringApplication.run(DubboConsumerApplicaiton.class, args);
    }
}
