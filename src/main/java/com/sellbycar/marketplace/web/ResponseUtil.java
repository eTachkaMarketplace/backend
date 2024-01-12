package com.sellbycar.marketplace.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {

    public static <T> ResponseEntity<ResponseBody<T>> create(String message, HttpStatus status, T responseObj) {
        ResponseBody<T> body = new ResponseBody<>(message, status.name(), responseObj);
        return new ResponseEntity<>(body, status);
    }

    public static ResponseEntity<ResponseBody<Void>> create(String message, HttpStatus status) {
        ResponseBody<Void> body = new ResponseBody<>(message, status.name(), null);
        return new ResponseEntity<>(body, status);
    }

    public static <T> ResponseEntity<ResponseBody<T>> created(T responseObj) {
        ResponseBody<T> body = new ResponseBody<>(null, "Created", responseObj);
        return new ResponseEntity<>(body, HttpStatus.CREATED);
    }

    public static <T> ResponseEntity<ResponseBody<T>> ok(T responseObj) {
        ResponseBody<T> body = new ResponseBody<>(null, "OK", responseObj);
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    public static ResponseEntity<ResponseBody<Void>> ok(String message) {
        ResponseBody<Void> body = new ResponseBody<>(message, "OK", null);
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    public static <T> ResponseEntity<ResponseBody<T>> ok(String message, T responseObj) {
        ResponseBody<T> body = new ResponseBody<>(message, "OK", responseObj);
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    public static ResponseEntity<ResponseBody<Void>> error(String message, HttpStatus status) {
        ResponseBody<Void> body = new ResponseBody<>(message, status.name(), null);
        return new ResponseEntity<>(body, status);
    }
}
