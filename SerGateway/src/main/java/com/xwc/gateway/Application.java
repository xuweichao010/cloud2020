package com.xwc.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import springfox.documentation.oas.annotations.EnableOpenApi;

/**
 * 作者：徐卫超 cc
 * 时间：2020/9/10
 * 描述：项目启动类
 */

@SpringBootApplication
@EnableDiscoveryClient
@EnableOpenApi
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @GetMapping("/test")
    public String test() {
        return "hello I'm Test ";
    }
}
