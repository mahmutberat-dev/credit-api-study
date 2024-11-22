package com.mbi.study.exception;

public class LoanNotEnoughLimitException extends RuntimeException {
    public LoanNotEnoughLimitException(String string) {
        super(string);
    }
}
