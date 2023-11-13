package com.sellbycar.marketplace.rest;

import com.sellbycar.marketplace.rest.dto.UserDTO;
import com.sellbycar.marketplace.service.impl.UserDetailsImpl;
import com.sellbycar.marketplace.service.UserService;
import com.sellbycar.marketplace.service.jwt.JwtUtils;
import com.sellbycar.marketplace.repository.model.User;
import com.sellbycar.marketplace.rest.exception.CustomUserException;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User Library", description = "Endpoints for managing user")
public class UserController {

    private final UserService userService;
    private final JwtUtils jwtUtils;

    private String getTokenFromRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }

    @GetMapping("/info")
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

            User user = userService.getUserByEmail(username);

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


//    @PostMapping("/user")
//    @SecurityRequirement(name = "Bearer Authentication")
//    @Operation(summary = "Create a new user", description = "Create a new user", tags = {"User Library"})
//    @ApiResponse(
//            responseCode = "201",
//            description = "User created successfully",
//            content = @Content(
//                    mediaType = "application/json",
//                    schema = @Schema(implementation = User.class)
//            )
//    )
//    @ApiResponse(responseCode = "400", description = "Bad Request")
//    public ResponseEntity<String> createUser(@RequestBody SignupRequest signupRequest) {
//        if (userService.isEmailAlreadyExists(signupRequest.getEmail())) {
//            return ResponseEntity.status(HttpStatus.CONFLICT).body("There is email: " + signupRequest.getEmail() + " exist in database");
//        }
//        userService.createUser(signupRequest);
//        return ResponseEntity.ok("You a created account");
//
//    }

//    @PutMapping("/user/{id}")
//    @SecurityRequirement(name = "Bearer Authentication")
//    @Operation(summary = "Change existing user")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "OK"),
//            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED")})
//    public ResponseEntity<User> updateUser(@RequestBody User user, @PathVariable Long id) {
//
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        if (principal instanceof UserDetailsConfig userPrincipal) {
//            if (userPrincipal.getId().equals(id)) {
//                userService.updateUser(user);
//            }
//            return ResponseEntity.ok(user);
//        }
//        throw new CustomUserException("User with id: " + id + " does not exists in database");
//    }

    @PutMapping("/user/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Change existing user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN"),
            @ApiResponse(responseCode = "404", description = "User not found")})
    public ResponseEntity<User> updateUser(@RequestBody UserDTO updatedUser, @PathVariable Long id) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetailsImpl) {
            UserDetailsImpl userPrincipal = (UserDetailsImpl) principal;
            if (userPrincipal.getId().equals(id)) {
                User existingUser = userService.getUser(id);
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


    @DeleteMapping("/user/{id}")
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

//                throw new CustomUserException("User with id: " + id + " does not exists in database");
        }
//        if (userService.deleteUser(id)) {
//            return ResponseEntity.ok("User with id = " + id + " was deleted");
//        } else {
        throw new CustomUserException("User with id: " + id + " does not exists in database");
    }

}


