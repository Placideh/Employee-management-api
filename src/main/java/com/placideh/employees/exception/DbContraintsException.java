package com.placideh.employees.exception;

public class DbContraintsException extends  Exception {
    public DbContraintsException() {
        super();
    }

    public DbContraintsException(String message) {
        super(message);
    }

    public DbContraintsException(String message, Throwable cause) {
        super(message, cause);
    }

    public DbContraintsException(Throwable cause) {
        super(cause);
    }

    protected DbContraintsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
