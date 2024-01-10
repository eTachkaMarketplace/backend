package com.sellbycar.marketplace.auth;

import jakarta.security.auth.message.AuthException;
import jakarta.validation.constraints.NotNull;

public interface AuthService {
    /**
     * Saves the JWT refresh token associated with a user's email.
     * <p>
     * This method is responsible for persisting the JWT refresh token in a secure manner,
     * typically in the database, for the given user identified by their email address.
     *
     * @param email           The email address of the user.
     * @param jwtRefreshToken The JWT refresh token to be saved.
     */
    void saveJwtRefreshToken(String email, String jwtRefreshToken);

    /**
     * Retrieves a new JWT access token based on the provided refresh token.
     * <p>
     * This method is responsible for generating a new JWT access token using the provided refresh token.
     * The refresh token is typically obtained from the client and used to request a fresh access token.
     *
     * @param refreshToken The refresh token used to request a new JWT access token.
     * @return JwtResponse containing the new JWT access token and related information.
     * @throws AuthException if the provided refresh token is null.
     */

    JwtResponse getJwtAccessToken(@NotNull String refreshToken) throws AuthException;

    /**
     * Retrieves information related to a JWT refresh token.
     * <p>
     * This method is responsible for obtaining information about the provided JWT refresh token.
     * It can include details such as the token's expiration, associated user, or any additional data.
     *
     * @param refreshToken The JWT refresh token for which information is requested.
     * @return JwtResponse containing information related to the provided JWT refresh token.
     * @throws AuthException if the provided refresh token is null.
     */

    JwtResponse getJwtRefreshToken(@NotNull String refreshToken) throws AuthException;
}
