package com.edstem.taxibookingandbillingsystem.exception;

public class InvalidUserException extends RuntimeException {
    private String entity;
    private Long id;

    public InvalidUserException(String entity) {
        super("Invalid " + entity);
    }
}
