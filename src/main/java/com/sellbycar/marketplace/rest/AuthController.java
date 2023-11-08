package com.sellbycar.marketplace.rest;

import com.sellbycar.marketplace.config.UserDetailsConfig;
import com.sellbycar.marketplace.payload.jwt.JwtUtils;
import com.sellbycar.marketplace.payload.request.LoginRequest;
import com.sellbycar.marketplace.payload.request.SignupRequest;
import com.sellbycar.marketplace.payload.response.JwtResponse;
import com.sellbycar.marketplace.payload.response.MessageResponse;
import com.sellbycar.marketplace.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    @Operation(summary = "Login User")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String jwtAccessToken = jwtUtils.generateJwtToken(authentication);
        final String jwtRefreshToken = jwtUtils.generateRefreshToken(authentication);

        UserDetailsConfig userDetails = (UserDetailsConfig) authentication.getPrincipal();

        return ResponseEntity.ok(new JwtResponse(jwtAccessToken, jwtRefreshToken));
    }

    @PostMapping("/signup")
    @Operation(summary = "Register User")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {
        if (userService.createUser(signUpRequest)) {
            return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
        } else return ResponseEntity
                .badRequest()
                .body(new MessageResponse("Error: Email is already in use!"));
    }

}
