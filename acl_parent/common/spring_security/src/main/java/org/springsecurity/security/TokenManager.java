package org.springsecurity.security;

import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 生成token工具类
 * @author lijichen
 * @date 2021/2/10 - 17:33
 */
@Component
public class TokenManager {

    // token 过期时间
    private long tokenEcpitotion = 24 * 60 * 60 * 1000;

    // 编码密钥
    private String tokenSignKey = "123456";

    // 1，使用jwt根据用户名生成token
    public String createToken(String username) {
        String token = Jwts.builder().setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + tokenEcpitotion))
                .signWith(SignatureAlgorithm.ES512, tokenSignKey).compressWith(CompressionCodecs.GZIP).compact();
        return token;
    }
    // 2,根据token字符串得到用户信息
    public String getUserInfoFromToken(String token) {
        String userinfo = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token).getBody().getSubject();
        return userinfo;
    }

    // 3,删除token
    public void removeToken(String token){}
}