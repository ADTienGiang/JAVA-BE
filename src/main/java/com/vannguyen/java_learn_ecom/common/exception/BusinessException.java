package com.vannguyen.java_learn_ecom.common.exception;

public class BusinessException extends RuntimeException {
    public BusinessException(String message){
        super(message);
    }
}
