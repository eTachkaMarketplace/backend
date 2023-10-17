package com.sellbycar.marketplace.rest;

import com.sellbycar.marketplace.payload.request.SignupRequest;
import com.sellbycar.marketplace.repository.model.User;
import com.sellbycar.marketplace.rest.exception.CustomUserException;
import com.sellbycar.marketplace.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User Library", description = "Endpoints for managing user")
public class UserController {

    private final UserService userService;
//
//
//    @GetMapping("/")
//    public ResponseEntity<String> loginPage() {
//
//        return ResponseEntity.ok("Login page");
//    }


    @GetMapping("/user/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieve a user by their ID", tags = {"User Library"})
    @ApiResponse(
            responseCode = "200",
            description = "User retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = User.class)
            )
    )
    public ResponseEntity<User> getUser(@PathVariable Long id) {

        User user = userService.getUser(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            throw new CustomUserException("User with id: " + id + " not found");
        }
    }

    @PostMapping("/user")
    @Operation(summary = "Create a new user", description = "Create a new user", tags = {"User Library"})
    @ApiResponse(
            responseCode = "201",
            description = "User created successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = User.class)
            )
    )
    @ApiResponse(responseCode = "400", description = "Bad Request")
    public ResponseEntity<String> createUser(@RequestBody SignupRequest signupRequest) {
        if (userService.isEmailAlreadyExists(signupRequest.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("There is email: " + signupRequest.getEmail() + " exist in database");
        }
        userService.createUser(signupRequest);
        return ResponseEntity.ok("You a created account");

    }

    @PutMapping("/user")
    @Operation(summary = "Update a user", description = "Update an existing user", tags = {"User Library"})
    @ApiResponse(
            responseCode = "200",
            description = "User updated successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = User.class)
            )
    )
    @ApiResponse(responseCode = "400", description = "Bad Request")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        userService.updateUser(user);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/user/{id}")
    @Operation(summary = "Delete a user", description = "Delete an existing user by ID", tags = {"User Library"})
    @ApiResponse(responseCode = "200", description = "User deleted successfully")
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        if (userService.deleteUser(id)) {
            return ResponseEntity.ok("User with id = " + id + " was deleted");
        } else {
            throw new CustomUserException("User with id: " + id + " does not exists in database");
        }
    }

}
