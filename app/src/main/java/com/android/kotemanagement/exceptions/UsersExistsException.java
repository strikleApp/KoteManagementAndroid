package com.android.kotemanagement.exceptions;

public class UsersExistsException extends Exception{
    public String message() {
        return "User already exists.";
    }
}
