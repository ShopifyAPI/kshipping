package com.shopify.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import lombok.Data;
 
@Data
@ConfigurationProperties(prefix="file")
public class FileUploadConfig {
    private String uploadDir;

}
