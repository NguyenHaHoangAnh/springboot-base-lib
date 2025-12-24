package com.example.core.constant;

import org.springframework.http.HttpStatus;

public enum ResultCode {
    SUCCESS("SUCCESS", "SUCCESS", HttpStatus.OK),
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "INTERNAL SERVER ERROR", HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHORIZED("UNAUTHORIZED", "UNAUTHORIZED", HttpStatus.UNAUTHORIZED),
    BAD_CREDENTIAL("BAD_CREDENTIAL", "UNAUTHORIZED", HttpStatus.UNAUTHORIZED),
    PERMISSION_DENIED("PERMISSION_DENIED", "ACCESS DENIED", HttpStatus.FORBIDDEN),
    NOT_FOUND("NOT_FOUND", "API NOT FOUND", HttpStatus.NOT_FOUND),
    OBJECT_NOT_EXIST("OBJECT_NOT_EXIST", "OBJECT NO EXIST", HttpStatus.NOT_FOUND),
    OBJECT_EXISTED("OBJECT_EXISTED", "OBJECT EXISTED", HttpStatus.INTERNAL_SERVER_ERROR),
    OTHER_SERVLET_ERROR("OTHER_SERVLET_ERROR", "OTHER SERVLET ERROR", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String desc;
    private final HttpStatus httpStatus;

    private ResultCode(String code, String desc, HttpStatus httpStatus) {
        this.code = code;
        this.desc = desc;
        this.httpStatus = httpStatus;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
