package com.sellbycar.marketplace.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public class ResponseUtil {
    public static ResponseEntity<Object> create(String message, HttpStatus status, Object responseObj) {
        Map<String, Object> map = Map.of(
                "message", message,
                "status", status.value(),
                "data", responseObj
        );

        return new ResponseEntity<>(map, status);
    }

    public static ResponseEntity<Object> create(String message, HttpStatus status) {
        Map<String, Object> map = Map.of(
                "message", message,
                "status", status.value()
        );

        return new ResponseEntity<>(map, status);
    }

    public static ResponseEntity<Object> createError(String message, HttpStatus status) {
        Map<String, Object> map = Map.of(
                "message", message,
                "status", status.value()
        );

        return new ResponseEntity<>(map, status);
    }
}
