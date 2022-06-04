package com.leaf.admin.utils;

import cn.hutool.json.JSONObject;
import com.leaf.common.constant.SecurityConstant;
import com.leaf.common.web.utils.JwtUtil;

/**
 * 用户工具类
 */
public class UserUtils {

    public static Long getCurrentUserId() {
        Long userId = null;
        JSONObject jwtPayload = JwtUtil.getJwtPayload();
        if (jwtPayload != null) {
            userId = jwtPayload.getLong(SecurityConstant.JWT_USER_ID);
        }
        return userId;
    }

    public static String getCurrentUsername() {
        String username = null;
        JSONObject jwtPayload = JwtUtil.getJwtPayload();
        if (jwtPayload != null) {
            username = jwtPayload.getStr(SecurityConstant.JWT_USER_NAME);
        }
        return username;
    }
}
