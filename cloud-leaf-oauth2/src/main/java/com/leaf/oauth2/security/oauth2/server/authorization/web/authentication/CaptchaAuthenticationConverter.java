package com.leaf.oauth2.security.oauth2.server.authorization.web.authentication;

import com.leaf.oauth2.security.oauth2.server.authorization.authentication.OAuth2CaptchaAuthenticationToken;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;


/**
 * @author liuk
 */
public class CaptchaAuthenticationConverter implements AuthenticationConverter {

    private final static String GRANT_TYPE = "captcha";

    @Nullable
    @Override
    public Authentication convert(HttpServletRequest request) {
        String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
        if (!GRANT_TYPE.equals(grantType)) {
            return null;
        }

        Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();
        String clientId = clientPrincipal.getName();
        MultiValueMap<String, String> parameters = getParameters(request);

        Map<String, String> additionalParameters = new HashMap<>();
        parameters.forEach((key, value) -> {
            if (!key.equals(OAuth2ParameterNames.CLIENT_ID) &&
                    !key.equals(OAuth2ParameterNames.GRANT_TYPE)) {
                additionalParameters.put(key, value.get(0));
            }
        });

        return new OAuth2CaptchaAuthenticationToken(clientId, clientPrincipal, additionalParameters);
    }

    private static MultiValueMap<String, String> getParameters(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>(parameterMap.size());
        parameterMap.forEach((key, values) -> {
            for (String value : values) {
                parameters.add(key, value);
            }
        });
        return parameters;
    }
}
