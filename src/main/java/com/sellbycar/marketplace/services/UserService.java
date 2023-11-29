package com.sellbycar.marketplace.services;

import com.sellbycar.marketplace.models.entities.User;
import com.sellbycar.marketplace.utilities.exception.UserDataException;
import com.sellbycar.marketplace.utilities.payload.request.EmailRequest;
import com.sellbycar.marketplace.utilities.payload.request.LoginRequest;
import com.sellbycar.marketplace.utilities.payload.request.SignupRequest;
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

    boolean createNewUser(SignupRequest signUpRequest) throws MessagingException;

    /**
     * Checks whether the user exists in the database by email.
     *
     * @param email Email address of the user to check.
     * @return User object representing the user if found, null otherwise.
     */

    User existByEmail(String email);

    /**
     * Finds a user in the database by checking the given user ID and converts it into DTO.
     *
     * @param id ID of the user.
     * @return Optional containing the converted user as the result.
     */
    User findUserById(Long id);

    /**
     * Updates an existing user with new data.
     *
     * @param updatedUser New data for the update.
     * @param emailOfUserForUpdating Email in order to get user, who needs to apply changes.
     * @return User object representing the updated user.
     */
    User updateUser(User updatedUser, String emailOfUserForUpdating);

    /**
     * Removes the selected user.
     *
     * @param id ID of the existing user.
     * @return true if the deletion is successful, false otherwise.
     */
    void deleteUser(long id);

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
    Authentication userAuthentication(User user);

    /**
     * Initiates the process of resetting the user's password and sends a reset password link via email.
     *
     * @param request EmailRequest containing the user's email address.
     * @return A success message indicating that the reset link has been sent.
     * @throws MessagingException if there is an issue with sending the reset password email.
     */
    String forgotPassword(EmailRequest request) throws MessagingException;

    /**
     * Verifies the provided unique code for password reset and retrieves the associated user.
     *
     * @param uniqueCode Unique code sent to the user for password reset.
     * @return User object representing the user associated with the unique code.
     */
    User acceptCode(String uniqueCode);

    /**
     * Changes the user's password based on the provided login request.
     *
     * @param request LoginRequest containing the user's email, new password, and confirmation code.
     * @return A success message indicating that the password has been changed.
     */
    String changePassword(LoginRequest request);

    /**
     * Retrieves the currently authenticated user from the security context.
     *
     * @return User object representing the authenticated user.
     */
    User getUserFromSecurityContextHolder();
}
