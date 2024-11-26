package com.android.kotemanagement.authentication;

import android.content.Context;
import android.content.SharedPreferences;

public class LoginAuthentication {


    public static void saveLoginInfo(Context context, String armyNumber, String username) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("armyNumber", armyNumber);
        editor.putString("username", username);
        editor.putBoolean("isLoggedIn", true);
        editor.apply();
    }

    public static boolean isLoggedIn(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("isLoggedIn", false); // Default value false if not logged in
    }


    public static void clearLoginInfo(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public static String getUsername(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("username", null);
    }


    public static String getArmyNumber(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("armyNumber", null);
    }
}
