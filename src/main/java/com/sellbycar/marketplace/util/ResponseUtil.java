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

    public static ResponseEntity<Object> created(Object responseObj) {
        Map<String, Object> map = Map.of(
                "status", HttpStatus.CREATED.value(),
                "data", responseObj
        );

        return new ResponseEntity<>(map, HttpStatus.CREATED);
    }

    public static ResponseEntity<Object> ok(Object responseObj) {
        Map<String, Object> map = Map.of(
                "status", HttpStatus.OK.value(),
                "data", responseObj
        );

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    public static ResponseEntity<Object> ok(String message) {
        Map<String, Object> map = Map.of(
                "message", message,
                "status", HttpStatus.OK.value()
        );

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    public static ResponseEntity<Object> ok(String message, Object responseObj) {
        Map<String, Object> map = Map.of(
                "message", message,
                "status", HttpStatus.OK.value(),
                "data", responseObj
        );

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    public static ResponseEntity<Object> error(String message, HttpStatus status) {
        Map<String, Object> map = Map.of(
                "message", message,
                "status", status.value()
        );

        return new ResponseEntity<>(map, status);
    }
}
