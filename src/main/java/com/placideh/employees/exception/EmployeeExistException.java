package com.placideh.employees.exception;

public class EmployeeExistException extends Exception {
    public EmployeeExistException() {
        super();
    }

    public EmployeeExistException(String message) {
        super(message);
    }

    public EmployeeExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmployeeExistException(Throwable cause) {
        super(cause);
    }

    protected EmployeeExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
