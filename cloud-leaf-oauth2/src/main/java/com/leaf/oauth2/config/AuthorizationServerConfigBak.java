//package com.leaf.oauth2.config;
//
//import cn.hutool.core.map.MapUtil;
//import cn.hutool.json.JSONUtil;
//import com.leaf.common.constant.SecurityConstant;
//import com.leaf.common.result.Result;
//import com.leaf.oauth2.security.provider.CaptchaGranter;
//import com.leaf.oauth2.security.provider.ClientDetailsServiceImpl;
//import com.leaf.oauth2.security.provider.PreAuthenticatedUserDetailsService;
//import com.leaf.oauth2.security.userdetails.AdminUserDetails;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.ProviderManager;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
//import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
//import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
//import org.springframework.security.oauth2.provider.CompositeTokenGranter;
//import org.springframework.security.oauth2.provider.TokenGranter;
//import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
//import org.springframework.security.oauth2.provider.token.TokenEnhancer;
//import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
//import org.springframework.security.oauth2.provider.token.TokenStore;
//import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
//import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
//import org.springframework.security.web.AuthenticationEntryPoint;
//import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
//
//import java.util.*;
//
//@Configuration
//@EnableAuthorizationServer
//@Slf4j
//public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
//
//    AuthenticationManager authenticationManager;
//
//    UserDetailsService sysUserDetailsServiceImpl;
//
//    PasswordEncoder passwordEncoder;
//
//    ClientDetailsServiceImpl clientDetailsService;
//
//    StringRedisTemplate redisTemplate;
//
//    @Autowired
//    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
//        this.authenticationManager = authenticationManager;
//    }
//
//    @Autowired
//    public void setSysUserDetailsServiceImpl(UserDetailsService sysUserDetailsServiceImpl) {
//        this.sysUserDetailsServiceImpl = sysUserDetailsServiceImpl;
//    }
//
//    @Autowired
//    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    @Autowired
//    public void setClientDetailsService(ClientDetailsServiceImpl clientDetailsService) {
//        this.clientDetailsService = clientDetailsService;
//    }
//
//    @Autowired
//    public void setRedisTemplate(StringRedisTemplate redisTemplate) {
//        this.redisTemplate = redisTemplate;
//    }
//
//    @Override
//    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//
//        // 获取原有默认授权模式(授权码模式、密码模式、客户端模式、简化模式)的授权者
//        List<TokenGranter> granterList = new ArrayList<>(Arrays.asList(endpoints.getTokenGranter()));
//        // 添加验证码授权模式授权者
//        granterList.add(new CaptchaGranter(authenticationManager,
//                endpoints.getTokenServices(),
//                clientDetailsService,
//                endpoints.getOAuth2RequestFactory(),
//                redisTemplate));
//
//        CompositeTokenGranter compositeTokenGranter = new CompositeTokenGranter(granterList);
//        endpoints.authenticationManager(authenticationManager)
////                .userDetailsService(sysUserDetailsServiceImpl)
////                .tokenStore(jwtTokenStore())
////                .accessTokenConverter(jwtAccessTokenConverter())
//                .tokenGranter(compositeTokenGranter)
//                .tokenServices(tokenServices());
//    }
//
//    @Override
//    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//        clients.withClientDetails(clientDetailsService);
//    }
//
//    @Bean
//    public TokenStore jwtTokenStore() {
//        return new JwtTokenStore(jwtAccessTokenConverter());
//    }
//
//    @Bean
//    public JwtAccessTokenConverter jwtAccessTokenConverter() {
//        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
////        converter.setKeyPair(keyPair());
//        converter.setSigningKey("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJhZG1pbiIsInNjb3BlIjpbImFsbCJdLCJkZXB0SWQiOjIsImV4cCI6MTY1MzAzNTYxOSwidXNlcklkIjoyLCJhdXRob3JpdGllcyI6WyJBRE1JTiJdLCJqdGkiOiI1ZGFhZDljZi04YzhiLTQ5NzItYjBiYi1lYjdiZjc3YjAxNWIiLCJjbGllbnRfaWQiOiJtYWxsLWFkbWluLXdlYiIsInVzZXJuYW1lIjoiYWRtaW4ifQ.YKu2Z8ZEBIVqJHdJhKpZvwg1k3BdsCe0g2LxI8iHrtg");
//        return converter;
//    }
//
//
//    public DefaultTokenServices tokenServices() {
//        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
//        List<TokenEnhancer> tokenEnhancers = new ArrayList<>();
//        tokenEnhancers.add(tokenEnhancer());
//        tokenEnhancers.add(jwtAccessTokenConverter());
//        tokenEnhancerChain.setTokenEnhancers(tokenEnhancers);
//
//        DefaultTokenServices tokenServices = new DefaultTokenServices();
//        tokenServices.setClientDetailsService(clientDetailsService);
//        tokenServices.setTokenStore(jwtTokenStore());
//        tokenServices.setTokenEnhancer(tokenEnhancerChain);
//
//        Map<String, UserDetailsService> userDetailsServiceMap = new HashMap<>();
//        userDetailsServiceMap.put(SecurityConstant.ADMIN_CLIENT_ID, sysUserDetailsServiceImpl);
//        PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
//        provider.setPreAuthenticatedUserDetailsService(new PreAuthenticatedUserDetailsService<>(userDetailsServiceMap));
//        tokenServices.setAuthenticationManager(new ProviderManager(Arrays.asList(provider)));
//        // 支持 RefreshToken 自动续期
//        tokenServices.setSupportRefreshToken(true);
////        默认值 为true
////        1.设为true时  调用刷新 token 时间原 refresh_token 过期时间不变
////        2. 设为false 每次调用  重置 refresh_token 过期时间
//        tokenServices.setReuseRefreshToken(false);
//        return tokenServices;
//    }
//
//    /**
//     * token 内容扩展
//     *
//     * @return
//     */
//    @Bean
//    public TokenEnhancer tokenEnhancer() {
//        return (accessToken, authentication) -> {
//            Map<String, Object> additionalInfo = MapUtil.newHashMap();
//            Object principal = authentication.getUserAuthentication().getPrincipal();
//            // TODO 利用设计模式 扩展该实现
//            if (principal instanceof AdminUserDetails) {
//                AdminUserDetails userDetails = (AdminUserDetails) principal;
//                additionalInfo.put(SecurityConstant.JWT_USER_ID, userDetails.getUserId());
//                additionalInfo.put(SecurityConstant.JWT_USERNAME, userDetails.getUsername());
//                additionalInfo.put(SecurityConstant.JWT_ORG_ID, userDetails.getOrgId());
//                // 认证身份标识(username:用户名；)
//                /*if (StrUtil.isNotBlank(userDetails.getAuthenticationIdentity())) {
//                    additionalInfo.put("authenticationIdentity", userDetails.getAuthenticationIdentity());
//                }*/
//            }
//            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
//            return accessToken;
//        };
//    }
//
//
//    @Bean
//    public AuthenticationEntryPoint authenticationEntryPoint() {
//        return (request, response, e) -> {
//            log.error("认证失败: {}", e.getMessage(), e);
//            response.setStatus(HttpStatus.OK.value());
//            response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
//            response.setHeader("Access-Control-Allow-Origin", "*");
//            response.setHeader("Cache-Control", "no-cache");
//            Result result = Result.fail("501");
//            response.getWriter().print(JSONUtil.toJsonStr(result));
//            response.getWriter().flush();
//        };
//    }
//}
//
