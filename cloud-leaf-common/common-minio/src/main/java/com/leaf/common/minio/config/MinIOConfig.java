package com.leaf.common.minio.config;

import io.minio.MinioClient;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liuk
 */
@Configuration
@ConfigurationProperties(prefix = "leaf.minio")
public class MinIOConfig {
    @Setter
    private String endpoint;

    @Setter
    private String accessKey;

    @Setter
    private String secretKey;

    @Setter
    private String bucket;


    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }
}
