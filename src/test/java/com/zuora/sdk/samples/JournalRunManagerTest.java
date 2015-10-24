package com.zuora.sdk.samples;

import org.junit.Test;

import com.zuora.sdk.http.ZClient;

public class JournalRunManagerTest {
	  static final String SAMPLE_JOURNAL_RUN_KEY = "JR-00000004";
	   
	   @Test
	   public void test_create_journal_run(){
	      ZClient zClient = new ZClient();

	      // create a journal run resource manager
	      JournalRunManager jrManager = new JournalRunManager(zClient);

	      // Connect to the End Point using default tenant's credentials
	      if (new ConnectionManager().isConnected(zClient)) {
	    	  jrManager.createJournalRun();
	      }
	   }
	   
	   
	   @Test
	   public void test_get_journal_run(){
	      ZClient zClient = new ZClient();

	      // create a journal run resource manager
	      JournalRunManager jrManager = new JournalRunManager(zClient);

	      // Connect to the End Point using default tenant's credentials
	      if (new ConnectionManager().isConnected(zClient)) {
	    	  jrManager.getJournalRun(SAMPLE_JOURNAL_RUN_KEY);
	      }
	   }
}
