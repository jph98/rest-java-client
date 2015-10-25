package com.zuora.examples;

import org.junit.Test;

import com.zuora.sdk.http.ZClient;

public class AccountServiceTest {
   static final String SAMPLE_ACCOUNT_KEY = "A00001069";
   
   @Test
   public void test_account_manager(){
      // Create a z_client
      ZClient zClient = new ZClient();

      // create an account resource manager
      AccountManager accountManager = new AccountManager(zClient);

      // Connect to the End Point using default tenant's credentials
      // and practice APIs
       ConnectionManager manager = new ConnectionManager();

       if (manager.isConnected(zClient)) {
        accountManager.getSummary(SAMPLE_ACCOUNT_KEY);
        accountManager.getDetails(SAMPLE_ACCOUNT_KEY);
        String accountNumber = accountManager.create();
        if (accountNumber != null) {
          accountManager.update(accountNumber);
          accountManager.createWithSubscription();
        }
      }

   }
}
