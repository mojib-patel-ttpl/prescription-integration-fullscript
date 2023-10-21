package com.fullscriptintegration.fullscript.integration.exception;

import com.fullscriptintegration.fullscript.integration.util.ResponseCode;
import lombok.Getter;

@Getter
public class FullscriptThinkitiveException extends Exception {

    private ResponseCode errorCode;
    private String[] fields;
    private Exception exception;

    public FullscriptThinkitiveException() {
        super("Failed to do operation");
        this.errorCode = ResponseCode.INTERNAL_ERROR;
        this.exception = new RuntimeException();
    }

    public FullscriptThinkitiveException(ResponseCode code, String message, String... fields) {
        super(message);
        this.errorCode = code;
        this.fields = fields;
    }

    public FullscriptThinkitiveException(Exception exception) {
        super(exception.getLocalizedMessage());
        this.errorCode = ResponseCode.INTERNAL_ERROR;
        this.exception = exception;
    }
}
