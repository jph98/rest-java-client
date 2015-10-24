/**
 * Copyright (c) 2013 Zuora Inc.
 * <p/>
 * Sample code to demonstrate how to use the Operations resources
 */

package com.zuora.sdk.samples;

import com.zuora.sdk.http.ZAPIArgs;
import com.zuora.sdk.http.ZAPIResp;
import com.zuora.sdk.http.ZClient;

public class OperationManager {

    static final String SAMPLE_ACCOUNT_KEY = "A00001069";
    private ZClient zClient;

    OperationManager(ZClient zClient) {
        this.zClient = zClient;
    }

    // invoice and collect payment from and account
    public void createBillRun(String accountKey) {
        ZAPIArgs args = new ZAPIArgs();
        args.set("uri", ResourceEndpoints.POST_INVOICE_COLLECT);

        args.set("reqBody", new ZAPIArgs());
        args.getArg("reqBody").set("accountKey", accountKey);
        args.getArg("reqBody").set("invoiceTargetDate", "2013-12-31");

        System.out.println("========== CREATE ACCOUNT BILL RUN ===========");

        try {
            ZAPIResp response = zClient.post(args);
            System.out.println(response.toJSONString());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

}
