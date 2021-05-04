package co.za.bonk;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class Hashing {

    public final static char[] hexArray = "0123456789ABCDEF".toCharArray();
    static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static String randomHex(int amount) {
        Random random = new Random();
        byte[] arr = new byte[amount];
        random.nextBytes(arr);
        return bytesToHex(arr);
    }

    public static String hashString(String input) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(input.getBytes());
            String stringHash = bytesToHex(messageDigest.digest());

            return stringHash;
        } catch (NoSuchAlgorithmException e) {}
        return null;
    }
    
}
