package com.leaf.oauth2.security.oauth2.server.authorization.web.authentication;

import com.leaf.oauth2.security.oauth2.server.authorization.authentication.OAuth2CaptchaAuthenticationToken;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;


/**
 * @author liuk
 */
public class CaptchaAuthenticationConverter implements AuthenticationConverter {
    @Nullable
    @Override
    public Authentication convert(HttpServletRequest request) {
        String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
        if (!"urn:ietf:params:oauth:grant-type:custom_code".equals(grantType)) {
            return null;
        }

        Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();
        MultiValueMap<String, String> parameters = getParameters(request);

        // code (REQUIRED)
        String captcha = parameters.getFirst("captcha");
        if (!StringUtils.hasText(captcha) ||
                parameters.get(OAuth2ParameterNames.CODE).size() != 1) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_REQUEST);
        }

        Map<String, Object> additionalParameters = new HashMap<>();
        parameters.forEach((key, value) -> {
            if (!key.equals(OAuth2ParameterNames.GRANT_TYPE) &&
                    !key.equals(OAuth2ParameterNames.CLIENT_ID) &&
                    !key.equals(OAuth2ParameterNames.CODE)) {
                additionalParameters.put(key, value.get(0));
            }
        });

        return new OAuth2CaptchaAuthenticationToken(captcha, clientPrincipal, additionalParameters);
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
