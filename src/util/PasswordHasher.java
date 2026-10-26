package util;

import java.nio.charset.*;
import java.security.*;

public class PasswordHasher {
    private static final int SALT_LENGTH = 16;
    private static final String HASH_ALGORITHM = "SHA-256";

    public static String generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        new SecureRandom().nextBytes(salt);
        return bytesToHex(salt);
    }

    public static String hashPassword(String password, String salt) {
        if (password == null || salt == null) {
            throw new IllegalArgumentException("Password and salt cannot be null");
        }
        try {
            MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
            digest.update(salt.getBytes(StandardCharsets.UTF_8));
            byte[] hashed = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hashed);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hashing algorithm unavailable", e);
        }
    }

    public static boolean verifyPassword(String password, String salt, String expectedHash) {
        if (password == null || salt == null || expectedHash == null) {
            return false;
        }
        String computedHash = hashPassword(password, salt);
        return MessageDigest.isEqual(
                computedHash.getBytes(StandardCharsets.UTF_8),
                expectedHash.getBytes(StandardCharsets.UTF_8)
        );
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
