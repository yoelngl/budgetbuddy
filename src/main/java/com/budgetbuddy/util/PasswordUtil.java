package com.budgetbuddy.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class PasswordUtil {

    private static final String ALGORITHM = "SHA-256";
    private static final String SALT      = "BudgetBuddy@2026#Salt";

    private PasswordUtil() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static String hash(String rawPassword) {
        try {
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            md.update(SALT.getBytes());
            byte[] bytes = md.digest(rawPassword.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Password hashing gagal: " + ALGORITHM + " tidak tersedia", e);
        }
    }

    public static boolean verify(String rawPassword, String hashedPassword) {
        return hash(rawPassword).equals(hashedPassword);
    }
}
