//package com.leaf.oauth2.security.provider;
//
//import com.leaf.common.constant.SecurityConstant;
//import com.leaf.oauth2.config.AuthorizationServerConfig;
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.util.Assert;
//
//import java.util.Map;
//
//public class PreAuthenticatedUserDetailsService<T extends Authentication> implements AuthenticationUserDetailsService<T>, InitializingBean {
//
//    /**
//     * 客户端ID和用户服务 UserDetailService 的映射
//     *
//     * @see AuthorizationServerConfig#jwtAccessTokenConverter() (AuthorizationServerEndpointsConfigurer)
//     */
//    private Map<String, UserDetailsService> userDetailsServiceMap;
//
//    public PreAuthenticatedUserDetailsService() {
//    }
//
//    public PreAuthenticatedUserDetailsService(Map<String, UserDetailsService> userDetailsServiceMap) {
//        Assert.notNull(userDetailsServiceMap, "userDetailsService cannot be null.");
//        this.userDetailsServiceMap = userDetailsServiceMap;
//    }
//
//    @Override
//    public UserDetails loadUserDetails(Authentication token) throws UsernameNotFoundException {
//        // todo 此处写死了
//        return userDetailsServiceMap.get(SecurityConstant.ADMIN_CLIENT_ID).loadUserByUsername(token.getName());
//    }
//
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        Assert.notNull(this.userDetailsServiceMap, "UserDetailsService must be set");
//    }
//}
