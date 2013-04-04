package com.example.wifilocatordemo.test;

import com.example.wifilocatordemo.WiFiDataBase;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

public class WiFiDataBaseTest extends AndroidTestCase {
	  private static final String TEST_FILE_PREFIX = "test_";
	  private WiFiDataBase mMyAdapter;

	  static final String KEY_PLACE = "place";
	    static final String KEY_WIFI = "WiFiInfo";
	    static final String TAG = "WiFiDataBase";
	    static final String DATABASE_NAME = "WiFiDB";
	    static final String DATABASE_TABLE = "places";
	    static final int DATABASE_VERSION = 1;
	    static final String DATABASE_CREATE =
	        "create table places ("
	        + "place text not null, WiFiInfo text not null);";
	    
	    
	    
	    
	  @Override
	  protected void setUp() throws Exception {
	      super.setUp();

	      RenamingDelegatingContext context 
	          = new RenamingDelegatingContext(getContext(), TEST_FILE_PREFIX);

	      mMyAdapter = new WiFiDataBase(context);
	      mMyAdapter.open();
	  }

	  public void testPreConditions() {
	      assertNotNull(mMyAdapter);
	  }
	  
	  public void insertPlaceTest(){
		  
	  }
	  
	  public void deletePlaceTest(){
		  
	  }
	  public void getAllPlacesTest(){
		  
	  }
	  
	  public void getPlaceTest(){
		  
	  }
	  
	  public void updatePlace(){
		  
	  }
	  @Override
	  
	  protected void tearDown() throws Exception {
	      super.tearDown();

	      mMyAdapter.close();
	      mMyAdapter = null;
	  }



}
