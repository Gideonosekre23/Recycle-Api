package com.Ossolutions.RecycleApi.Service;



public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}

