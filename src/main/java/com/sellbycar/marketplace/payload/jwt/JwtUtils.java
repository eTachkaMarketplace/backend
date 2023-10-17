package com.sellbycar.marketplace.payload.jwt;

import com.sellbycar.marketplace.config.UserDetailsConfig;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value(("${jwt.secret.access.expirationMs}"))
    private int jwtAccessExpirationMs;

    @Value(("${jwt.secret.refresh.expirationMs}"))
    private long jwtRefreshExpirationMs;

    private final Key jwtAccessSecret;
    private final SecretKey jwtRefreshSecret;

    public JwtUtils(
            @Value(("${jwt.secret.access}")) String jwtAccessSecret,
            @Value(("${jwt.secret.refresh}")) String jwtRefreshSecret
    ) {
        this.jwtAccessSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtAccessSecret));
        this.jwtRefreshSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtRefreshSecret));
    }


    public String generateJwtToken(Authentication authentication) {

        UserDetailsConfig userPrincipal = (UserDetailsConfig) authentication.getPrincipal();
        final Date expiration = new Date(new Date().getTime() + jwtAccessExpirationMs);

        return Jwts.builder()
                .subject((userPrincipal.getUsername()))
                .issuedAt(new Date())
                .expiration(expiration)
                .signWith(jwtAccessSecret, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(@NonNull Authentication authentication) {
        UserDetailsConfig userPrincipal = (UserDetailsConfig) authentication.getPrincipal();
        final Date expiration = new Date(new Date().getTime() + jwtRefreshExpirationMs);
        return Jwts.builder()
                .subject((userPrincipal.getUsername()))
                .expiration(expiration)
                .signWith(jwtRefreshSecret, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtAccessSecret).build()
                .parseClaimsJws(token).getBody().getSubject();
    }


    public boolean validateRefreshToken(@NonNull String refreshToken) {
        return validateJwtToken(refreshToken, jwtRefreshSecret);
    }

    public boolean validateAccessToken(@NonNull String accessToken) {
        return validateJwtToken(accessToken, jwtAccessSecret);
    }

    public boolean validateJwtToken(@NonNull String token, @NonNull Key secret) {
        try {
            Jwts.parser().setSigningKey(secret).build().parse(token);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }


}
