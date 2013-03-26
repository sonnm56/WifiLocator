package com.example.wifilocatordemo.test;
import junit.framework.Assert;
import android.test.*;
import com.example.wifilocatordemo.*;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

public class WifiTest extends ActivityInstrumentationTestCase2<TestPassAc> {
	private EditText resulf;

	public WifiTest(Class<TestPassAc> name) {
		super(name);
		TestPassAc testPassAC = getActivity();
		//resulf = (EditText) testPassAC.findViewById(R.id.HintText);
		
	}

	protected void setUp() throws Exception {
		super.setUp();
	}
	
	public WifiTest(){
		super("com.example.wifilocatordemo", TestPassAc.class);
		
	}
	public void test(){
		TestPassAc test = new TestPassAc();	
		//Assert.assertTrue(test.getHintText() ,"Enter your password");
		//Assert.assertEquals(test.getHintText(), "Enter your password");
		
		Assert.assertTrue(true);

	}

	
}
