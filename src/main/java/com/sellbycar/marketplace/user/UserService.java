package com.sellbycar.marketplace.user;

import com.sellbycar.marketplace.auth.LoginRequest;
import com.sellbycar.marketplace.auth.SignupRequest;
import jakarta.mail.MessagingException;
import org.springframework.security.core.Authentication;

public interface UserService {

    /**
     * Registers a new user in the system.
     *
     * @param signUpRequest DTO containing all required data for user registration.
     * @return true if the registration is successful, false otherwise.
     * @throws UserDataException if there is an issue during the registration process.
     */

    void createNewUser(SignupRequest signUpRequest) throws MessagingException;

    /**
     * Checks whether the user exists in the database by email.
     *
     * @param email Email address of the user to check.
     * @return User object representing the user if found, null otherwise.
     */

    UserDAO findUserByEmailOrThrow(String email);

    /**
     * Updates an existing user with new data.
     *
     * @param updatedUser            New data for the update.
     * @param emailOfUserForUpdating Email in order to get user, who needs to apply changes.
     * @return User object representing the updated user.
     */
    UserDAO updateUser(UserDTO updatedUser, String emailOfUserForUpdating);

    /**
     * Removes the selected user.
     *
     * @param id ID of the existing user.
     * @return true if the deletion is successful, false otherwise.
     */
    boolean deleteUser(long id);

    /**
     * Authenticates a user based on the provided User object.
     *
     * @param user The User object containing information for authentication.
     * @return Authentication object representing the authenticated user.
     */
    /**
     * Authenticates a user based on the provided User object.
     *
     * @param user The User object containing information for authentication.
     * @return Authentication object representing the authenticated user.
     */
    Authentication userAuthentication(UserDAO user);

    /**
     * Initiates the process of resetting the user's password and sends a reset password link via email.
     *
     * @param request EmailRequest containing the user's email address.
     */
    void forgotPassword(ForgotPasswordRequest request);

    /**
     * Verifies the provided unique code for password reset and retrieves the associated user.
     *
     * @param uniqueCode Unique code sent to the user for password reset.
     * @return User object representing the user associated with the unique code.
     */
    UserDTO resetUniqueCode(String uniqueCode);

    /**
     * Changes the user's password based on the provided login request.
     *
     * @param request LoginRequest containing the user's email, new password, and confirmation code.
     */
    void changePassword(LoginRequest request);

    /**
     * Retrieves the currently authenticated user from the security context.
     *
     * @return User object representing the authenticated user.
     */
    UserDAO getUserFromSecurityContextHolder();

    /**
     * Activates a user using the provided activation code.
     *
     * @param uniqueCode The unique activation code to identify and activate the user.
     * @throws UserDataException If the user is not found with the specified activation code.
     */
    void activateUser(String uniqueCode);
}
