package com.example.wifilocatordemo.test;

import com.example.wifilocatordemo.WiFiDataBase;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

public class WiFiDataBaseTest extends AndroidTestCase {
	  private static final String TEST_FILE_PREFIX = "test_";
	  private WiFiDataBase mMyAdapter;

	  @Override
	  protected void setUp() throws Exception {
	      super.setUp();

	      RenamingDelegatingContext context 
	          = new RenamingDelegatingContext(getContext(), TEST_FILE_PREFIX);

	      mMyAdapter = new WiFiDataBase(context);
	      mMyAdapter.open();
	  }

	  @Override
	  protected void tearDown() throws Exception {
	      super.tearDown();

	      mMyAdapter.close();
	      mMyAdapter = null;
	  }

	  public void testPreConditions() {
	      assertNotNull(mMyAdapter);
	  }

}
