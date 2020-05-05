package com.oocl.sentinel.cluster.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.oocl.sentinel"})
public class SentinelClient2Application {

    public static void main(String[] args) {
        SpringApplication.run(SentinelClient2Application.class, args);
    }

}
