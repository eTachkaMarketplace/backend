package com.sellbycar.marketplace.user;

import com.sellbycar.marketplace.util.ResponseUtil;
import com.sellbycar.marketplace.util.exception.RequestException;
import jakarta.security.auth.message.AuthException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class UserExceptionController {

    @ExceptionHandler(RequestException.class)
    @ResponseBody
    public ResponseEntity<?> onBadRequest(RequestException e) {
        return ResponseUtil.error(e.getMessage(), e.getStatus());
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseBody
    public ResponseEntity<?> onUserUnauthorized(BadCredentialsException e) {
        return ResponseUtil.error(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(SecurityException.class)
    @ResponseBody
    public ResponseEntity<?> onBadAccess(SecurityException e) {
        return ResponseUtil.error(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AuthException.class)
    @ResponseBody
    public ResponseEntity<?> onAuthFailed(AuthException e) {
        return ResponseUtil.error(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }
}
