package com.sellbycar.marketplace.util.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
@Getter
public class RequestException extends RuntimeException {

    private HttpStatus status;

    public RequestException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public RequestException(String message, Throwable cause, HttpStatus status) {
        super(message, cause);
        this.status = status;
    }

    public RequestException(Throwable cause, HttpStatus status) {
        super(cause);
        this.status = status;
    }

    public static RequestException internal(String message) {
        return new RequestException(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static RequestException forbidden(String message) {
        return new RequestException(message, HttpStatus.FORBIDDEN);
    }

    public static RequestException bad(String message) {
        return new RequestException(message, HttpStatus.BAD_REQUEST);
    }

    public static RequestException conflict(String message) {
        return new RequestException(message, HttpStatus.CONFLICT);
    }

    public static RequestException notFound(String message) {
        return new RequestException(message, HttpStatus.NOT_FOUND);
    }
}
