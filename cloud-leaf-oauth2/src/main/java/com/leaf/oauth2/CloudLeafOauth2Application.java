package com.leaf.oauth2;

import com.leaf.admin.api.OauthClientFeignClient;
import com.leaf.admin.api.UserFeignClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackageClasses = {OauthClientFeignClient.class, UserFeignClient.class})
public class CloudLeafOauth2Application {

    public static void main(String[] args) {
        SpringApplication.run(CloudLeafOauth2Application.class, args);
    }
}
