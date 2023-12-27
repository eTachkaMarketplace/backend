package com.sellbycar.marketplace.controllers;

import com.sellbycar.marketplace.models.dto.UserDTO;
import com.sellbycar.marketplace.models.entities.User;
import com.sellbycar.marketplace.services.UserService;
import com.sellbycar.marketplace.utilities.handlers.ResponseHandler;
import com.sellbycar.marketplace.utilities.jwt.JwtUtils;
import com.sellbycar.marketplace.utilities.mapper.UserMapper;
import com.sellbycar.marketplace.utilities.payload.request.EmailRequest;
import com.sellbycar.marketplace.utilities.payload.request.LoginRequest;
import com.sellbycar.marketplace.utilities.validate.Validator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User Library", description = "Endpoints for managing user")
@CrossOrigin(origins = "*")
public class UserController {

    private final Validator validator;
    private final UserMapper userMapper;
    private final UserService userService;
    private final JwtUtils jwtUtils;


    @GetMapping("/user")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Get user by email from token", tags = {"User Library"})
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

            User user = userService.existByEmail(emailOfUser);
            UserDTO userDTO = userMapper.toDTO(user);

            return ResponseEntity.ok(userDTO);
        } catch (BadCredentialsException e) {
            return ResponseHandler.generateError("Not Found", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/user")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Update existing user by email from token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN"),
            @ApiResponse(responseCode = "404", description = "User not found")})
    public ResponseEntity<?> updateUser(@RequestBody UserDTO updatedUser) {
        try {
            String token = getTokenFromRequest();
            String emailOfUser = jwtUtils.getEmailFromJwtToken(token);

            User userForUpdate = userMapper.toEntity(updatedUser);
            User user = userService.updateUser(userForUpdate, emailOfUser);
            UserDTO userDTO = userMapper.toDTO(user);

            return ResponseEntity.ok(userDTO);
        } catch (BadCredentialsException e) {
            return ResponseHandler.generateError("Not Found", HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Delete a user by id", tags = {"User Library"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "404", description = "User not found")})
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        String token = getTokenFromRequest();
        String emailOfUser = jwtUtils.getEmailFromJwtToken(token);
        User user = userService.existByEmail(emailOfUser);
        if (user.getId().equals(id)) {
            userService.deleteUser(id);
            return ResponseHandler.generateResponse("User was deleted", HttpStatus.OK);
        }
        return ResponseHandler.generateResponse("Access denied", HttpStatus.FORBIDDEN);
    }


    @PutMapping("/forgot/password")
    @Operation(description = "Send a unique code to user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<?> forgotPassword(@RequestBody EmailRequest emailRequest) throws MessagingException {

        var forgotPassword = userService.forgotPassword(emailRequest);

        return ResponseHandler.generateResponse("Check your email", HttpStatus.OK, forgotPassword);
    }

    @PutMapping("/accept/code/{code}")
    @Operation(description = "Unique code for change the password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<?> acceptCode(@PathVariable("code") String code) {
        var userDTO = userService.acceptCode(code);
        return ResponseHandler.generateResponse("Ok", HttpStatus.OK, userDTO);
    }

    @PutMapping("/change/password")
    @Operation(description = "Change user password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<?> changePassword(@RequestBody LoginRequest request) {
        if (!validator.isPasswordValid(request.getPassword())) return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Invalid password");
        var changePassword = userService.changePassword(request);
        return ResponseHandler.generateResponse("Ok", HttpStatus.OK, changePassword);
    }

    private String getTokenFromRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder
                .getRequestAttributes())).getRequest();
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }

}


