package com.placideh.employees.exception;

import com.placideh.employees.model.ErrorMessage;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.UnknownHostException;

@ControllerAdvice
@ResponseStatus
public class RestResponseEntityException extends ResponseEntityExceptionHandler {
    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<ErrorMessage> employeeNotFoundException(EmployeeNotFoundException ex, WebRequest request){
        ErrorMessage message=new ErrorMessage(HttpStatus.NOT_FOUND,ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }
    @ExceptionHandler(EmployeeInputException.class)
    public ResponseEntity<ErrorMessage> employeeInputException(EmployeeInputException ex){
        ErrorMessage message=new ErrorMessage(HttpStatus.FORBIDDEN,ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(message);
    }

    @ExceptionHandler(EmployeeExistException.class)
    public ResponseEntity<ErrorMessage> employeeExistException(EmployeeExistException ex){
        ErrorMessage message=new ErrorMessage(HttpStatus.CONFLICT,ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
    }

    @ExceptionHandler(EmailSendException.class)
    public ResponseEntity<ErrorMessage> employeeNetException(EmailSendException ex){
        ErrorMessage message=new ErrorMessage(HttpStatus.REQUEST_TIMEOUT,ex.getMessage());
        return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(message);
    }
    @ExceptionHandler(UnknownHostException.class)
    public ResponseEntity<ErrorMessage> unKnowHostException(UnknownHostException ex){
        ErrorMessage message=new ErrorMessage(HttpStatus.REQUEST_TIMEOUT,"Please Connect On The Network");
        return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(message);
    }

    @ExceptionHandler(DbContraintsException.class)
    public ResponseEntity<ErrorMessage> handleConstraintViolation(DbContraintsException ex, WebRequest request) {
        ErrorMessage message=new ErrorMessage(HttpStatus.CONFLICT,ex.getMessage());
        Throwable cause = ex.getCause();
        if (cause instanceof ConstraintViolationException){
            return ResponseEntity.status(HttpStatus.CONFLICT).body((new ErrorMessage(HttpStatus.CONFLICT,cause.getMessage())));
        }

        return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
    }
}
