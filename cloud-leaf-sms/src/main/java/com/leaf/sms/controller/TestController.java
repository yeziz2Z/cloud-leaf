package com.leaf.sms.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    /*@Value("${config.info}")
    private String configInfo;

    @GetMapping("/getConfig")
    public String getConfig() {
        return this.configInfo;
    }*/
}
