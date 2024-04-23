package com.shehan.book.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum BusinessErrorCode {

    NO_CODE(0, HttpStatus.NOT_IMPLEMENTED, "No code"),
    ACCOUNT_LOCKED(302, HttpStatus.FORBIDDEN, "User account is locked"),
    ;

    private final int code;
    private final HttpStatus httpStatus;
    private final String description;

    BusinessErrorCode(int code, HttpStatus httpStatus, String description) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.description = description;
    }
}
