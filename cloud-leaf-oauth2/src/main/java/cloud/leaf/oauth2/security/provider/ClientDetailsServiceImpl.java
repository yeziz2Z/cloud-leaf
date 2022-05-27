package cloud.leaf.oauth2.security.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Component;

@Component
public class ClientDetailsServiceImpl implements ClientDetailsService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        BaseClientDetails details = new BaseClientDetails(clientId, "", "all", "password,client_credentials,captcha,refresh_token,authorization_code", null);
        details.setAccessTokenValiditySeconds(1800);
        details.setRefreshTokenValiditySeconds(3600);
        details.setClientSecret(passwordEncoder.encode("889527"));
        return details;
    }
}
