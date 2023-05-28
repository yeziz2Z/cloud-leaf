package com.leaf.auth.controller;

import com.leaf.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

//@RestController
@RequestMapping("/oauth")
@Slf4j
public class AuthController {


    @PostMapping("/token")
    public Object postAccessToken(Principal principal, Map<String, String> parameters) {
        log.info("principal {}, param {}", principal, parameters);
        return null;
    }

   /* @PostMapping("/token")
    public Object postAccessToken(HttpServletRequest request) {
        log.info("principal {}, param {}", request);
        return null;
    }*/

    @GetMapping("ping")
    public Result pong(String text) {
        log.info("param {}", text);
        return Result.success();
    }
}
