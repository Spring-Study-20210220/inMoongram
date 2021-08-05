package com.team.security.jwt;

import com.team.auth.CustomUserDetailsService;
import com.team.config.AppProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;

@Slf4j
@Component
public class TokenProvider implements InitializingBean {
    public static final long ACCESS_TOKEN_VALID_TIME = 1 * 60 * 30 * 1000L; // 30 minutes
    public static final long REFRESH_TOKEN_VALID_TIME = 1 * 60 * 60 * 24 * 14 * 1000L; // 2 weeks
    private final CustomUserDetailsService customUserDetailsService;
    private final AppProperties properties;

    private final String secret;
    private Key key;

    public String getSecret() {
        return properties.getAuth().getSecret();
    }

    @PostConstruct
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes = Decoders.BASE64.decode(getSecret());
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(Authentication authentication) {
        return generateToken(authentication, ACCESS_TOKEN_VALID_TIME);
    }

    public String createRefreshToken(Authentication authentication) {
        return generateToken(authentication, REFRESH_TOKEN_VALID_TIME);
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaimsJws(token).getBody();
        User principle = new User(claims.getSubject(), "", new ArrayList<>());
        return new UsernamePasswordAuthenticationToken(principle, token, new ArrayList<>());
    }

    public String getUsername(String token) {
        return parseClaimsJws(token)
                .getBody()
                .get("email", String.class);
    }

    public boolean validateToken(String token) {
        try {
            parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
            throw e;
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    private Jws<Claims> parseClaimsJws(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }

    private String generateToken(Authentication authentication, long expireTime) {
        long now = (new Date()).getTime();
        Date expiration = new Date(now + expireTime);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("email", userDetails.getUsername())
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(expiration)
                .compact();
        log.info("createToken: {}, expiration: {}", token, expiration);
        return token;
    }
}
