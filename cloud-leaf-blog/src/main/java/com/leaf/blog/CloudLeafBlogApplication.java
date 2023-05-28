package com.leaf.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author liuk
 */
@SpringBootApplication
@EnableDiscoveryClient
public class CloudLeafBlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudLeafBlogApplication.class, args);
    }
}
