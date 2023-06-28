package com.codoacodo.flysky.demo.exception;

public class VueloNotFoundException extends RuntimeException{
    public VueloNotFoundException(String message) {
        super(message);
    }
}
