package com.kyoko.myalbum.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

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
    //jwtToken时效，秒为单位
    private long JwtTokenExpiration = 60*60*24;
    //注册验证token时效，分为单位
    private long ConfirmTokenExpiration = 15;
    @Value("${spring.mail.username}")
    private String MyEmail;

    public void setConfirmTokenExpiration(long confirmTokenExpiration) {
        ConfirmTokenExpiration = confirmTokenExpiration;
    }

    public void setJwtTokenExpiration(long jwtTokenExpiration) {
        JwtTokenExpiration = jwtTokenExpiration;
    }

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
