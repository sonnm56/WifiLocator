/**
 * 
 */
package com.example.wifilocatordemo.test;

import com.example.wifilocatordemo.WifiInfomation;

import android.content.BroadcastReceiver;
import android.net.wifi.WifiManager;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ViewAsserts;
import android.widget.Button;
import android.widget.TextView;

/**
 * @author NgoNui
 *
 */
public class WifiInfomattionTest extends ActivityInstrumentationTestCase2<WifiInfomation> {

	WifiInfomation activity;
	WifiManager wifi;
	BroadcastReceiver receiver;

	TextView tvStatus;
	Button btScanWiFi;

	public WifiInfomattionTest(Class<WifiInfomation> name ) {
		super(name);
	}
	
	@SuppressWarnings("deprecation")
	public WifiInfomattionTest() {
		super("com.example.wifilocatordemo", WifiInfomation.class);
	}


	protected void setUp() throws Exception {
		  super.setUp();
		  activity = getActivity();
		  tvStatus = (TextView) activity.findViewById(com.example.wifilocatordemo.R.id.tvStatus);
		  btScanWiFi = (Button)activity.findViewById(com.example.wifilocatordemo.R.id.btScanWiFi);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	 public void testViewsCreated() {
		    assertNotNull(getActivity());
		    assertNotNull(tvStatus);
		    assertNotNull(btScanWiFi);
	}
	public void testViewsVisible() {
		    ViewAsserts.assertOnScreen(tvStatus.getRootView(), tvStatus);
		    ViewAsserts.assertOnScreen(btScanWiFi.getRootView(), btScanWiFi);
	}
	 public void testStartingEmpty() {
		    assertTrue( "Test Scan Button","Scan".equals(btScanWiFi.getText().toString()));
		    assertTrue("Test TextView", "WiFi Locator Demo".equals(tvStatus.getText().toString()));
	 }	  
}
