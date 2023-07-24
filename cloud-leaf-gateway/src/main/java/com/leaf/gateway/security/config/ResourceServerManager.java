//package com.leaf.gateway.security.config;
//
//import cn.hutool.core.collection.CollectionUtil;
//import cn.hutool.core.util.StrUtil;
//import com.leaf.common.constant.SecurityConstant;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.security.authorization.AuthorizationDecision;
//import org.springframework.security.authorization.ReactiveAuthorizationManager;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.web.server.authorization.AuthorizationContext;
//import org.springframework.stereotype.Component;
//import org.springframework.util.AntPathMatcher;
//import org.springframework.util.PathMatcher;
//import reactor.core.publisher.Mono;
//
//import java.util.HashSet;
//import java.util.Map;
//import java.util.Set;
//
///**
// * @author liuk
// */
//@Component
//public class ResourceServerManager implements ReactiveAuthorizationManager<AuthorizationContext> {
//
//    @Autowired
//    RedisTemplate redisTemplate;
//
//    @Override
//    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext authorizationContext) {
//        ServerHttpRequest request = authorizationContext.getExchange().getRequest();
//        if (request.getMethod() == HttpMethod.OPTIONS) { // 预检请求放行
//            return Mono.just(new AuthorizationDecision(true));
//        }
//
//        PathMatcher pathMatcher = new AntPathMatcher();
//        String method = request.getMethod().name();
//        String path = request.getURI().getPath();
//        String restfulPath = method + ":" + path;
//
//        Map<String, Set<String>> perms = redisTemplate.opsForHash().entries(SecurityConstant.URL_PERM_ROLES_KEY);
//
//        Set<String> authorizedRoles = new HashSet<>();
//        for (String url : perms.keySet()) {
//            if (pathMatcher.match(url, restfulPath)) {
//                authorizedRoles.addAll(perms.get(url));
//            }
//        }
//        // 没有相应权限限制
//        if (CollectionUtil.isEmpty(authorizedRoles)) {
//            return Mono.just(new AuthorizationDecision(true));
//        }
//
//        // 判断JWT中携带的用户是否有权限访问
//        Mono<AuthorizationDecision> authorizationDecisionMono = authentication
//                .filter(Authentication::isAuthenticated)
//                .flatMapIterable(Authentication::getAuthorities)
//                .map(GrantedAuthority::getAuthority)
//                .any(roleStr -> {
//                    String role = StrUtil.subAfter(roleStr, SecurityConstant.AUTHORITY_PREFIX, true);
//                    if (StrUtil.equals(SecurityConstant.ROLE_ADMIN, role)) {
//                        return true;
//                    }
//                    return authorizedRoles.contains(role);
//                })
//                .map(AuthorizationDecision::new)
//                .defaultIfEmpty(new AuthorizationDecision(false));
//        return authorizationDecisionMono;
//    }
//
//    public static void main(String[] args) {
//        String t = "ROLE_hello";
//
//        System.out.println(StrUtil.subAfter(t, SecurityConstant.AUTHORITY_PREFIX, true));
//    }
//}
