package com.sellbycar.marketplace.auth;

import com.sellbycar.marketplace.user.UserDAO;
import com.sellbycar.marketplace.user.UserDetailsImpl;
import com.sellbycar.marketplace.user.UserService;
import com.sellbycar.marketplace.util.exception.RequestException;
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
            Authentication authentication = authenticateWithRefreshToken(refreshToken);
            String accessToken = jwtUtils.generateJwtToken(authentication);
            return new JwtResponse(accessToken, null);
        }
        throw new AuthException("Invalid token");
    }

    public JwtResponse getJwtRefreshToken(@NotNull String refreshToken) {
        Authentication authentication = authenticateWithRefreshToken(refreshToken);
        String accessToken = jwtUtils.generateJwtToken(authentication);
        String newRefreshToken = jwtUtils.generateRefreshToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        refreshStorage.put(userDetails.getUsername(), newRefreshToken);
        return new JwtResponse(accessToken, newRefreshToken);
    }

    private Authentication authenticateWithRefreshToken(String refreshToken) {
        if (!jwtUtils.validateRefreshToken(refreshToken)) {
            throw RequestException.forbidden("Invalid refresh token.");
        }
        Claims claims = jwtUtils.getRefreshClaims(refreshToken);
        String login = claims.getSubject();
        String saveRefreshToken = refreshStorage.get(login);
        if (saveRefreshToken == null || !saveRefreshToken.equals(refreshToken)) {
            throw RequestException.forbidden("Invalid refresh token.");
        }
        UserDAO user = new UserDAO();
        user.setEmail(login);
        Authentication authentication = userService.userAuthentication(user);
        if (authentication == null) {
            throw RequestException.forbidden("Not allowed.");
        }
        return authentication;
    }
}

