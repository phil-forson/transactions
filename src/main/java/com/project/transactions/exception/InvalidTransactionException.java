package com.project.transactions.exception;

public class InvalidTransactionException extends RuntimeException {
    public InvalidTransactionException(String message){
        super(message);
    }
}
