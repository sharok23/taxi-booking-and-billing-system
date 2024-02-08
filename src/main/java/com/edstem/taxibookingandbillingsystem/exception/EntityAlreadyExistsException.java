package com.edstem.taxibookingandbillingsystem.exception;

public class EntityAlreadyExistsException extends RuntimeException {
    private String entity;

    public EntityAlreadyExistsException(String entity) {
        super(entity + " already exists");
        this.entity = entity;
    }
}
