package com.app.exception.valid;

import com.app.exception.ApplicationGlobalException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ClientValidationException extends ApplicationGlobalException {

    public ClientValidationException() {
        super();
    }

    public ClientValidationException(String message) {
        super(message);
    }

    public ClientValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
