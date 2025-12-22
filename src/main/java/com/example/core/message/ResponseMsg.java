package com.example.core.message;

import com.example.core.constant.ResultCode;
import com.example.core.entity.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.HashMap;

public class ResponseMsg<T> extends ResponseEntity<ResponseMsg.WrapContent<T>> {
    protected ResponseMsg(HttpStatus httpStatus) {
        super(httpStatus);
    }

    protected ResponseMsg(WrapContent<T> body, HttpStatus httpStatus) {
        super(body, httpStatus);
    }

    public static ResponseMsg<Object> newResponse(HttpStatus httpStatus, String message) {
        WrapContent<Object> wrapContent = new WrapContent<>(message);
        return new ResponseMsg<>(wrapContent, httpStatus);
    }

    public static ResponseMsg<Object> newResponse(ResultCode resultCode) {
        WrapContent<Object> wrapContent = new WrapContent<>(resultCode);
        return new ResponseMsg<>(wrapContent, resultCode.getHttpStatus());
    }

    public static ResponseMsg<Object> newOKResponse() {
        WrapContent<Object> wrapContent = new WrapContent<>(ResultCode.SUCCESS);
        return new ResponseMsg<>(wrapContent, ResultCode.SUCCESS.getHttpStatus());
    }

    public static ResponseMsg<Object> newOKResponse(Object body) {
        WrapContent<Object> wrapContent = new WrapContent<>(ResultCode.SUCCESS, body);
        return new ResponseMsg<>(wrapContent, ResultCode.SUCCESS.getHttpStatus());
    }

    public static ResponseMsg<Object> new500ErrorResponse() {
        WrapContent<Object> wrapContent = new WrapContent<>(ResultCode.INTERNAL_SERVER_ERROR);
        return new ResponseMsg<>(wrapContent, ResultCode.INTERNAL_SERVER_ERROR.getHttpStatus());
    }

    protected static class WrapContent<T> extends HashMap<String, Object> {
        public WrapContent(ResultCode resultCode) {
            this.put("resultCode", resultCode.getCode());
            this.put("resultMsg", resultCode.getDesc());
        }

        public WrapContent(String message) {
            this.put("resultCode", ResultCode.OTHER_SERVLET_ERROR.getCode());
            this.put("resultMsg", message);
        }

        public WrapContent(ResultCode resultCode, T content) {
            this.put("data", content);
            this.put("resultCode", resultCode.getCode());
            this.put("resultMsg", resultCode.getDesc());
        }
    }
}
