package com.leaf.common.web.utils;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.leaf.common.constant.SecurityConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Slf4j
public class JwtUtil {


    public static JSONObject getJwtPayload() {
        JSONObject jsonObject = null;
        String payload = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader(SecurityConstant.JWT_PAYLOAD_KEY);
        if (StrUtil.isNotBlank(payload)) {
            try {
                jsonObject = JSONUtil.parseObj(URLDecoder.decode(payload, StandardCharsets.UTF_8.name()));
            } catch (UnsupportedEncodingException e) {
                ExceptionUtil.wrapRuntimeAndThrow("parse jwt payload error!");
            }
        }
        return jsonObject;
    }
}
