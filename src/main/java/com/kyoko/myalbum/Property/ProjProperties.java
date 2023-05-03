package com.kyoko.myalbum.Property;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author young
 * @create 2023/5/2 14:35
 * @Description 项目自定义配置
 */
@ConfigurationProperties(prefix = "proj")
@Getter
public class ProjProperties {
    private String[] SecurityWhiteList;
    private String JwtSigningSecretKey;
    private String FileStoragePath="C://uploads//default";

    public void setFileStoragePath(String fileStoragePath) {
        FileStoragePath = fileStoragePath;
    }

    public void setJwtSigningSecretKey(String jwtSigningSecretKey) {
        JwtSigningSecretKey = jwtSigningSecretKey;
    }

    public void setSecurityWhiteList(String[] securityWhiteList) {
        this.SecurityWhiteList = securityWhiteList;
    }
}
