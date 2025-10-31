package com.rps.goldloan.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<Object> handleCustomerNotFoundException(CustomerNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomerCreationException.class)
    public ResponseEntity<Object> handleCustomerCreationException(CustomerCreationException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomerUpdateException.class)
    public ResponseEntity<Object> handleCustomerUpdateException(CustomerUpdateException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BranchNotFoundException.class)
    public ResponseEntity<Object> handleBranchNotFoundException(BranchNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BranchCreationException.class)
    public ResponseEntity<Object> handleBranchCreationException(BranchCreationException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BranchUpdateException.class)
    public ResponseEntity<Object> handleBranchUpdateException(BranchUpdateException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserCreationException.class)
    public ResponseEntity<Object> handleUserCreationException(UserCreationException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserUpdateException.class)
    public ResponseEntity<Object> handleUserUpdateException(UserUpdateException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DocumentNotFoundException.class)
    public ResponseEntity<Object> handleDocumentNotFoundException(DocumentNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DocumentCreationException.class)
    public ResponseEntity<Object> handleDocumentCreationException(DocumentCreationException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DocumentUpdateException.class)
    public ResponseEntity<Object> handleDocumentUpdateException(DocumentUpdateException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LoanApplicationNotFoundException.class)
    public ResponseEntity<Object> handleLoanApplicationNotFoundException(LoanApplicationNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(LoanApplicationCreationException.class)
    public ResponseEntity<Object> handleLoanApplicationCreationException(LoanApplicationCreationException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LoanApplicationUpdateException.class)
    public ResponseEntity<Object> handleLoanApplicationUpdateException(LoanApplicationUpdateException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LoanTermNotFoundException.class)
    public ResponseEntity<Object> handleLoanTermNotFoundException(LoanTermNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(LoanTermCreationException.class)
    public ResponseEntity<Object> handleLoanTermCreationException(LoanTermCreationException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LoanTermUpdateException.class)
    public ResponseEntity<Object> handleLoanTermUpdateException(LoanTermUpdateException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GoldDetailNotFoundException.class)
    public ResponseEntity<Object> handleGoldDetailNotFoundException(GoldDetailNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(GoldDetailCreationException.class)
    public ResponseEntity<Object> handleGoldDetailCreationException(GoldDetailCreationException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GoldDetailUpdateException.class)
    public ResponseEntity<Object> handleGoldDetailUpdateException(GoldDetailUpdateException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ApplicationAuditNotFoundException.class)
    public ResponseEntity<Object> handleApplicationAuditNotFoundException(ApplicationAuditNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ApplicationAuditCreationException.class)
    public ResponseEntity<Object> handleApplicationAuditCreationException(ApplicationAuditCreationException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValuationReportNotFoundException.class)
    public ResponseEntity<Object> handleValuationReportNotFoundException(ValuationReportNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ValuationReportCreationException.class)
    public ResponseEntity<Object> handleValuationReportCreationException(ValuationReportCreationException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValuationReportUpdateException.class)
    public ResponseEntity<Object> handleValuationReportUpdateException(ValuationReportUpdateException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
