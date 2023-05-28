//package com.leaf.oauth2.security.provider;
//
//import com.leaf.admin.api.OauthClientFeignClient;
//import com.leaf.common.pojo.auth.Oauth2Client;
//import com.leaf.common.result.Result;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.oauth2.provider.ClientDetails;
//import org.springframework.security.oauth2.provider.ClientDetailsService;
//import org.springframework.security.oauth2.provider.ClientRegistrationException;
//import org.springframework.security.oauth2.provider.NoSuchClientException;
//import org.springframework.security.oauth2.provider.client.BaseClientDetails;
//import org.springframework.stereotype.Component;
//
//@Component
//public class ClientDetailsServiceImpl implements ClientDetailsService {
//
//    @Autowired
//    PasswordEncoder passwordEncoder;
//    @Autowired
//    OauthClientFeignClient oauthClientFeignClient;
//
//    @Override
//    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
//
//        Result<Oauth2Client> result = oauthClientFeignClient.getOauth2ClientById(clientId);
//        if (result.getCode() == Result.success().getCode()) {
//            Oauth2Client client = result.getData();
//            BaseClientDetails details = new BaseClientDetails(client.getClientId(),
//                    client.getResourcesIds(),
//                    client.getScope(),
//                    client.getAuthorizedGrantTypes(),
//                    client.getAuthorities(),
//                    client.getWebServerRedirectUri());
//            details.setAccessTokenValiditySeconds(client.getAccessTokenValidity());
//            details.setRefreshTokenValiditySeconds(client.getRefreshTokenValidity());
//            details.setClientSecret(passwordEncoder.encode(client.getClientSecret()));
//            return details;
//        }
//        throw new NoSuchClientException("No client with requested id:" + clientId);
//
//    }
//}
