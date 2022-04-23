package com.leaf.admin.config;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leaf.admin.common.SystemConst;
import com.leaf.admin.common.enums.TokenErrorEnum;
import com.leaf.admin.security.UserDetailsServiceImpl;
import com.leaf.admin.security.exception.CaptchaException;
import com.leaf.admin.sys.dto.UserLoginDTO;
import com.leaf.admin.utils.JwtUtils;
import com.leaf.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;

@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    private String[] WITHE_URLS = {"/auth/login", "/auth/captcha", "/auth/logout", "/auth/refreshToken"};

    private static final String TOKEN_ERROR = "token_error";


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf()
                .disable()
                // 登录配置
                .formLogin()
                /*.loginProcessingUrl("/")
                .successHandler(loginSuccessHandler())
                .failureHandler(loginFailureHandler())*/

                //登出配置
                .and()
                .logout()
                .logoutUrl("/auth/logout")
                .logoutSuccessHandler(jwtLogoutSuccessHandler())

                // 禁用session
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                // 配置拦截规则  白名单放行
                .and()
                .authorizeRequests()
                .antMatchers(WITHE_URLS)
                .permitAll()
                .anyRequest()
                .authenticated()

                //异常处理器
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint())
                .accessDeniedHandler(jwtAccessDeniedHandler())

                // 自定义过滤器配置
                .and()
                .addFilterAt(loginAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
        ;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Bean
    PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 认证失败 Handler
     *
     * @return
     */
    @Bean
    public AuthenticationFailureHandler loginFailureHandler() {
        return (request, response, exception) -> {
            response.setContentType("application/json;charset=UTF-8");

            log.error("loginFailureHandler", exception);
            Result<String> fail = Result.fail(exception.getMessage());

            if (exception instanceof BadCredentialsException) {
                fail.setMsg("用户名或密码错误");
            }

            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(JSONUtil.toJsonStr(fail).getBytes(CharsetUtil.UTF_8));
            outputStream.flush();
            outputStream.close();

        };
    }

    /**
     * 认证成功 Handler
     *
     * @return
     */
    @Bean
    public AuthenticationSuccessHandler loginSuccessHandler() {
        return (request, response, authentication) -> {
            response.setContentType("application/json;charset=UTF-8");

            Map<String, String> payload = MapUtil.builder("name", authentication.getName()).build();

            Result<Map> result = Result.success(MapUtil.builder()
                    .put(SystemConst.ACCESS_TOKEN, jwtUtils.generateAccessToken(payload))
                    .put(SystemConst.REFRESH_TOKEN, jwtUtils.generateRefreshToken(payload))
                    .build());
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(JSONUtil.toJsonStr(result).getBytes(CharsetUtil.UTF_8));
            outputStream.flush();
            outputStream.close();

        };
    }

    @Bean
    public BasicAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        return new BasicAuthenticationFilter(authenticationManager()) {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
                String token = request.getHeader(jwtUtils.getHeader());
                if (StrUtil.isBlankOrUndefined(token)) {
                    chain.doFilter(request, response);
                    return;
                }
                JWT jwt = jwtUtils.getJwtFromToken(token);

                if (!jwtUtils.isValidToken(jwt)) {
                    request.setAttribute(TOKEN_ERROR, TokenErrorEnum.INVALID_TOKEN);
                    chain.doFilter(request, response);
                    return;
                }

                if (jwtUtils.isExpired(jwt)) {
                    request.setAttribute(TOKEN_ERROR, TokenErrorEnum.EXPIRED_TOKEN);
                    chain.doFilter(request, response);
                    return;
                }

                if (!Objects.equals(SystemConst.ACCESS_TOKEN, jwt.getPayload("tokenType"))) {
                    request.setAttribute(TOKEN_ERROR, TokenErrorEnum.INVALID_TOKEN);
                    chain.doFilter(request, response);
                    return;
                }
                // TODO 校验参与生成token 客户端特征  例如请求ip  访问设备等等

                String username = (String) jwt.getPayload("name");

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, userDetailsService.getUserAuthoritiesByUsername(username));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                chain.doFilter(request, response);
            }
        };
    }

    @Bean
    public AuthenticationEntryPoint jwtAuthenticationEntryPoint() {
        return (request, response, authException) -> {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            TokenErrorEnum tokenErrorEnum = (TokenErrorEnum) request.getAttribute(TOKEN_ERROR);

            Result result = Result.fail(tokenErrorEnum.getCode(), tokenErrorEnum.getMessage());

            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(JSONUtil.toJsonStr(result).getBytes(CharsetUtil.UTF_8));
            outputStream.flush();
            outputStream.close();
        };
    }

    @Bean
    public UsernamePasswordAuthenticationFilter loginAuthenticationFilter() throws Exception {
        UsernamePasswordAuthenticationFilter authenticationFilter = new UsernamePasswordAuthenticationFilter() {
            @Override
            public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

                if (request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)) {
                    try (InputStream is = request.getInputStream()) {
                        ObjectMapper mapper = new ObjectMapper();
                        UserLoginDTO userLoginDTO = mapper.readValue(is, UserLoginDTO.class);
                        String key = SystemConst.CAPTCHA_KEY + userLoginDTO.getUid();
                        Object cacheCaptcha = redisTemplate.opsForValue().get(key);
                        log.info("UserLoginDTO {}", userLoginDTO);
                        redisTemplate.delete(key);
                        if (!Objects.equals(cacheCaptcha, userLoginDTO.getCaptcha())) {
                            throw new CaptchaException("验证码错误!");
                        }
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userLoginDTO.getUsername(), userLoginDTO.getPassword());
                        setDetails(request, authenticationToken);
                        return this.getAuthenticationManager().authenticate(authenticationToken);
                    } catch (IOException e) {
                        log.error("", e);
                    }
                }
                return super.attemptAuthentication(request, response);
            }
        };
        authenticationFilter.setAuthenticationSuccessHandler(loginSuccessHandler());
        authenticationFilter.setAuthenticationFailureHandler(loginFailureHandler());
        authenticationFilter.setFilterProcessesUrl("/auth/login");
        authenticationFilter.setAuthenticationManager(super.authenticationManagerBean());
        return authenticationFilter;
    }

    /**
     * 权限不足 异常处理器
     *
     * @return
     */
    @Bean
    public AccessDeniedHandler jwtAccessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            Result<String> fail = Result.fail(accessDeniedException.getMessage());
            log.warn("============>权限不足 异常处理器", accessDeniedException);
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(JSONUtil.toJsonStr(fail).getBytes(CharsetUtil.UTF_8));
            outputStream.flush();
            outputStream.close();
        };
    }

    /**
     * 登出处理器
     *
     * @return
     */
    @Bean
    public LogoutSuccessHandler jwtLogoutSuccessHandler() {
        return ((request, response, authentication) -> {
            response.setContentType("application/json;charset=UTF-8");
            if (!Objects.isNull(authentication)) {
                new SecurityContextLogoutHandler().logout(request, response, authentication);
            }
            response.setHeader(jwtUtils.getHeader(), "");

            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(JSONUtil.toJsonStr(Result.success()).getBytes(CharsetUtil.UTF_8));
            outputStream.flush();
            outputStream.close();
        });
    }
}

