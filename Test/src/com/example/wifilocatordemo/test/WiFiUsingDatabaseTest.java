package com.example.wifilocatordemo.test;
import android.test.*;

import com.example.wifilocatordemo.WiFiUsingDatabase;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class WiFiUsingDatabaseTest extends ActivityInstrumentationTestCase2<WiFiUsingDatabase> {
	private WiFiUsingDatabase activity;
	
	TextView tvPlace;
	Button btStartFind;
	Button btAddPlace;
	EditText etAddPlace;
	Button btExport;

	public WiFiUsingDatabaseTest(Class<WiFiUsingDatabase> name) {
		super(name);	
	}
	
	@SuppressWarnings("deprecation")
	public WiFiUsingDatabaseTest(){
		super("com.example.wifilocatordemo", WiFiUsingDatabase.class);
		
	}
	protected void setUp() throws Exception {
		super.setUp();
		activity = getActivity();
	    btStartFind = (Button)activity.findViewById(com.example.wifilocatordemo.R.id.btStartFind);
	    btAddPlace = (Button)activity.findViewById(com.example.wifilocatordemo.R.id.btAddPlace); 
	    btExport = (Button)activity.findViewById(com.example.wifilocatordemo.R.id.btExport); 
	}
	protected void tearDown() throws Exception {
	    super.tearDown();
 
	}
	  
	  @SmallTest
	  public void testViewsCreated() {
	    assertNotNull(getActivity());
	    assertNotNull(btStartFind);
	    assertNotNull(btAddPlace);
	    assertNotNull(btExport);
	 }
	  
	  @SmallTest
	  public void testViewsVisible() {
		  ViewAsserts.assertOnScreen(btStartFind.getRootView(), btStartFind);
		  ViewAsserts.assertOnScreen(btAddPlace.getRootView(), btAddPlace);  
		  ViewAsserts.assertOnScreen(btExport.getRootView(), btExport);
	  }  
	  @SmallTest
	  public void testStartingEmpty() {
		  assertTrue("test button startFind", "Start".equals(btStartFind.getText().toString()));
		  assertTrue("Add Place".equals(btAddPlace.getText().toString()));
		  assertTrue("Export".equals(btExport.getText().toString()));
	  }
	  
	  @SmallTest
	  public void testKilosToPounds() {
	  }
	

	
}
