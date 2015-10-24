package com.zuora.sdk.samples;

import org.junit.Test;

import com.zuora.sdk.http.ZClient;

public class RevenueItemManagerTest {
   static final String SAMPLE_CHARGE_REVENUE_SUMMARY_KEY = "CRS-00000001";
   static final String SAMPLE_REVENUE_SCHEDULE_KEY = "RS-00000001";
   static final String SAMPLE_REVENUE_EVENT_KEY = "RE-00000001";
   
   @Test
   public void test_revenue_item_manager(){
      // Create a z_client
      ZClient zClient = new ZClient();

      // create an revenue item resource manager
      RevenueItemManager riManager = new RevenueItemManager(zClient);

      // Connect to the End Point using default tenant's credentials
      if (new ConnectionManager().isConnected(zClient)) {
         riManager.getItemsByChargeRevenueSummary(SAMPLE_CHARGE_REVENUE_SUMMARY_KEY);
         riManager.getItemsByRevenueSchedule(SAMPLE_REVENUE_SCHEDULE_KEY);
         riManager.getItemsByRevenueEvent(SAMPLE_REVENUE_EVENT_KEY);
      }

   }
}
