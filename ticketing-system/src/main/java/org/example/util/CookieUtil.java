package org.example.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class CookieUtil {
    private static final String ALGORITHM = "AES";
    private static final String SECRET_KEY = "a35dc357dc5d3abc9482dc55ce752de7"; // 32 byte

    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, encrypt(value));
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    public static String getCookie(HttpServletRequest request, String name) {
        if (request.getCookies() == null) return null;
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(name)) {
                return decrypt(cookie.getValue());
            }
        }
        return null;
    }

    public static void deleteCookie(HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    private static String encrypt(String value) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            return Base64.getEncoder().encodeToString(cipher.doFinal(value.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException("Cookie encryption failed", e);
        }
    }

    private static String decrypt(String encryptedValue) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            return new String(cipher.doFinal(Base64.getDecoder().decode(encryptedValue)));
        } catch (Exception e) {
            return null;
        }
    }
}
