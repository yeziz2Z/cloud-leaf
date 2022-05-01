package com.leaf.file.config.prop;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ToString
@ConfigurationProperties(prefix = "leaf.minio")
public class MinioProp {

    private String endpoint;

    private String accessKey;

    private String secretKey;

    private String bucket;

}
