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
// test insertPlaceInfo function    
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
	      test.deletePlace("UET");
	      sTest = test.getPlace("Strong");
	      assertEquals(sTest, "Not Found");
	      
	      test.deletePlace("Ulis");
	      sTest = test.getPlace("quiteStrong");
	      assertEquals(sTest, "Not Found");
	      
	      test.deletePlace("Home");
	      sTest = test.getPlace("veryStrong");
	      assertEquals(sTest, "Not Found");
	      
	      test.deletePlace("Hostal");
	      sTest = test.getPlace("Weak");
	      assertEquals(sTest, "Not Found");
	      
	      test.deletePlace("Android");
	      sTest = test.getPlace("Fun");
	      assertEquals(sTest, "Not Found");
	      
	      test.deletePlace("Phone");
	      sTest = test.getPlace("unclear");
	      assertEquals(sTest, "Not Found");
	      
//test getPlace
	      test.insertPlace("Face", "social");
	      sTest = test.getPlace("social");
	      assertEquals(sTest, "Face");
	      
	      test.insertPlace("E3", "sercurity");
	      sTest = test.getPlace("sercurity");
	      assertEquals(sTest, "E3");
	      
	      test.insertPlace("FastCafe", "can not be access");
	      sTest = test.getPlace("can not be access");
	      assertEquals(sTest, "FastCafe");
	      
	      test.insertPlace("Library", "public");
	      sTest = test.getPlace("public");
	      assertEquals(sTest, "Library");

	  }
	  @Override
	  
	  protected void tearDown() throws Exception {
	      super.tearDown();
	      test.close();
	      test = null;
	  }



}
