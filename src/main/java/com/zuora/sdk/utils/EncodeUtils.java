package com.zuora.sdk.utils;

import java.net.URLEncoder;

/**
 * EncodeUtils
 */
public class EncodeUtils {

    public String encode(String text) {

        try {
            return URLEncoder.encode(text, "UTF-8");
        } catch (Exception e) {
            System.out.println("Could not encode text " + e);
        }

        return null;
    }
}
