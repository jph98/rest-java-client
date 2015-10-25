/**
 * Copyright (c) 2013 Zuora Inc.
 * <p/>
 * Sample code to demonstrate how to use the Accounts resources
 */

package com.zuora.sdk.services;

import com.zuora.examples.ConnectionManager;
import com.zuora.examples.ResourceEndpoints;
import com.zuora.sdk.http.ZAPIArgs;
import com.zuora.sdk.http.ZAPIResp;
import com.zuora.sdk.http.ZClient;
import com.zuora.sdk.utils.EncodeUtils;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Provides convenience services around the Zuroa API for integration.
 */
public class AccountService {

    private ZClient zClient;

    public AccountService(ZClient zClient) {
        this.zClient = zClient;
    }

    public String getSummary(String accountKey) {

        if (!isConnected()) throw new IllegalStateException("Not connected to Zuora API");

        String accountKeyEncoded = new EncodeUtils().encode(accountKey);

        ZAPIArgs args = new ZAPIArgs();
        args.set("uri", ResourceEndpoints.GET_ACCOUNT_SUMMARY.replace("{account-key}", accountKeyEncoded));

        try {
            ZAPIResp response = zClient.get(args);
            return response.toJSONString();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public String getDetails(String accountKey) {

        if (!isConnected()) throw new IllegalStateException("Not connected to Zuora API");

        String accountKeyEncoded = new EncodeUtils().encode(accountKey);

        ZAPIArgs args = new ZAPIArgs();
        args.set("uri", ResourceEndpoints.GET_ACCOUNT_DETAIL.replace("{account-key}", accountKeyEncoded));

        try {
            ZAPIResp response = zClient.get(args);
            return response.toJSONString();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    /**
     * TODO: Create an account given a wrapper object
     */
    public String create() {
        throw new UnsupportedOperationException();
    }

    /**
     * TODO: Update an account given a wrapper object
     */
    public void update(String accountKey) {
        throw new UnsupportedOperationException();
    }

    /**
     * TODO: Create with subscription, need wrapper object
     */
    public void createWithSubscription() {
        throw new UnsupportedOperationException();
    }

    private boolean isConnected() {

        return new ZConnectionUtil().isConnected(zClient);
    }
}
