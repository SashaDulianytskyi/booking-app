package com.app.exception.valid;

import com.app.exception.ApplicationGlobalException;

public class RoomValidationException extends ApplicationGlobalException {
    public RoomValidationException() {
        super();
    }

    public RoomValidationException(String message) {
        super(message);
    }

    public RoomValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
