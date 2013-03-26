package com.example.wifilocatordemo;

//import android.widget.EditText;
import junit.framework.Assert;
import junit.framework.TestCase;

public class testDemo2 extends TestCase {
	void testThis(){
//	EditText HintText;
	TestPassAc test = new TestPassAc();	
	//Assert.assertTrue(test.getHintText() ,"Enter your password");
	Assert.assertEquals(test.getHintText(), "Enter your password");
	Assert.assertNotNull(getClass());
	Assert.assertTrue(true);
	}
}
