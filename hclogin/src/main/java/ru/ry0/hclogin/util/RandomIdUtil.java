package ru.ry0.hclogin.util;

public class RandomIdUtil {
    public static String generateRandomId() {
        return Long.toHexString(Double.doubleToLongBits(Math.random()));
    }
}
