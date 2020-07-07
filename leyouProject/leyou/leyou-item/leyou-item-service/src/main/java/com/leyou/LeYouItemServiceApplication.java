package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(basePackages = {"com.leyou.item.mapper"})
public class LeYouItemServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(LeYouItemServiceApplication.class,args);
    }
}