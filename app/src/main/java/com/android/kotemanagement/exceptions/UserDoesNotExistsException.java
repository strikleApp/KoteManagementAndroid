package com.android.kotemanagement.exceptions;

public class UserDoesNotExistsException extends Exception{
    public String message() {
        return "Soldier doesn't exists.";
    }
}
