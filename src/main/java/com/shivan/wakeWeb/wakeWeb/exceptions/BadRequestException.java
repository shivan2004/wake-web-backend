package com.shivan.wakeWeb.wakeWeb.exceptions;

public class BadRequestException extends RuntimeException{
    public BadRequestException(String message){
        super(message);
    }
}
