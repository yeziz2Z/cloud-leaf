package com.leaf.oauth2.security.authentication;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;

import java.io.Serial;
import java.util.Collection;

/**
 * @author liuk
 */
public class CloudLeafAdminUserAuthenticationToken extends AbstractAuthenticationToken {

    private final Long userId;

    @Serial
    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    private final Object principal;

    private final Object credentials;

    /**
     * cloud-leaf-admin  Authentication token
     *
     * @param principal   信息
     * @param userId      用户id
     * @param credentials 凭证
     * @param authorities 权限
     */
    public CloudLeafAdminUserAuthenticationToken(Object principal, Long userId, Object credentials,
                                                 Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.userId = userId;
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true); // must use super, as we override
    }


    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }


    public Long getUserId() {
        return userId;
    }
}
