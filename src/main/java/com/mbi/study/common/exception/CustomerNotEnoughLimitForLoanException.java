package com.mbi.study.common.exception;

public class CustomerNotEnoughLimitForLoanException extends RuntimeException {
    public CustomerNotEnoughLimitForLoanException(String string) {
        super(string);
    }
}
