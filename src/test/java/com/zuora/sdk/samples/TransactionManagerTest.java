package com.zuora.sdk.samples;

import org.junit.Test;

import com.zuora.sdk.http.ZAPIResp;
import com.zuora.sdk.http.ZClient;

public class TransactionManagerTest {
   static final String SAMPLE_ACCOUNT_KEY = "A00001069";
   
   @Test
   public void test_transaction(){
      // create a Z_Client
      ZClient zClient = new ZClient();

      // Create a z_client object and pass it to APIRepo
      TransactionManager transactionManager = new TransactionManager(zClient);

      // Connect to the End Point using default tenant's credentials
      // and practice APIs
      ZAPIResp resp;
      if ( new ConnectionManager().isConnected(zClient)) {
        resp = transactionManager.getInvoices(SAMPLE_ACCOUNT_KEY);

        // follow nextPage if present
        while (resp != null) {
          String nextPageLink = (String)resp.get("nextPage");
          if (nextPageLink == null) {
            resp = null;
          } else {
            resp = transactionManager.getInvoices(SAMPLE_ACCOUNT_KEY, nextPageLink);
          }
        }
        resp = transactionManager.getPayments(SAMPLE_ACCOUNT_KEY);
        // follow nextPage if present
        while (resp != null) {
          String nextPageLink = (String)resp.get("nextPage");
          if (nextPageLink == null) {
            resp = null;
          } else {
            resp = transactionManager.getPayments(SAMPLE_ACCOUNT_KEY, nextPageLink);
          }
        }
      }
   }
}
