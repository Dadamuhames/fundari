package com.msd.fundari.utils;

public class HelperMethods {
    public static String toCamelCase(String className) {
        if (className == null || className.isEmpty()) {
            return className;
        }

        char firstChar = Character.toLowerCase(className.charAt(0));

        return firstChar + className.substring(1);
    }
}
