package com.placideh.employees.exception;

import java.net.UnknownHostException;

public class EmailSendException extends UnknownHostException {
    public EmailSendException(String message) {
        super(message);
    }

    public EmailSendException() {
        super();
    }
}
