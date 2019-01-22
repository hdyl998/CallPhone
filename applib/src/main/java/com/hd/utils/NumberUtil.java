package com.hd.utils;

public class NumberUtil {

    public static int convertToInt(String intStr, int defValue) {
        try {
            return Integer.parseInt(intStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defValue;
    }

    public static long convertToLong(String longStr, long defValue) {
        try {
            return Long.parseLong(longStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defValue;
    }

    public static float convertToFloat(String fStr, float defValue) {
        try {
            return Float.parseFloat(fStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defValue;
    }

    public static double convertToDouble(String dStr, double defValue) {
        try {
            return Double.parseDouble(dStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defValue;
    }


    public static int convertToInt(String intStr) {
        try {
            return Integer.valueOf(intStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static long convertToLong(String longStr) {
        try {
            return Long.valueOf(longStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0l;
    }

    public static float convertToFloat(String fStr) {
        try {
            return Float.valueOf(fStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0f;
    }

    public static double convertToDouble(String dStr) {
        try {
            return Double.valueOf(dStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0d;
    }

}
