package com.oocl.sentinel.flow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.oocl.sentinel.flow"})
public class SentinelFlowControlApplication {

    public static void main(String[] args) {
        SpringApplication.run(SentinelFlowControlApplication.class, args);
    }

}
