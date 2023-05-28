//package com.leaf.auth.serurity.provider;
//
//import org.springframework.security.oauth2.provider.ClientDetails;
//import org.springframework.security.oauth2.provider.ClientDetailsService;
//import org.springframework.security.oauth2.provider.client.BaseClientDetails;
//import org.springframework.stereotype.Service;
//
//@Service
//public class ClientDetailServiceImpl implements ClientDetailsService {
//    @Override
//    public ClientDetails loadClientByClientId(String clientId) {
//
//        BaseClientDetails clientDetails = new BaseClientDetails(clientId, "", "all", "password,client_credentials,captcha,refresh_token,authorization_code", "admin");
//        clientDetails.setAccessTokenValiditySeconds(1800);
//        clientDetails.setRefreshTokenValiditySeconds(3600);
//        clientDetails.setClientSecret("123123");
//        return clientDetails;
//    }
//}
