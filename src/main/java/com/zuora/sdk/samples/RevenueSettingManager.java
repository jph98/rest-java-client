package com.zuora.sdk.samples;

import com.zuora.sdk.http.ZAPIArgs;
import com.zuora.sdk.http.ZAPIResp;
import com.zuora.sdk.http.ZClient;

public class RevenueSettingManager {
    private ZClient zClient;

    public RevenueSettingManager(ZClient zClient) {
        this.zClient = zClient;
    }

    // GET REVENUE EVENT DETAIL
    public void getRevenueAutomationDate() {
        ZAPIArgs args = new ZAPIArgs();
        args.set("uri", ResourceEndpoints.GET_REVENUE_AUTOMATION_START_DATE);

        System.out.println("========== GET REVENUE AUTOMATION START DATE ============");

        try {
            ZAPIResp response = zClient.get(args);
            System.out.print(response.toJSONString());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }
}
