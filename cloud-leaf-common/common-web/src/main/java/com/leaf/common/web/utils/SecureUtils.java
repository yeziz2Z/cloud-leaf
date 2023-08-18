package com.leaf.common.web.utils;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import jakarta.annotation.PostConstruct;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * @author liuk
 */
@Component
@ConditionalOnProperty(name = "cloud.leaf.secure.rsa", matchIfMissing = true)
@ConfigurationProperties(prefix = "cloud.leaf.secure.rsa")
@Slf4j
public class SecureUtils {
    @Setter
    private String publicKey;

    @Setter
    private String privateKey;

    private RSA decodeRsa;

    private RSA encodeRsa;

    @PostConstruct
    private void init() {
        this.decodeRsa = SecureUtil.rsa(privateKey, null);
        this.encodeRsa = SecureUtil.rsa(null, publicKey);

        log.info("================= publicKey: [{}]", publicKey);
        log.info("================= privateKey: [{}]", privateKey);
    }

    public String decode(String text) {
        return this.decodeRsa.decryptStr(text, KeyType.PrivateKey);
    }


    public String encode(String text) {
        return new String(this.encodeRsa.encrypt(text, KeyType.PublicKey), StandardCharsets.UTF_8);
    }
}
