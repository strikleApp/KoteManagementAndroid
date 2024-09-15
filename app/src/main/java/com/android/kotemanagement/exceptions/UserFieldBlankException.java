package com.android.kotemanagement.exceptions;

public class UserFieldBlankException extends Exception{
    public String message() {
        return "Please fill all the fields.";
    }
}
