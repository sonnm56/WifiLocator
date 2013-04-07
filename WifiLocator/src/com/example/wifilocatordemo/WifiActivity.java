package com.example.wifilocatordemo;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class WifiActivity extends Activity {
	private Button btWiFiInformation;
	private Button btFindPlace;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wifi_locator);
		btWiFiInformation = (Button) findViewById(R.id.btWiFiInformation);
		
		btWiFiInformation.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(WifiActivity.this,WifiInfomation.class));
			}
		});
		btFindPlace = (Button) findViewById(R.id.btFindPlace);
		
		btFindPlace.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(WifiActivity.this,WiFiUsingDatabase.class));
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
