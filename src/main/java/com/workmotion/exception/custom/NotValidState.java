package com.workmotion.exception.custom;

public class NotValidState extends RuntimeException{
    public NotValidState (String message){
        super(message);
    }
}
