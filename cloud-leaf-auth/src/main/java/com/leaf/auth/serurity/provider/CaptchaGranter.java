//package com.leaf.auth.serurity.provider;
//
//import com.alibaba.cloud.commons.lang.StringUtils;
//import com.leaf.common.constant.SystemConstant;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.security.authentication.*;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
//import org.springframework.security.oauth2.provider.*;
//import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
//import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
//import org.springframework.validation.BindException;
//
//import java.util.LinkedHashMap;
//import java.util.Map;
//
//public class CaptchaGranter extends AbstractTokenGranter {
//
//    private static final String GRANT_TYPE = "captcha";
//
//    private final AuthenticationManager authenticationManager;
//
//    private StringRedisTemplate redisTemplate;
//
//    public CaptchaGranter(AuthenticationManager authenticationManager,
//                          AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory) {
//        this(authenticationManager, tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
//    }
//
//    protected CaptchaGranter(AuthenticationManager authenticationManager, AuthorizationServerTokenServices tokenServices,
//                             ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory, String grantType) {
//        super(tokenServices, clientDetailsService, requestFactory, grantType);
//        this.authenticationManager = authenticationManager;
//    }
//
//    @Override
//    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
//
//        Map<String, String> parameters = new LinkedHashMap<>(tokenRequest.getRequestParameters());
//        String username = parameters.get("username");
//        String password = parameters.get("password");
//        String captcha = parameters.get("captcha");
//        String uid = parameters.get("uid");
//        if (StringUtils.isEmpty(captcha)) {
//            throw new InvalidGrantException("验证码不能为空");
//        }
//        String validCaptchaKey = SystemConstant.VALID_CODE + uid;
//
//        String captchaCode = redisTemplate.opsForValue().getAndDelete(validCaptchaKey);
//        if (StringUtils.equals(captcha, captchaCode)) {
//            throw new InvalidGrantException("验证码错误");
//        }
//
//        // Protect from downstream leaks of password
//        parameters.remove("password");
//        parameters.remove("uid");
//        parameters.remove("captcha");
//
//        Authentication userAuth = new UsernamePasswordAuthenticationToken(username, password);
//        ((AbstractAuthenticationToken) userAuth).setDetails(parameters);
//        try {
//            userAuth = authenticationManager.authenticate(userAuth);
//        } catch (AccountStatusException ase) {
//            //covers expired, locked, disabled cases (mentioned in section 5.2, draft 31)
//            throw new InvalidGrantException(ase.getMessage());
//        } catch (BadCredentialsException e) {
//            // If the username/password are wrong the spec says we should send 400/invalid grant
//            throw new InvalidGrantException(e.getMessage());
//        }
//        if (userAuth == null || !userAuth.isAuthenticated()) {
//            throw new InvalidGrantException("Could not authenticate user: " + username);
//        }
//
//        OAuth2Request storedOAuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);
//        return new OAuth2Authentication(storedOAuth2Request, userAuth);
//    }
//}
