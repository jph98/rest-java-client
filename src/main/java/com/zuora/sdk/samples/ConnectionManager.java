/**
 * Copyright (c) 2013 Zuora Inc.
 * <p/>
 * Sample code to demonstrate how to use the Connections resources
 */

package com.zuora.sdk.samples;

import com.zuora.sdk.common.ZConstants;
import com.zuora.sdk.http.ZAPIArgs;
import com.zuora.sdk.http.ZAPIResp;
import com.zuora.sdk.http.ZClient;
import com.zuora.sdk.utils.ZLogger;
import com.zuora.sdk.utils.ZUtils;

public class ConnectionManager {
    ZClient zClient;

    public boolean isConnected(ZClient zClient, String apiAccessKeyId, String apiSecretAccessKey) {
        this.zClient = zClient;

        ZAPIArgs args = new ZAPIArgs();
        args.set("uri", ResourceEndpoints.CONNECT);
        args.set("headers", new ZAPIArgs());
        args.getArg("headers").set("apiAccessKeyId", apiAccessKeyId);
        args.getArg("headers").set("apiSecretAccessKey", apiSecretAccessKey);

        System.out.println("========== CONNECT SERVICE ENDPOINT ============");

        ZAPIResp response;
        try {
            response = zClient.post(args);
            System.out.println(response.toJSONString());
            if ((Integer) response.get("httpStatusCode") == 200 && (Boolean) response.get("success")) {
                System.out.println("Connected");
                return true;
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            ZLogger.getInstance().log(ZUtils.stackTraceToString(e), ZConstants.LOG_BOTH);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            ZLogger.getInstance().log(ZUtils.stackTraceToString(e), ZConstants.LOG_BOTH);
        }

        return false;
    }

    public boolean isConnected(ZClient zClient) {
        return isConnected(zClient, null, null);
    }
}
