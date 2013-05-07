/*
 * This class is the main class of this app
 * It has some links to move to the some
 * thinks like information of this app
 * how to use,...
 * */
package com.example.wifilocatordemo;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class WifiActivity extends Activity {
	@Override
	protected void onCreate(final Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.wifi_locator);
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.wifiinfo, menu);
		return true;
	}

	// ---links to go to functions---
	public void onClickWiFiInformation(final View view) {
		startActivity(new Intent(WifiActivity.this, WifiInfomation.class));
	}

	public void onClickFindPlace(final View view) {
		startActivity(new Intent(WifiActivity.this, WiFiUsingDatabase.class));
	}

	public void onClickGuide(final View view) {
		startActivity(new Intent(WifiActivity.this, Guide.class));
	}
}
