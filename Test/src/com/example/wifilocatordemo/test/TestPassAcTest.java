package com.example.wifilocatordemo.test;
import android.test.*;
import com.example.wifilocatordemo.TestPassAc;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.Button;
import android.widget.EditText;

public class TestPassAcTest extends ActivityInstrumentationTestCase2<TestPassAc> {
	EditText etHintText;
	Button btOKPass;
	Button btChange;
	Button btOKChange;
	TestPassAc activity;
	

	public TestPassAcTest(Class<TestPassAc> name) {
		super(name);
		//TestPassAc testPassAC = getActivity();

		
	}
	@SuppressWarnings("deprecation")
	public TestPassAcTest(){
		super("com.example.wifilocatordemo", TestPassAc.class);
		
	}
	protected void setUp() throws Exception {
		super.setUp();
		activity = getActivity();
	    etHintText = (EditText)activity.findViewById(com.example.wifilocatordemo.R.id.etHintText);
	    btOKPass = (Button)activity.findViewById(com.example.wifilocatordemo.R.id.btOKPass);
	    btChange = (Button)activity.findViewById(com.example.wifilocatordemo.R.id.btChange); 
	}
	protected void tearDown() throws Exception {
	    super.tearDown();
	}
	  
	  @SmallTest
	  public void testViewsCreated() {
	    assertNotNull(getActivity());
	    assertNotNull(etHintText);
	    assertNotNull(btOKPass);
	    assertNotNull(btChange);
	  }
	  
	  @SmallTest
	  public void testViewsVisible() {
	    ViewAsserts.assertOnScreen(etHintText.getRootView(), btOKPass);
	    ViewAsserts.assertOnScreen(btOKPass.getRootView(), etHintText);
	    ViewAsserts.assertOnScreen(btChange.getRootView(), etHintText);
	  }
	  
	  @SmallTest
	  public void testStartingEmpty() {
	    assertTrue("etHintText field is empty", "".equals(etHintText.getText().toString()));
	    assertTrue("Test etHintText", "Enter your password".equals(etHintText.getHint().toString()));
	    assertTrue("Text in Button", "OK".equals(btOKPass.getText().toString()));
	    //assertTrue("Text in Button Change", "Change your password".equals(btChange.getText().toString()));
	    
	    
	  }
	  
	  @SmallTest
	  public void testKilosToPounds() {
	    etHintText.clearComposingText();
	    btOKPass.clearComposingText();
	    
	    TouchUtils.tapView(this, etHintText);
	    sendKeys("123abc");
	    
	    assertTrue("1 kilo is 2.20462262 pounds", true);
	  }
	

	
}
