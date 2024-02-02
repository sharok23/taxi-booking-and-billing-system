package com.edstem.taxibookingandbillingsystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EntityAlreadyExistsException extends RuntimeException{
    private String entity;

    public EntityAlreadyExistsException(String entity){
        super(entity+ " already exists");
        this.entity = entity;
    }
}
