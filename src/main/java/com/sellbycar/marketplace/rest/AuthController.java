package com.sellbycar.marketplace.rest;

import com.sellbycar.marketplace.service.impl.UserDetailsImpl;
import com.sellbycar.marketplace.service.UserService;
import com.sellbycar.marketplace.service.jwt.JwtUtils;
import com.sellbycar.marketplace.rest.payload.request.LoginRequest;
import com.sellbycar.marketplace.rest.payload.request.SignupRequest;
import com.sellbycar.marketplace.rest.payload.response.JwtResponse;
import com.sellbycar.marketplace.rest.payload.response.MessageResponse;
import com.sellbycar.marketplace.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.security.auth.message.AuthException;
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
@Tag(name = "User Registration Library", description = "Endpoints for register user")
public class AuthController {

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Login User")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String jwtAccessToken = jwtUtils.generateJwtToken(authentication);
        final String jwtRefreshToken = jwtUtils.generateRefreshToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        authService.saveJwtRefreshToken(userDetails.getUsername(), jwtRefreshToken);

        return ResponseEntity.ok(new JwtResponse(jwtAccessToken, jwtRefreshToken));
    }

    @PostMapping("/signup")
    @Operation(summary = "Register User")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {
        if (userService.createUser(signUpRequest)) {
            return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
        }
        return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
    }

    @PostMapping("/refresh/access-token")
    @Operation(summary = "Refresh jwt access token")
    @ApiResponses({@ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = JwtResponse.class), mediaType = "application/json")}),})
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<JwtResponse> getNewAccessToken(@RequestBody JwtResponse response) throws AuthException {
        final JwtResponse token = authService.getAccessToken(response.getJwtRefreshToken());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/refresh/refresh-token")
    @Operation(summary = "Refresh jwt refresh token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = JwtResponse.class), mediaType = "application/json") }),
    })
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<JwtResponse> getNewRefreshToken(@RequestBody JwtResponse response) throws AuthException {
        final JwtResponse token = authService.getJwtRefreshToken(response.getJwtRefreshToken());
        return ResponseEntity.ok(token);
    }
}
