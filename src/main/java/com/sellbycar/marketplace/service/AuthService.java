package com.sellbycar.marketplace.service;

import com.sellbycar.marketplace.config.UserDetailsConfig;
import com.sellbycar.marketplace.payload.jwt.JwtUtils;
import com.sellbycar.marketplace.payload.response.JwtResponse;
import com.sellbycar.marketplace.repository.model.User;
import io.jsonwebtoken.Claims;
import jakarta.security.auth.message.AuthException;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtils jwtUtils;

    private final UserService userService;
    private final Map<String, String> refreshStorage = new HashMap<>();

    public void saveJwtRefreshToken(String email, String jwtRefreshToken) {
        refreshStorage.put(email, jwtRefreshToken);
    }

    public JwtResponse getAccessToken(@NotNull String refreshToken) throws AuthException {
        if (jwtUtils.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtUtils.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final Authentication authentication = userService.userAuthentication(new User(login));
                if (authentication == null) throw new AuthException("User authentication error");

                final String accessToken = jwtUtils.generateJwtToken(authentication);

                return new JwtResponse(accessToken, null);
            }
        }
        throw  new AuthException("Invalid Token");
    }

    public JwtResponse getJwtRefreshToken(@NonNull String refreshToken) throws AuthException {
        if (jwtUtils.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtUtils.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final Authentication authentication = userService.userAuthentication(new User(login));
                if (authentication == null) throw new AuthException("User authentication error");

                final String accessToken = jwtUtils.generateJwtToken(authentication);
                final String newRefreshToken = jwtUtils.generateRefreshToken(authentication);
                UserDetailsConfig userDetails = (UserDetailsConfig) authentication.getPrincipal();
                refreshStorage.put(userDetails.getUsername(), newRefreshToken);
                return new JwtResponse(accessToken, newRefreshToken);
            }
        }
        throw new AuthException("Invalid JWT token");
    }
}

