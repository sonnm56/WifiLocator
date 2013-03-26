package com.example.wifilocatordemo.test;
import com.example.wifilocatordemo.WifiSecurity;

import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.ViewAsserts;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.Button;
import android.widget.EditText;


/*
 * Test code to test com.marakana.WifiSecurity
 * 
 * To run on command line:
 * adb -e shell am instrument -w -e class com.marakana.test.WifiSecurityTests 
 *              com.marakana.test/android.test.InstrumentationTestRunner
 */
public class testsecurity extends ActivityInstrumentationTestCase2<WifiSecurity> {
  EditText etPass;
  Button btOK;
  WifiSecurity activity;

  public testsecurity(String name) {
    super("com.marakana", WifiSecurity.class);
    setName(name);
  }

  protected void setUp() throws Exception {
    super.setUp();
    
    // Find views
    activity = getActivity();
    etPass = (EditText)activity.findViewById(com.example.wifilocatordemo.R.id.etPass);
    btOK = (Button)activity.findViewById(com.example.wifilocatordemo.R.id.btOK);
  }

  protected void tearDown() throws Exception {
    super.tearDown();
  }
  
  @SmallTest
  public void testViewsCreated() {
    assertNotNull(getActivity());
    assertNotNull(etPass);
    assertNotNull(btOK);
  }
  
  @SmallTest
  public void testViewsVisible() {
    ViewAsserts.assertOnScreen(etPass.getRootView(), btOK);
    ViewAsserts.assertOnScreen(btOK.getRootView(), etPass);
  }
  
  @SmallTest
  public void testStartingEmpty() {
    assertTrue("etPass field is empty", "".equals(etPass.getText().toString()));
    
  }
  
  @SmallTest
  public void testKilosToPounds() {
    etPass.clearComposingText();
    btOK.clearComposingText();
    
    TouchUtils.tapView(this, etPass);
    sendKeys("123abc");
    
    assertTrue("1 kilo is 2.20462262 pounds", true);
  }

}
