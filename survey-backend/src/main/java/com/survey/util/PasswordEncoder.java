package com.survey.util;

import org.springframework.util.DigestUtils;

import java.util.UUID;

public class PasswordEncoder {

    public static String encode(String rawPassword) {
        String salt = UUID.randomUUID().toString().substring(0, 8);
        String hash = DigestUtils.md5DigestAsHex((salt + rawPassword).getBytes());
        return salt + ":" + hash;
    }

    public static boolean matches(String rawPassword, String encodedPassword) {
        if (encodedPassword == null || !encodedPassword.contains(":")) {
            return false;
        }
        String[] parts = encodedPassword.split(":");
        String salt = parts[0];
        String storedHash = parts[1];
        String hash = DigestUtils.md5DigestAsHex((salt + rawPassword).getBytes());
        return storedHash.equals(hash);
    }
}
