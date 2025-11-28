package com.lisoft.autapi.application.utils;

import java.util.Objects;
import java.util.regex.Pattern;

public class PasswordValidator {

    private static final int MIN_LENGTH = 8;
    private static final Pattern HAS_UPPER_CASE = Pattern.compile("[A-Z]");
    private static final Pattern HAS_LOWER_CASE = Pattern.compile("[a-z]");
    private static final Pattern HAS_NUMBER = Pattern.compile("\\d");
    private static final Pattern HAS_SPECIAL_CHAR = Pattern.compile("[!@#$%^&*]");

    public static boolean isValid(String password) {
        if (Objects.isNull(password)) return false;

        boolean hasUpperCase = HAS_UPPER_CASE.matcher(password).find();
        boolean hasLowerCase = HAS_LOWER_CASE.matcher(password).find();
        boolean hasNumber = HAS_NUMBER.matcher(password).find();
        boolean hasSpecialChar = HAS_SPECIAL_CHAR.matcher(password).find();

        return password.length() >= MIN_LENGTH &&
                hasUpperCase &&
                hasLowerCase &&
                hasNumber &&
                hasSpecialChar;
    }
}