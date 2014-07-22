package com.robotech.military.internal;

public class ConnectionError extends Error {
    public ConnectionError() {
    }

    public ConnectionError(String message) {
        super(message);
    }

    public ConnectionError(String message, Throwable cause) {
        super(message, cause);
    }

    public ConnectionError(Throwable cause) {
        super(cause);
    }
}
