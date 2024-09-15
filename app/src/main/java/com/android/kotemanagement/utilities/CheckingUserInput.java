package com.android.kotemanagement.utilities;

import androidx.annotation.NonNull;

public class CheckingUserInput {

    public static boolean isFirstNameHaveNumber(String firstName) {
        return firstName.matches(".*\\d.*");
    }

    public static boolean isLastNameHaveNumber(String lastName) {
        return lastName.matches(".*\\d.*");
    }

    public static boolean isArmyNumberHaveWhiteSpace(@NonNull String armyNumber) {
        return armyNumber.matches(".*\\s.*");
    }

    public static boolean isFirstNameHaveSpecialCharacters(@NonNull String firstName) {
        return firstName.matches(".*[^a-zA-Z\\s].*");
    }

    public static boolean isLastNameHaveSpecialCharacters(@NonNull String lastName) {
        return lastName.matches(".*[^a-zA-Z\\s].*");
    }

    public static boolean isArmyNumberHaveSpecialCharacters(@NonNull String armyNumber) {
        return armyNumber.matches(".*[^a-zA-Z0-9\\s].*");
    }

}
