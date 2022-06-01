package com.leaf.common.pojo.auth;

import lombok.Data;

import java.io.Serializable;

/**
 * Oauth2 pojo
 */
@Data
public class Oauth2Client implements Serializable {


    private static final long serialVersionUID = 1L;

    /**
     * 客户端标识
     */
    private String clientId;

    /**
     *
     */
    private String resourcesIds;

    /**
     * 客户端密钥
     */
    private String clientSecret;

    /**
     * 客户端权限范围
     */
    private String scope;

    /**
     * 授权类型
     */
    private String authorizedGrantTypes;

    /**
     * 重定向地址
     */
    private String webServerRedirectUri;

    /**
     *
     */
    private String authorities;

    /**
     * 访问令牌有效时间 单位:s
     */
    private Integer accessTokenValidity;

    /**
     * 刷新令牌有效时间 单位:s
     */
    private Integer refreshTokenValidity;

    /**
     * 其他信息
     */
    private String additionalInformation;

    /**
     * 自动通过
     */
    private Integer autoApprove;


}
