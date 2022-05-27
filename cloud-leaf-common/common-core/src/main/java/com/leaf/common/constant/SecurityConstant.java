package com.leaf.common.constant;

public interface SecurityConstant {

    String AUTHORIZATION_KEY = "Authorization";

    String VALID_CODE = "VALID_CODE:";

    String ADMIN_CLIENT_ID = "cloud-leaf-admin";

    /**
     * JWT令牌前缀
     */
    String JWT_PREFIX = "Bearer ";

    /**
     * JWT载体key
     */
    String JWT_PAYLOAD_KEY = "payload";
}
