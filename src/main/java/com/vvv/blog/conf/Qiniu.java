package com.vvv.blog.conf;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "qiniu")
public class Qiniu {
    private String endpoint;
    private String ak;
    private String sk;
    private String bucketName;
}
