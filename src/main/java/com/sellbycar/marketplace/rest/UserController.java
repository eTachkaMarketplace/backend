package com.sellbycar.marketplace.rest;

import com.sellbycar.marketplace.repository.model.User;
import com.sellbycar.marketplace.rest.dto.UserDTO;
import com.sellbycar.marketplace.rest.exception.CustomUserException;
import com.sellbycar.marketplace.rest.payload.request.EmailRequest;
import com.sellbycar.marketplace.rest.payload.request.LoginRequest;
import com.sellbycar.marketplace.service.UserService;
import com.sellbycar.marketplace.service.impl.UserDetailsImpl;
import com.sellbycar.marketplace.service.jwt.JwtUtils;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "User Library", description = "Endpoints for managing user")
//@CrossOrigin(origins = "https://yura-platonov.github.io")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;
    private final JwtUtils jwtUtils;

    private String getTokenFromRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder
                .getRequestAttributes())).getRequest();
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }

    @GetMapping("/users/info")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Get user by ID", description = "Retrieve a user by their ID", tags = {"User Library"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
    })
    public ResponseEntity<UserDTO> getUser() {
        try {
            String token = getTokenFromRequest();
            String username = jwtUtils.getUserNameFromJwtToken(token);

            User user = userService.existByEmail(username);

            if (user != null) {
                UserDTO userDTO = userToUserDTO(user);
                return ResponseEntity.ok(userDTO);
            } else {
                throw new CustomUserException("User not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private UserDTO userToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhone(user.getPhone());
        return userDTO;
    }


    @PutMapping("/users/user/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Change existing user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN"),
            @ApiResponse(responseCode = "404", description = "User not found")})
    public ResponseEntity<User> updateUser(@RequestBody UserDTO updatedUser, @PathVariable Long id) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetailsImpl userPrincipal) {
            if (userPrincipal.getId().equals(id)) {
                User existingUser = userService.findUser(id);
                if (existingUser != null) {
                    existingUser.setFirstName(updatedUser.getFirstName());
                    userService.updateUser(existingUser);

                    return ResponseEntity.ok(existingUser);
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                }
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
        } else {
            throw new CustomUserException("Unauthorized");
        }
    }


    @DeleteMapping("/users/user/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Delete a user", description = "Delete an existing user by ID", tags = {"User Library"})
    @ApiResponse(responseCode = "200", description = "User deleted successfully")
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetailsImpl userPrincipal) {
            if (userPrincipal.getId().equals(id)) {
                userService.deleteUser(id);
                return ResponseEntity.ok("User with id = " + id + " was deleted");
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");

        }
        throw new CustomUserException("User with id: " + id + " does not exists in database");
    }

    @PutMapping("/forgot/password")
    public ResponseEntity<?> forgotPassword(@RequestBody EmailRequest emailRequest) throws MessagingException {

        return ResponseEntity.ok(userService.forgotPassword(emailRequest));
    }

    @PutMapping("/accept/code/{code}")
    public ResponseEntity<User> acceptCode(@PathVariable("code") String code) {
        return ResponseEntity.ok(userService.acceptCode(code));
    }

    @PutMapping("/change/password")
    public ResponseEntity<String> changePassword(@RequestBody LoginRequest request) {
        if (!userService.isPasswordValid(request.getPassword())) return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Invalid password");
        return ResponseEntity.ok(userService.changePassword(request));
    }

}


