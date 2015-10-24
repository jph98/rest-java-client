package com.zuora.sdk.samples;

import org.junit.Test;

import com.zuora.sdk.http.ZAPIResp;
import com.zuora.sdk.http.ZClient;

public class MassUpdateManagerTest {

   static final String SAMPLE_MassUpdate_FILE = MassUpdateManager.class.getClassLoader().getResource("com/zuora/sdk/samples/CreateRevenueSchedulesTemplate.csv").getFile(); 

   @Test
   public void test_massUpdate(){
      // Create a Z_Client object
      ZClient zClient = new ZClient();

      // Create a z_client object and pass it to APIRepo
      MassUpdateManager massUpdateManager = new MassUpdateManager(zClient);

      // Connect to the End Point using public voidault tenant's credentials
      // and practice APIs
      if (new ConnectionManager().isConnected(zClient)) {
         String actionKey = massUpdateManager.create(SAMPLE_MassUpdate_FILE, "{actionType:CreateRevenueSchedule}");

        ZAPIResp resp = massUpdateManager.get(actionKey);
        resp.toJSONString();
        
        resp = massUpdateManager.stop(actionKey);
      }
   }

   
}
