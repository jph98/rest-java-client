/**
 * Copyright (c) 2013 Zuora Inc.
 */
package com.zuora.sdk.utils;

public class ZUtils {

    public static String stackTraceToString(Throwable e) {

        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : e.getStackTrace()) {
            sb.append(element.toString());
            sb.append("\n");
        }
        return sb.toString();
    }
}