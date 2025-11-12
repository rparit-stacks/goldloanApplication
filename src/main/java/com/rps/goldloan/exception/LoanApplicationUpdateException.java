package com.rps.goldloan.exception;

public class LoanApplicationUpdateException extends RuntimeException {

    public LoanApplicationUpdateException(String message, Exception e) {
        super(message);
    }
}

