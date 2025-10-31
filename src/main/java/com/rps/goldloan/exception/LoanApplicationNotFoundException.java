package com.rps.goldloan.exception;

public class LoanApplicationNotFoundException extends RuntimeException {

    public LoanApplicationNotFoundException(String message) {
        super(message);
    }
}

