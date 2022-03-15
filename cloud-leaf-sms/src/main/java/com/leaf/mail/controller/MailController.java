package com.leaf.mail.controller;

import com.leaf.common.result.Result;
import com.leaf.mail.service.IMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mail")
public class MailController {

    @Autowired
    private IMailService mailService;

    @PostMapping("send")
    public Result send(String to, String code) {
        mailService.send(to,code);
        return Result.success();
    }

}
