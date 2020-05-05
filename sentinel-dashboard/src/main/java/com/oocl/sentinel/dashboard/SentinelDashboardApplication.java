package com.oocl.sentinel.dashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.oocl.sentinel"})
public class SentinelDashboardApplication {

    public static void main(String[] args) {
        SpringApplication.run(SentinelDashboardApplication.class, args);
    }

}
