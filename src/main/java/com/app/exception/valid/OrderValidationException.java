package com.app.exception.valid;

import com.app.exception.ApplicationGlobalException;

public class OrderValidationException extends ApplicationGlobalException {
    public OrderValidationException() {
        super();
    }

    public OrderValidationException(String message) {
        super(message);
    }

    public OrderValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
