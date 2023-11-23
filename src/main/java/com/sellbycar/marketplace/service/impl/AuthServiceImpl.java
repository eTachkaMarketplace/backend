package com.sellbycar.marketplace.service.impl;

import com.sellbycar.marketplace.persistance.model.User;
import com.sellbycar.marketplace.rest.payload.response.JwtResponse;
import com.sellbycar.marketplace.service.AuthService;
import com.sellbycar.marketplace.service.UserService;
import com.sellbycar.marketplace.service.jwt.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.security.auth.message.AuthException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtUtils jwtUtils;

    private final UserService userService;
    private final Map<String, String> refreshStorage = new HashMap<>();

    public void saveJwtRefreshToken(String email, String jwtRefreshToken) {
        refreshStorage.put(email, jwtRefreshToken);
    }

    public JwtResponse getJwtAccessToken(@NotNull String refreshToken) throws AuthException {
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

    public JwtResponse getJwtRefreshToken(@NotNull String refreshToken) throws AuthException {
        if (jwtUtils.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtUtils.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final Authentication authentication = userService.userAuthentication(new User(login));
                if (authentication == null) throw new AuthException("User authentication error");

                final String accessToken = jwtUtils.generateJwtToken(authentication);
                final String newRefreshToken = jwtUtils.generateRefreshToken(authentication);
                UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
                refreshStorage.put(userDetails.getUsername(), newRefreshToken);
                return new JwtResponse(accessToken, newRefreshToken);
            }
        }
        throw new AuthException("Invalid JWT token");
    }
}

