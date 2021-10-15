package com.placideh.employees.exception;

public class EmployeeInputException extends Exception {
    public EmployeeInputException() {
        super();
    }

    public EmployeeInputException(String message) {
        super(message);
    }

    public EmployeeInputException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmployeeInputException(Throwable cause) {
        super(cause);
    }

    protected EmployeeInputException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
