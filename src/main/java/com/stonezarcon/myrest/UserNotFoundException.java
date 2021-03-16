package com.stonezarcon.myrest;

public class UserNotFoundException extends RuntimeException {

    UserNotFoundException(int id) {
        super("Could not find user " + id);
    }
}