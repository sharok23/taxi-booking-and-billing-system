package com.edstem.taxibookingandbillingsystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EntityNotFoundException extends RuntimeException{
    private String entity;
    private Long id;

    public EntityNotFoundException(String entity){
        super(entity+ " not found");
        this.entity = entity;
    }

    public EntityNotFoundException(String entity,Long id){
        super(entity+ " not found with id " + id);
        this.entity = entity;
        this.id = id;
    }
}
