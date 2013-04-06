package com.example.wifilocatordemo.test;

import com.example.wifilocatordemo.WiFiDataBase;

import android.database.Cursor;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

public class WiFiDataBaseTest extends AndroidTestCase {
	  private static final String TEST_FILE_PREFIX = "test_";
	  private WiFiDataBase test;

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

	      test = new WiFiDataBase(context);
	      test.open();
	  }
	  Cursor c1;
	  String sTest;

	  public void testPreConditions() {
	      assertNotNull(test);
// test insertPlace function    
	      test.insertPlace("UET", "Strong");
	      c1 = test.getPlaceInfo("Strong");
	      sTest = test.getPlace("Strong");
	      assertEquals(c1.getString(0), "UET");
	      assertEquals(c1.getString(1), "Strong");

	      test.insertPlace("Hostal", "Weak");
	      c1 = test.getPlaceInfo("Weak");
	      assertEquals(c1.getString(0), "Hostal");
	      assertEquals(c1.getString(1), "Weak");
	      
	      test.insertPlace("Home", "veryStrong");
	      c1 = test.getPlaceInfo("veryStrong");
	      assertEquals(c1.getString(0), "Home");
	      assertEquals(c1.getString(1), "veryStrong");
	      
	      test.insertPlace("Ulis", "quiteStrong");
	      c1 = test.getPlaceInfo("quiteStrong");
	      assertEquals(c1.getString(0), "Ulis");
	      assertEquals(c1.getString(1), "quiteStrong");
	      
	      test.insertPlace("Android", "Fun");
	      c1 = test.getPlaceInfo("Fun");
	      assertEquals(c1.getString(0), "Android");
	      assertEquals(c1.getString(1), "Fun");
	      
// test DeletePlace Function
	      test.deletePlace("Strong");
	      sTest = test.getPlace("Strong");
	      assertTrue(sTest == null);
	    	  
	      
	  }
	  @Override
	  
	  protected void tearDown() throws Exception {
	      super.tearDown();
	      test.close();
	      test = null;
	  }



}
