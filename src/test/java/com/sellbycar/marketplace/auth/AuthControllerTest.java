package com.sellbycar.marketplace.auth;

import com.sellbycar.marketplace.user.UserService;
import com.sellbycar.marketplace.util.ConfiguredSpringBootTest;
import com.sellbycar.marketplace.web.ResponseBody;
import com.sellbycar.marketplace.web.ResponseUtil;
import jakarta.mail.MessagingException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthControllerTest extends ConfiguredSpringBootTest {
    @Mock
    private UserService userService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private AuthService authService;
    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void authenticateUser() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("test");

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        UserDetails userDetails = mock(UserDetails.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("test@example.com");

        when(jwtUtils.generateJwtToken(any(Authentication.class))).thenReturn("fakeAccessToken");
        when(jwtUtils.generateJwtToken(any(Authentication.class))).thenReturn("fakeRefreshToken");

        doNothing().when(authService).saveJwtRefreshToken(anyString(), anyString());

        ResponseEntity<?> response = authController.authenticateUser(loginRequest, false);


        assertNotNull(response);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ResponseBody);

    }

    @Test
    @SneakyThrows
    void registerUser() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("user@test.com");
        signupRequest.setName("test");
        signupRequest.setPassword("test");

        doNothing().when(userService).createNewUser(any(SignupRequest.class));

        ResponseEntity<?> response = authController.registerUser(signupRequest);

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ResponseBody);
        assertEquals("User registered successfully!", ((ResponseBody) response.getBody()).getMessage());
    }

    @Test
    @SneakyThrows
    void registerUserShouldThrowMessagingException() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("user@test.com");
        signupRequest.setName("test");
        signupRequest.setPassword("test");

        doThrow(MessagingException.class).when(userService).createNewUser(signupRequest);

        assertThrows(MessagingException.class, () -> {
            authController.registerUser(signupRequest);
        });
    }

    @Test
    @SneakyThrows
    void getNewAccessToken() {
        JwtResponse response = new JwtResponse("fakeAccessToken", "fakeRefreshToken");

        JwtResponse token = new JwtResponse("newAccessToken", "newRefreshToken");
        when(authService.getJwtAccessToken(anyString())).thenReturn(token);

        ResponseEntity<?> result = authController.getNewAccessToken(response);

        assertEquals(ResponseUtil.ok("Access Token", token), result);
    }

    @Test
    @SneakyThrows
    void getNewRefreshToken() {
        JwtResponse jwtResponse = new JwtResponse("fakeAccessToken", "fakeRefreshToken");

        JwtResponse token = new JwtResponse("newAccessToken", "newRefreshToken");

        when(authService.getJwtRefreshToken(anyString())).thenReturn(token);

        ResponseEntity<?> result = authController.getNewRefreshToken(jwtResponse);

        assertEquals(ResponseUtil.ok("Refresh token", token), result);
    }

    @Test
    void activateUser() {
        String code = "activationCode123";

        ResponseEntity<?> result = authController.activateUser(code);

        assertEquals(ResponseUtil.ok("User activated successfully!"), result);
    }
}