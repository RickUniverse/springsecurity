package org.aciservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * 可能需要先开启网关，可能。。。。，反正都试试
 */
@SpringBootApplication
@EnableDiscoveryClient
// 之后的项目一定要加上atguigu 之类的前缀
//@ComponentScan(basePackages = "org.aciservice")
@MapperScan("org.aciservice.mapper")
public class ServiceAclApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceAclApplication.class, args);
    }

}
