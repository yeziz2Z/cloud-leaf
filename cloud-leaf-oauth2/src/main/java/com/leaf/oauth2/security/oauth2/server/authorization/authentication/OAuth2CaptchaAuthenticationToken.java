package com.leaf.oauth2.security.oauth2.server.authorization.authentication;

import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.util.SpringAuthorizationServerVersion;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liuk
 */
public class OAuth2CaptchaAuthenticationToken extends AbstractAuthenticationToken {
    private static final long serialVersionUID = SpringAuthorizationServerVersion.SERIAL_VERSION_UID;
    private final String authorizationGrantType;
    private final Authentication clientPrincipal;
    private final Map<String, Object> additionalParameters;

    /**
     * Sub-class constructor.
     *
     * @param authorizationGrantType the authorization grant type
     * @param clientPrincipal        the authenticated client principal
     * @param additionalParameters   the additional parameters
     */
    public OAuth2CaptchaAuthenticationToken(String authorizationGrantType,
                                                          Authentication clientPrincipal, @Nullable Map<String, Object> additionalParameters) {
        super(Collections.emptyList());
        Assert.notNull(authorizationGrantType, "authorizationGrantType cannot be null");
        Assert.notNull(clientPrincipal, "clientPrincipal cannot be null");
        this.authorizationGrantType = authorizationGrantType;
        this.clientPrincipal = clientPrincipal;
        this.additionalParameters = Collections.unmodifiableMap(
                additionalParameters != null ?
                        new HashMap<>(additionalParameters) :
                        Collections.emptyMap());
    }

    /**
     * Returns the authorization grant type.
     *
     * @return the authorization grant type
     */
    public String getGrantType() {
        return this.authorizationGrantType;
    }


    @Override
    public Object getPrincipal() {
        return this.clientPrincipal;
    }

    @Override
    public Object getCredentials() {
        return "";
    }

    /**
     * Returns the additional parameters.
     *
     * @return the additional parameters
     */
    public Map<String, Object> getAdditionalParameters() {
        return this.additionalParameters;
    }
}
