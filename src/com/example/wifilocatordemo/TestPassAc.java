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
	private Button btChange,btOKChange;
	private Button btOKPass;
	private String test,pass;
	private EditText etHintText,etChange;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pass);
		etHintText = (EditText) findViewById(R.id.etHintText);
		etChange = (EditText) findViewById(R.id.etChange);
		etChange.setHint("");
		etChange.setEnabled(false);
		
		btOKChange = (Button) findViewById(R.id.btOKChange);
		btOKChange.setEnabled(false);
		btOKPass = (Button) findViewById(R.id.btOKPass);
		btChange = (Button) findViewById(R.id.btChange);
		
		btOKPass.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SharedPreferences appPrefs = 
			            getSharedPreferences("appPreferences", MODE_PRIVATE);
		        pass = appPrefs.getString("editTextPref", "");
				test= etHintText.getText().toString();
				if(test.compareTo(pass) == 0){
					etHintText.setText("");
					startActivity(new Intent(TestPassAc.this,WifiActivity.class));
				}
				else {
					Toast.makeText(getApplicationContext(), "Incorrect ! Try retype password",
						Toast.LENGTH_SHORT).show();
					etHintText.setText("");
				}
			}
		});
		btChange.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SharedPreferences appPrefs = 
			            getSharedPreferences("appPreferences", MODE_PRIVATE);
		        pass = appPrefs.getString("editTextPref", "");
				test= etHintText.getText().toString();
				if(test.compareTo(pass) == 0){
					etHintText.setText("");
					etHintText.setHint("");
					etHintText.setEnabled(false);
					btOKPass.setEnabled(false);
					
					etChange.setEnabled(true);
					etChange.setHint("Enter New Password");
					btOKChange.setEnabled(true);
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
