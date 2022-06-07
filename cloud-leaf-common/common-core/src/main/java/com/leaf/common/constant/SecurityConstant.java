package com.leaf.common.constant;

public interface SecurityConstant {

    /**
     * 管理员
     */
    String ROLE_ADMIN = "admin";

    String AUTHORIZATION_KEY = "Authorization";

    String VALID_CODE = "VALID_CODE:";

    String ADMIN_CLIENT_ID = "cloud-leaf-admin";

    /**
     * JWT存储权限前缀
     */
    String AUTHORITY_PREFIX = "ROLE_";

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
    String JWT_USER_ID = "userId";

    /**
     * 用户名
     */
    String JWT_USERNAME = "username";
    /**
     * 组织机构id
     */
    String JWT_ORG_ID = "orgId";

    /**
     * 黑名单token前缀
     */
    String TOKEN_BLACKLIST_PREFIX = "auth:token:blacklist:";

    /**
     * [ {接口路径:[角色编码]},...]
     */
    String URL_PERM_ROLES_KEY = "system:perm_roles_rule:url";

    /**
     * cloud-leaf-admin 用户权限列表
     */
    String USER_PERM_URL_KEY = "system:user_perm_urls:";
}
