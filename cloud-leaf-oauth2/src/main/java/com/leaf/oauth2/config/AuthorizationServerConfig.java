package com.leaf.oauth2.config;

import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import com.leaf.common.constant.SecurityConstant;
import com.leaf.common.result.Result;
import com.leaf.oauth2.security.provider.CaptchaGranter;
import com.leaf.oauth2.security.provider.ClientDetailsServiceImpl;
import com.leaf.oauth2.security.provider.PreAuthenticatedUserDetailsService;
import com.leaf.oauth2.security.userdetails.AdminUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;

import java.util.*;

@Configuration
@EnableAuthorizationServer
@Slf4j
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserDetailsService sysUserDetailsServiceImpl;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    ClientDetailsServiceImpl clientDetailsService;
    @Autowired
    StringRedisTemplate redisTemplate;


    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

        // ??????????????????????????????(???????????????????????????????????????????????????????????????)????????????
        List<TokenGranter> granterList = new ArrayList<>(Arrays.asList(endpoints.getTokenGranter()));
        // ????????????????????????????????????
        granterList.add(new CaptchaGranter(authenticationManager,
                endpoints.getTokenServices(),
                clientDetailsService,
                endpoints.getOAuth2RequestFactory(),
                redisTemplate));

        CompositeTokenGranter compositeTokenGranter = new CompositeTokenGranter(granterList);
        endpoints.authenticationManager(authenticationManager)
//                .userDetailsService(sysUserDetailsServiceImpl)
//                .tokenStore(jwtTokenStore())
//                .accessTokenConverter(jwtAccessTokenConverter())
                .tokenGranter(compositeTokenGranter)
                .tokenServices(tokenServices());
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService);
    }

    @Bean
    public TokenStore jwtTokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
//        converter.setKeyPair(keyPair());
        converter.setSigningKey("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJhZG1pbiIsInNjb3BlIjpbImFsbCJdLCJkZXB0SWQiOjIsImV4cCI6MTY1MzAzNTYxOSwidXNlcklkIjoyLCJhdXRob3JpdGllcyI6WyJBRE1JTiJdLCJqdGkiOiI1ZGFhZDljZi04YzhiLTQ5NzItYjBiYi1lYjdiZjc3YjAxNWIiLCJjbGllbnRfaWQiOiJtYWxsLWFkbWluLXdlYiIsInVzZXJuYW1lIjoiYWRtaW4ifQ.YKu2Z8ZEBIVqJHdJhKpZvwg1k3BdsCe0g2LxI8iHrtg");
        return converter;
    }


    public DefaultTokenServices tokenServices() {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        List<TokenEnhancer> tokenEnhancers = new ArrayList<>();
        tokenEnhancers.add(tokenEnhancer());
        tokenEnhancers.add(jwtAccessTokenConverter());
        tokenEnhancerChain.setTokenEnhancers(tokenEnhancers);

        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setClientDetailsService(clientDetailsService);
        tokenServices.setTokenStore(jwtTokenStore());
        tokenServices.setTokenEnhancer(tokenEnhancerChain);

        Map<String, UserDetailsService> userDetailsServiceMap = new HashMap<>();
        userDetailsServiceMap.put(SecurityConstant.ADMIN_CLIENT_ID, sysUserDetailsServiceImpl);
        PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
        provider.setPreAuthenticatedUserDetailsService(new PreAuthenticatedUserDetailsService<>(userDetailsServiceMap));
        tokenServices.setAuthenticationManager(new ProviderManager(Arrays.asList(provider)));
        // ?????? RefreshToken ????????????
        tokenServices.setSupportRefreshToken(true);
//        ????????? ???true
//        1.??????true???  ???????????? token ????????? refresh_token ??????????????????
//        2. ??????false ????????????  ?????? refresh_token ????????????
        tokenServices.setReuseRefreshToken(false);
        return tokenServices;
    }

    /**
     * token ????????????
     *
     * @return
     */
    @Bean
    public TokenEnhancer tokenEnhancer() {
        return (accessToken, authentication) -> {
            Map<String, Object> additionalInfo = MapUtil.newHashMap();
            Object principal = authentication.getUserAuthentication().getPrincipal();
            // TODO ?????????????????? ???????????????
            if (principal instanceof AdminUserDetails) {
                AdminUserDetails userDetails = (AdminUserDetails) principal;
                additionalInfo.put(SecurityConstant.JWT_USER_ID, userDetails.getUserId());
                additionalInfo.put(SecurityConstant.JWT_USERNAME, userDetails.getUsername());
                additionalInfo.put(SecurityConstant.JWT_ORG_ID, userDetails.getOrgId());
                // ??????????????????(username:????????????)
                /*if (StrUtil.isNotBlank(userDetails.getAuthenticationIdentity())) {
                    additionalInfo.put("authenticationIdentity", userDetails.getAuthenticationIdentity());
                }*/
            }
            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
            return accessToken;
        };
    }


    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, e) -> {
            log.error("????????????: {}", e.getMessage(), e);
            response.setStatus(HttpStatus.OK.value());
            response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Cache-Control", "no-cache");
            Result result = Result.fail("501");
            response.getWriter().print(JSONUtil.toJsonStr(result));
            response.getWriter().flush();
        };
    }
}

