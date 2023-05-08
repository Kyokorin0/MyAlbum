package com.kyoko.myalbum.Filter.Util;

import com.kyoko.myalbum.Property.ProjProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author young
 * @create 2023/3/14 2:00
 * @Description Json Web Token Service 由jjwt支持
 * JWT Components: Header -- Payload -- Signature 标头 负载 签名
 * Header: 类型和加密算法
 * {
 * "alg": "HS256",
 * "typ": "JWT"
 * }
 * Payload: 通常是用户实体、授权的角色、时间、一些其他声明等等
 * {
 * "sub": "1234567890",
 * "name": "John Doe",
 * "iat": 1516239022
 * }
 * Signature: 验证签名防止篡改
 */

@Service//交由Spring容器管理，提供JwtToken解析服务
public class JwtTokenUtil {
    //JwtSigningSecretKey，可以在application.yml中设置
    private final String SECRET_KEY;
    private final long TOKEN_EXPIRATION;

    @Autowired
    public JwtTokenUtil(ProjProperties projProperties) {
        SECRET_KEY = projProperties.getJwtSigningSecretKey();
        TOKEN_EXPIRATION = projProperties.getTokenExpiration();
    }




    //验证token是否有效，包括是否与userDetails信息一致，是否过期
    public boolean isTokenValid(String jwtToken, UserDetails userDetails) throws Exception {
        final String username = extractUsername(jwtToken);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(jwtToken);//注意这里isTokenExpired取反
    }

    //验证Expiration日期是否在当前时间之前，false未过期，true过期
    private boolean isTokenExpired(String jwtToken) throws Exception {
        return extractExpiration(jwtToken).before(new Date());//Date()默认返回当前系统时间
    }

    private Date extractExpiration(String jwtToken) throws Exception {
        return extractClaim(jwtToken, Claims::getExpiration);//相当于调用Claims.getExpiration()?
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    //生成token
    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())//设置账户名
                .setIssuedAt(new Date(System.currentTimeMillis()))//设置声明时间
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION*1000))//设置令牌时效，这里是24小时+1秒
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)//设置签名密钥和对应算法
                .compact();
    }

    //解析username
    public String extractUsername(String jwtToken) throws Exception {
        return extractClaim(jwtToken, Claims::getSubject);//相当于调用claims.getSubject()方法？
    }

    public <T> T extractClaim(String jwtToken, Function<Claims, T> claimsResolver) throws Exception {
        final Claims claims = extractAllClaims(jwtToken);
        return claimsResolver.apply(claims);
    }

    //解析所有声明
    private Claims extractAllClaims(String jwtToken) throws Exception {
        Claims claims;
        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())//获取并配置签名密钥到解析器
                    .build()
                    .parseClaimsJws(jwtToken)//抛出多种异常，由JwtFilter统一处理
                    //解析到声明
                    .getBody();
        } catch (ExpiredJwtException e) {
            claims = e.getClaims();
        } catch (Exception e) {
            throw e;
        }
        return claims;
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
