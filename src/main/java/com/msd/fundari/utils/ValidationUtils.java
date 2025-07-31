package com.msd.fundari.utils;

import java.util.regex.Pattern;

public class ValidationUtils {
  public static boolean isNumeric(String strNum) {
    Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

    if (strNum == null) {
      return false;
    }
    return pattern.matcher(strNum).matches();
  }

  public static boolean isInteger(String str) {
    if (str == null || str.isEmpty()) {
      return false;
    }
    // Matches an optional sign (+ or -) followed by one or more digits
    return str.matches("^-?\\d+$");
  }
}
