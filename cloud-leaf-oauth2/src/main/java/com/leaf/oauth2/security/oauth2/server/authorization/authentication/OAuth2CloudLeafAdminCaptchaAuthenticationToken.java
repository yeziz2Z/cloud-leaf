package com.leaf.oauth2.security.oauth2.server.authorization.authentication;

import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.authorization.util.SpringAuthorizationServerVersion;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liuk
 */
public class OAuth2CloudLeafAdminCaptchaAuthenticationToken extends AbstractAuthenticationToken {
    private static final long serialVersionUID = SpringAuthorizationServerVersion.SERIAL_VERSION_UID;
    private final String clientId;
    private final Authentication clientPrincipal;
    private final Map<String, String> additionalParameters;

    /**
     * Sub-class constructor.
     *
     * @param clientId the authorization grant type
     * @param clientPrincipal        the authenticated client principal
     * @param additionalParameters   the additional parameters
     */
    public OAuth2CloudLeafAdminCaptchaAuthenticationToken(String clientId,
                                                          Authentication clientPrincipal, @Nullable Map<String, String> additionalParameters) {
        super(Collections.emptyList());
        Assert.notNull(clientId, "clientId cannot be null");
        Assert.notNull(clientPrincipal, "clientPrincipal cannot be null");
        this.clientId = clientId;
        this.clientPrincipal = clientPrincipal;
        this.additionalParameters = Collections.unmodifiableMap(
                additionalParameters != null ?
                        new HashMap<>(additionalParameters) :
                        Collections.emptyMap());
    }

    public String getClientId() {
        return clientId;
    }

    /**
     * Returns the authorization grant type.
     *
     * @return the authorization grant type
     */


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
    public Map<String, String> getAdditionalParameters() {
        return this.additionalParameters;
    }
}
