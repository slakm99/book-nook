package com.booknook.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.booknook.common.LoginUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/** JWT 工具类。 */
@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expire-hours}")
    private int expireHours;

    public String createToken(LoginUser user) {
        Date now = new Date();
        Date expires = new Date(now.getTime() + expireHours * 3600_000L);
        return JWT.create()
                .withSubject(user.getUsername())
                .withClaim("userId", user.getUserId())
                .withClaim("role", user.getRole())
                .withClaim("refId", user.getRefId())
                .withIssuedAt(now)
                .withExpiresAt(expires)
                .sign(Algorithm.HMAC256(secret));
    }

    public LoginUser parse(String token) {
        DecodedJWT jwt = JWT.require(Algorithm.HMAC256(secret)).build().verify(token);
        return new LoginUser(
                jwt.getClaim("userId").asLong(),
                jwt.getSubject(),
                jwt.getClaim("role").asString(),
                jwt.getClaim("refId").asLong()
        );
    }
}
