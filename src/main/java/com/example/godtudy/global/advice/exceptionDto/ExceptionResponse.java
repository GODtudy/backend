package com.example.godtudy.global.advice.exceptionDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExceptionResponse {
    private boolean success;
    private int status;
    private String msg;

    public static ExceptionResponse getFailureResult(int status, String msg) {
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setSuccess(false);
        exceptionResponse.setStatus(status);
        exceptionResponse.setMsg(msg);

        return exceptionResponse;
    }
}
