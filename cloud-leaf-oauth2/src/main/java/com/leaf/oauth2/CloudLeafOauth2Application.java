package com.leaf.oauth2;

import com.leaf.admin.api.CloudLeafAdminUserFeignClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackageClasses = {CloudLeafAdminUserFeignClient.class})
@EnableDiscoveryClient
public class CloudLeafOauth2Application {

    public static void main(String[] args) {
        SpringApplication.run(CloudLeafOauth2Application.class, args);
    }
}
