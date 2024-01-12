package com.sellbycar.marketplace.auth;

import com.sellbycar.marketplace.user.UserService;
import com.sellbycar.marketplace.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.security.auth.message.AuthException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
@Tag(name = "Authentication Registration Library", description = "Endpoints for authentication business logic")
@CrossOrigin(origins = "*")
@Slf4j
public class AuthController {

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Login User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    public ResponseEntity<?> authenticateUser(
            @Valid @RequestBody LoginRequest loginRequest,
            @RequestParam(name = "rememberMe", defaultValue = "false") boolean rememberMe
    ) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwtAccessToken = jwtUtils.generateJwtToken(authentication);
        String jwtRefreshToken = null;

        if (rememberMe) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            jwtRefreshToken = jwtUtils.generateRefreshToken(authentication);
            authService.saveJwtRefreshToken(userDetails.getUsername(), jwtRefreshToken);
        }

        return ResponseUtil.ok(new JwtResponse(jwtAccessToken, jwtRefreshToken));
    }


    @PostMapping("/signup")
    @Operation(summary = "Register User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "409", description = "CONFLICT")
    })
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) throws MessagingException {
        userService.createNewUser(signUpRequest);
        return ResponseUtil.ok("User registered successfully!");
    }

    @PostMapping("/refresh/access-token")
    @Operation(summary = "Refresh jwt access token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = JwtResponse.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<?> getNewAccessToken(@RequestBody JwtResponse response) throws AuthException {
        final JwtResponse token = authService.getJwtAccessToken(response.getJwtRefreshToken());
        return ResponseUtil.ok("Access Token", token);
    }

    @PostMapping("/refresh/refresh-token")
    @Operation(summary = "Refresh jwt refresh token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = JwtResponse.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<?> getNewRefreshToken(@RequestBody JwtResponse response) throws AuthException {
        final JwtResponse token = authService.getJwtRefreshToken(response.getJwtRefreshToken());
        return ResponseUtil.create("Refresh token", HttpStatus.OK, token);
    }

    @PostMapping("/activate/{code}")
    @Operation(summary = "Activate user with activation code")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Ok",
                    content = @Content(schema = @Schema(implementation = ActivateRequest.class))
            ),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    public ResponseEntity<?> activateUser(@RequestBody ActivateRequest request) {
        userService.activateUser(request.getCode());
        return ResponseUtil.ok("User activated successfully!");
    }
}
