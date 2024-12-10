package com.example.jpahibernatetip.support;

import java.security.SecureRandom;

public class IdGenerator {

    public static String generateULID() {
        char[] ENCODING = "0123456789".toCharArray();
        int TIMEBASE_LENGTH = 10;
        int RANDOM_LENGTH = 10;
        SecureRandom RANDOM = new SecureRandom();

        long time = System.currentTimeMillis();
        char[] timebaseChars = new char[TIMEBASE_LENGTH];
        for (int i = 0; i < timebaseChars.length; i++) {
            timebaseChars[i] = ENCODING[(int) (time & (ENCODING.length - 1))];
            time >>>= 5;
        }
        char[] randomChars = new char[RANDOM_LENGTH];
        for (int i = 0; i < randomChars.length; i++) {
            randomChars[i] = ENCODING[RANDOM.nextInt(ENCODING.length)];
        }
        return String.format(
                "%s%s",
                new String(timebaseChars),
                new String(randomChars)
        );
    }
}
