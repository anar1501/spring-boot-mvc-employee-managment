package com.company.employeemanagment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND,reason = "USER NOT FOUND")
public class CustomRuntimeException extends RuntimeException{
    public CustomRuntimeException(){
        super("USER NOT FOUND");
    }

}
