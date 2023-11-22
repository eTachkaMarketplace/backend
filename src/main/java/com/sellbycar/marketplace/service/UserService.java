package com.sellbycar.marketplace.service;

import com.sellbycar.marketplace.repository.model.User;
import com.sellbycar.marketplace.rest.exception.UserDataException;
import com.sellbycar.marketplace.rest.payload.request.EmailRequest;
import com.sellbycar.marketplace.rest.payload.request.LoginRequest;
import com.sellbycar.marketplace.rest.payload.request.SignupRequest;
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
    User findUser(Long id);

    /**
     * Updates an existing user with new data.
     *
     * @param user New data for the update.
     * @return User object representing the updated user.
     */
    User updateUser(User user);

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
    Authentication userAuthentication(User user);

    String forgotPassword(EmailRequest request) throws MessagingException;

    User acceptCode(String uniqueCode);

    String changePassword(LoginRequest request);

    boolean isPasswordValid(String password);
}
