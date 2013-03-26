package com.example.wifilocatordemo;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

//import android.widget.TextView;

public class WifiSecurity extends Activity {
	private Button btOK;
	private EditText etPass;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.security);
		btOK = (Button) findViewById(R.id.btOK);
		etPass = (EditText)findViewById(R.id.etPass);
		btOK.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(etPass.getText().toString().compareTo("123abc")==0)
					startActivity(new Intent(WifiSecurity.this,WifiActivity.class));
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
