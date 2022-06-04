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

    /**
     * JWT refresh 标识
     */
    String JWT_ATI = "ati";

    /**
     * JWT ID 标识
     */
    String JWT_JTI = "jti";

    /**
     * JWT ID 标识
     */
    String JWT_EXP = "exp";

    /**
     * 用户id
     */
    String JWT_USER_ID = "user_id";

    /**
     * 用户名
     */
    String JWT_USER_NAME = "user_name";

    /**
     * 黑名单token前缀
     */
    String TOKEN_BLACKLIST_PREFIX = "auth:token:blacklist:";
}
