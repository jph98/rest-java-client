/**
 * Copyright (c) 2013 Zuora Inc.
 * <p/>
 * Sample code to demonstrate how to use the Catalog resources
 */

package com.zuora.sdk.samples;

import com.zuora.sdk.http.ZAPIArgs;
import com.zuora.sdk.http.ZAPIResp;
import com.zuora.sdk.http.ZClient;

public class CatalogManager {
    private ZClient zClient;

    CatalogManager(ZClient zClient) {
        this.zClient = zClient;
    }

    // Get products in a catalog
    public ZAPIResp getProducts(String nextPage) {
        ZAPIArgs args = new ZAPIArgs();

        // Follow next page if present
        if (nextPage != null) {
            args.set("uri", nextPage);
        } else {
            args.set("uri", ResourceEndpoints.GET_PRODUCT_CATALOG);
            args.set("queryString", new ZAPIArgs());
            args.getArg("queryString").set("pageSize", 10);
        }

        System.out.println("========== GET PRODUCT CATALOG ============");

        ZAPIResp response = null;
        try {
            ZAPIResp resp = zClient.get(args);
            System.out.println(resp.toJSONString());
            if ((Integer) resp.get("httpStatusCode") == 200 && (Boolean) resp.get("success")) {
                response = resp;
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }

        return response;
    }

    public ZAPIResp getProducts() {
        return getProducts(null);
    }

}
