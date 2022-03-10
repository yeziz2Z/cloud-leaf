package com.leaf.admin.config;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWT;
import com.leaf.admin.security.UserDetailsServiceImpl;
import com.leaf.admin.security.exception.CaptchaException;
import com.leaf.admin.utils.JwtUtils;
import com.leaf.common.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
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
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    static String CAPTCHA_KEY = "captcha";

    private String[] WITHE_URLS = {"/auth/login", "/auth/captcha", "/logout"};

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
                .successHandler(loginSuccessHandler())
                .failureHandler(loginFailureHandler())

                //登出配置
                .and()
                .logout()
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
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(captchaFilter(), UsernamePasswordAuthenticationFilter.class)
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
     * 验证码过滤器
     *
     * @return
     */
    @Bean
    public OncePerRequestFilter captchaFilter() {

        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
                String uri = request.getRequestURI();
                if ("/login".equals(uri) && request.getMethod().equals("POST")) {
                    try {
                        validate(request);
                    } catch (CaptchaException e) {
                        loginFailureHandler().onAuthenticationFailure(request, response, e);
                    }
                }

                filterChain.doFilter(request, response);
            }

            private void validate(HttpServletRequest request) {
                String key = request.getParameter("key");
                String captcha = request.getParameter("captcha");

                if (StrUtil.isBlank(key) || StrUtil.isBlank(captcha)) {
                    throw new CaptchaException("验证码错误");
                }
                if (!captcha.equals(redisTemplate.opsForHash().get(CAPTCHA_KEY, key))) {
                    throw new CaptchaException("验证码错误");
                }

                // 一次性使用
                redisTemplate.opsForHash().delete(CAPTCHA_KEY, key);
            }
        };
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

//            Result<String> fail = Result.fail(exception.getMessage());
            Result<String> fail = Result.ok(exception.getMessage());
            if (exception instanceof BadCredentialsException) {
                fail.setMsg("用户名或密码错误");
            }
            /*if (exception instanceof CaptchaException) {
                fail.setMsg(exception.getMessage());
            }*/

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

            String token = jwtUtils.generateToken(authentication.getName());
            response.setHeader(jwtUtils.getHeader(), token);

            Result<String> result = Result.success();

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
//                    throw new JWTException("不合法的token!");
                    /*jwtAuthenticationEntryPoint().commence(request, response, new AuthenticationException("不合法的token!", new JWTException("不合法的token!")) {
                    });*/
                    request.setAttribute(TOKEN_ERROR, "不合法的token!");
                    chain.doFilter(request, response);
                    return;
                }

                if (jwtUtils.isExpired(jwt)) {
//                    throw new JWTException("token已过期!");
                    /*jwtAuthenticationEntryPoint().commence(request, response, new AuthenticationException("token已过期!", new JWTException("token已过期!")) {
                    });*/
                    request.setAttribute(TOKEN_ERROR, "token已过期!");
                    chain.doFilter(request, response);
                    return;
                }
                String username = (String) jwt.getPayload("sub");

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

            Object token_error = request.getAttribute(TOKEN_ERROR);

            Result result = Result.error(401, "请先登录");
            if (!Objects.isNull(token_error)) {
                result.setMsg(String.valueOf(token_error));
            }

            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(JSONUtil.toJsonStr(result).getBytes(CharsetUtil.UTF_8));
            outputStream.flush();
            outputStream.close();
        };
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
//            Result<String> fail = Result.fail(accessDeniedException.getMessage());
            Result<String> fail = Result.ok(accessDeniedException.getMessage());

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

