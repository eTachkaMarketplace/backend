package com.sellbycar.marketplace.user;

import com.sellbycar.marketplace.auth.ActivateRequest;
import com.sellbycar.marketplace.auth.JwtUtils;
import com.sellbycar.marketplace.auth.LoginRequest;
import com.sellbycar.marketplace.web.ResponseBody;
import com.sellbycar.marketplace.web.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "User Library", description = "Endpoints for managing user")
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
public class UserController {

    private final UserRequestValidator userRequestValidator;
    private final UserMapper userMapper;
    private final UserService userService;
    private final JwtUtils jwtUtils;

    @PutMapping("/me")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Update current user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<?> updateUser(@RequestBody UserDTO updatedUser) {
        String token = getTokenFromRequest();
        String emailOfUser = jwtUtils.getEmailFromJwtToken(token);

        UserDAO user = userService.updateUser(updatedUser, emailOfUser);
        UserDTO userDTO = userMapper.toDTO(user);

        return ResponseEntity.ok(userDTO);
    }

    @PutMapping(value = "/me/photo", consumes = {"multipart/form-data"})
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Update current user's photo", tags = {"User Library"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User's photo updated successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    public ResponseEntity<ResponseBody<UserDTO>> putUserPhoto(@RequestPart("image") MultipartFile image) {
        String userToken = getTokenFromRequest();
        String userEmail = jwtUtils.getEmailFromJwtToken(userToken);
        UserDAO userDAO = userService.findUserByEmailOrThrow(userEmail);
        UserDAO updatedUser = userService.updatePhoto(userDAO, image);
        return ResponseUtil.ok(userMapper.toDTO(updatedUser));
    }

    @GetMapping("/me")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Get current user", tags = {"User Library"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
    })
    public ResponseEntity<?> getUser() {
        try {
            String token = getTokenFromRequest();
            String emailOfUser = jwtUtils.getEmailFromJwtToken(token);

            UserDAO user = userService.findUserByEmailOrThrow(emailOfUser);
            UserDTO userDTO = userMapper.toDTO(user);

            return ResponseEntity.ok(userDTO);
        } catch (BadCredentialsException e) {
            return ResponseUtil.error("Not Found", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/me")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Delete current user.", tags = {"User Library"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<?> deleteUser() {
        String token = getTokenFromRequest();
        String emailOfUser = jwtUtils.getEmailFromJwtToken(token);
        UserDAO user = userService.findUserByEmailOrThrow(emailOfUser);
        userService.deleteUser(user.getId());
        return ResponseUtil.create("User deleted.", HttpStatus.OK);
    }

    @PutMapping("/password/forgot")
    @Operation(description = "Send a unique code to user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        userService.forgotPassword(request);
        return ResponseUtil.ok("Recovery link sent to your email.");
    }

    @PutMapping("/accept/code/{code}")
    @Operation(description = "Unique code for change the password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<?> acceptCode(@PathVariable("code") String code) {
        var userDTO = userService.resetUniqueCode(code);
        return ResponseUtil.create("Ok", HttpStatus.OK, userDTO);
    }

    @PutMapping("/password")
    @Operation(description = "Change user password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<?> changePassword(@RequestBody LoginRequest request) {
        if (userRequestValidator.isNotPasswordValid(request.getPassword())) {
            return ResponseUtil.error("Invalid password.", HttpStatus.BAD_REQUEST);
        }
        userService.changePassword(request);
        return ResponseUtil.ok("Password changed.");
    }

    private String getTokenFromRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }
}


