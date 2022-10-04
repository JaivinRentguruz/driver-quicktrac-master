package com.evolution.quicktrack.util;

public class Utility {
    public static boolean isValidString(String target) {
        return !target.equals("") || target.trim().length() > 0;
    }
}
