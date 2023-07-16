package com.leaf.oauth2.security.userdetails;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface OAuth2ClientUserDetailsService extends UserDetailsService {

    boolean support(String clientId);

}
