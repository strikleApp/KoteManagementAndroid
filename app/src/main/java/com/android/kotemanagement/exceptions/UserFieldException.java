package com.android.kotemanagement.exceptions;

public class UserFieldException extends Exception{

   public String message() {
       return "Check all fields carefully before submitting.";
   }
}
