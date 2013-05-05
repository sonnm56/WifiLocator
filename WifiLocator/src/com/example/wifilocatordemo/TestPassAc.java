/* 
 * */
package com.example.wifilocatordemo;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
//import android.widget.TextView;
import android.widget.Toast;

public class TestPassAc extends Activity{
	private Button btOKChange;
	private Button btOKPass;
	private String test,pass;
	private EditText etHintText,etChange;
	
	@Override
	protected void onCreate(final Bundle bundle) {
		super.onCreate(bundle);
		Button btChange;
		setContentView(R.layout.pass);
		etHintText = (EditText) findViewById(R.id.etHintText);//Enter password here
		etChange = (EditText) findViewById(R.id.etChange);//Enter new password here to change
		
		
		btOKChange = (Button) findViewById(R.id.btOKChange);
		btOKPass = (Button) findViewById(R.id.btOKPass);
		btChange = (Button) findViewById(R.id.btChange);
		
		
		/*
		 * Default state of the field for changing password is false, 
		 * user cannot change password before enter the right password
		 */
		btOKChange.setVisibility(View.INVISIBLE);
		etChange.setVisibility(View.INVISIBLE);
		
		
		/*
		 * Set the activity when click button change password 
		 */
		btChange.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View view) {
				// TODO Auto-generated method stub
				final SharedPreferences appPrefs = 
			            getSharedPreferences("appPreferences", MODE_PRIVATE);
		        pass = appPrefs.getString("editTextPref", "");
				test= etHintText.getText().toString();
				
				/*
				 * If users enter the right password, they can change password
				 */
				if(test.compareTo(pass) == 0){
					
					/*
					 * Set the state of field to enter password is false
					 * enable the field to change password
					 */
					etHintText.setVisibility(View.INVISIBLE);
					btOKPass.setVisibility(View.INVISIBLE);
					etHintText.setText("");
					
					etChange.setVisibility(View.VISIBLE);
					etChange.setHint("Enter New Password");
					btOKChange.setVisibility(View.VISIBLE);
							
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
	//Set activity when click button "OK" after type the password to continue this app
	public void onClickOKPass(final View view) {
		// TODO Auto-generated method stub
		final SharedPreferences appPrefs = 
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
			
	/*
	 * Set the activity when user change password,
	 * reset the value of the password
	 */
	public void onClickOKChange(final View view) {
		final SharedPreferences appPrefs = 
				getSharedPreferences("appPreferences", MODE_PRIVATE);
		final SharedPreferences.Editor prefsEditor = appPrefs.edit();
		prefsEditor.putString("editTextPref", 
				etChange.getText().toString());
		prefsEditor.commit();
	        
		etChange.setVisibility(View.INVISIBLE);
		btOKChange.setVisibility(View.INVISIBLE);
		etChange.setText("");
			
		etHintText.setHint("Enter your password");
		etHintText.setVisibility(View.VISIBLE);
		btOKPass.setVisibility(View.VISIBLE);
			
			
	}
		
	
}
