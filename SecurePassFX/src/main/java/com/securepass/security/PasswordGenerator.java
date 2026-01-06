package com.securepass.security;




import java.security.SecureRandom;

public class PasswordGenerator {
    private static final String CHAR_SET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%&*";
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generate(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHAR_SET.charAt(RANDOM.nextInt(CHAR_SET.length())));
        }
        return sb.toString();
    }
}

