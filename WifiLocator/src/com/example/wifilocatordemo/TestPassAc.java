/* 
 * */
package com.example.wifilocatordemo;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
//import android.widget.TextView;
import android.widget.Toast;

public class TestPassAc extends Activity{
	private Button btChange;
	private Button btOKChange;
	private Button btOKPass;
	private String test,pass;
	private EditText etHintText,etChange;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pass);
		etHintText = (EditText) findViewById(R.id.etHintText);//Enter password here
		etChange = (EditText) findViewById(R.id.etChange);//Enter new password here to change
		etChange.setHint("");
		
		
		btOKChange = (Button) findViewById(R.id.btOKChange);
		btOKPass = (Button) findViewById(R.id.btOKPass);
		btChange = (Button) findViewById(R.id.btChange);
		
		
		/*
		 * Default state of the field for changing password is false, 
		 * user cannot change password before enter the right password
		 */
		btOKChange.setEnabled(false);
		etChange.setEnabled(false);
		
		//Set activity when click button "OK" after type the password to continue this app
		btOKPass.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SharedPreferences appPrefs = 
			            getSharedPreferences("appPreferences", MODE_PRIVATE);
		        pass = appPrefs.getString("editTextPref", "");//Defaul password is "".
				test= etHintText.getText().toString();
				
				/*
				 * If the typed password matches the right password
				 * start new activity has name WifiActivity
				 */
				if(test.compareTo(pass) == 0){
					etHintText.setText("");
					startActivity(new Intent(TestPassAc.this,WifiActivity.class));
				}
				
				/*
				 * If the password is wrong, create a small window
				 * with the announcement. 
				 * Reset the field to enter password
				 */
				else {
					Toast.makeText(getApplicationContext(), "Incorrect ! Try retype password",
						Toast.LENGTH_SHORT).show();
					etHintText.setText("");
				}
			}
		});
		
		/*
		 * Set the activity when click button change password 
		 */
		btChange.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SharedPreferences appPrefs = 
			            getSharedPreferences("appPreferences", MODE_PRIVATE);
		        pass = appPrefs.getString("editTextPref", "");
				test= etHintText.getText().toString();
				
				/*
				 * If users enter the right password, they can change password
				 */
				if(test.compareTo(pass) == 0){
					etHintText.setText("");
					etHintText.setHint("");
					
					/*
					 * Set the state of field to enter password is false
					 * enable the field to change password
					 */
					etHintText.setEnabled(false);
					btOKPass.setEnabled(false);
					
					etChange.setEnabled(true);
					etChange.setHint("Enter New Password");
					btOKChange.setEnabled(true);
					
					/*
					 * Set the activity when user change password,
					 * reset the value of the password
					 */
					btOKChange.setOnClickListener(new OnClickListener() {	
						@Override
						public void onClick(View v) {
							SharedPreferences appPrefs = 
						            getSharedPreferences("appPreferences", MODE_PRIVATE);
							SharedPreferences.Editor prefsEditor = appPrefs.edit();
					        prefsEditor.putString("editTextPref", 
					        		etChange.getText().toString());
					        prefsEditor.commit();
					        
					        etChange.setText("");
					        etChange.setHint("");
					        etChange.setEnabled(false);
							btOKChange.setEnabled(false);
							
							etHintText.setHint("Enter your password");
							etHintText.setEnabled(true);
							btOKPass.setEnabled(true);
							
							
						}
					});
				}
				/*
				 * else, create a small window to notice again
				 */
				else {
					Toast.makeText(getApplicationContext(), "Incorrect ! Try retype password",
						Toast.LENGTH_SHORT).show();
					etHintText.setText("");
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.wifiinfo, menu);
		return true;
	}
	
}
