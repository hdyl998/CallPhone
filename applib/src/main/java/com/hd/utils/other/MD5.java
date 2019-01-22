package com.hd.utils.other;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5运算
 * <p/>
 */
public class MD5 {
    public static String getMessageDigest(byte[] paramArrayOfByte) {
        char[] arrayOfChar1 = {48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102};
        try {
            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
            localMessageDigest.update(paramArrayOfByte);
            byte[] arrayOfByte = localMessageDigest.digest();
            int i = arrayOfByte.length;
            char[] arrayOfChar2 = new char[i * 2];
            int j = 0;
            int k = 0;
            while (true) {
                if (j >= i) return new String(arrayOfChar2);
                int m = arrayOfByte[j];
                int n = k + 1;
                arrayOfChar2[k] = arrayOfChar1[(0xF & m >>> 4)];
                k = n + 1;
                arrayOfChar2[n] = arrayOfChar1[(m & 0xF)];
                j++;
            }
        } catch (Exception localException) {
        }
        return null;
    }

    public static byte[] encrypt(byte[] content) {
        try {
            MessageDigest md;
            md = MessageDigest.getInstance("MD5");
            md.update(content, 0, content.length);
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encrypt(String key) {
        if (key == null) {
            return "";
        }
        char[] hex = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
                'f'};
        byte[] bytes = encrypt(key.getBytes());
        if (bytes == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder(32);
        for (byte b : bytes) {
            sb.append(hex[((b >> 4) & 0xF)]).append(hex[((b >> 0) & 0xF)]);
        }
        return sb.toString();
    }

}
