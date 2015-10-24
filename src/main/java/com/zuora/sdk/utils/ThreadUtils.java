package com.zuora.sdk.utils;

/**
 * ThreadUtils
 */
public class ThreadUtils {

    private static final int SAMPLE_WAIT = 10000;

    public static void sleep() {

        try {
            Thread.sleep(SAMPLE_WAIT);
        } catch (InterruptedException e) {
        }
    }
}
