/**
 * Copyright (c) 2013 Zuora Inc.
 */
package com.zuora.sdk.common;

import com.zuora.sdk.utils.ZLogger;

public class ZEnv {

    private static ZEnv instance_;

    public ZEnv() {

        ZLogger.getInstance().log("***** SDK loggers initialized *****", ZConstants.LOG_BOTH);

        try {
            ZConfig.getInstance().loaded();
        } catch (RuntimeException e) {
            ZLogger.getInstance().log(e.getMessage(), ZConstants.LOG_SDK);
            System.out.println("Unable to initialize SDK environment. Please see log for details.");
            System.exit(1);
        }
    }

    public synchronized static ZEnv getInstance() {
        if (instance_ == null) {
            instance_ = new ZEnv();
        }
        return instance_;
    }
}