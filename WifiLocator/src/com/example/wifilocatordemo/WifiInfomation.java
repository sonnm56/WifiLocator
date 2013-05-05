package com.example.wifilocatordemo;

import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class WifiInfomation extends Activity {
	private static final String TAG = "WifiInfo";
	private WifiManager wifi;
	private BroadcastReceiver receiver;

	private TextView tvStatus;
	private Button btScanWiFi;
	private StringBuilder sbStatus = new StringBuilder();


	/** Called when the activity is first created. */
	@Override
	public void onCreate(final Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.wifiinfo);

		tvStatus = (TextView) findViewById(R.id.tvStatus);
		btScanWiFi = (Button) findViewById(R.id.btScanWiFi);
		
		
	}
	public void onResume(){
		super.onResume();
		btScanWiFi.setOnClickListener(new View.OnClickListener() {
			public void onClick(final View view) {
				wifi.setWifiEnabled(true);
				Toast.makeText(getApplicationContext(), "Scanning!!!",
						Toast.LENGTH_LONG).show();

				if (view.getId() == R.id.btScanWiFi) {
					Log.d(TAG, "onClick() wifi.startScan()");
					wifi.startScan();
				}
			}

		});
		wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		wifi.setWifiEnabled(true);
		
		if (receiver == null){
			receiver = new WiFiScanReceiver();
		}

		registerReceiver(receiver, new IntentFilter(
				WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		Log.d(TAG, "onCreate()");

	}
	
	public void onDestroy() {
		unregisterReceiver(receiver);
		wifi.setWifiEnabled(false);
		super.onDestroy();	
	}
	
	class WiFiScanReceiver extends BroadcastReceiver {
		
		@Override
		public void onReceive(final Context context,final Intent intent) {
		  	 sbStatus = new StringBuilder();
		  	 final List<ScanResult> results = wifi.getScanResults();
		     for(int i = 0; i < results.size(); i++){
		  		 sbStatus.append(results.get(i).toString());
		  		 sbStatus.append("\n");
		  	 }
		     tvStatus.setText(sbStatus);
			
		}

	}
}


