package com.workmotion.exception.custom;

public class NotFoundData extends RuntimeException {
    public NotFoundData(String message) {
        super(message);
    }
}
