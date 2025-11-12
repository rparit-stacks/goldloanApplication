package com.rps.goldloan.exception;

public class LoanApplicationCreationException extends RuntimeException {

    public LoanApplicationCreationException(String message, Exception e) {
        super(message);
    }
}

