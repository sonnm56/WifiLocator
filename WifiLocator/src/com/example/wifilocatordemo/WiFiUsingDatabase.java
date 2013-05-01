/*
 * This activity will show you the place
 * and you can add, edit , load or save...
 * */

package com.example.wifilocatordemo;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;

import android.content.Intent;
import android.content.IntentFilter;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;

import android.view.inputmethod.InputMethodManager;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import android.widget.PopupWindow;

import android.widget.TextView;


public class WiFiUsingDatabase extends Activity {
	WifiManager wifi;
	BroadcastReceiver receiver;
	
	TextView tvPlace;
	Button btStartFind;
	Button btAddPlace;
	String newPlace;
	EditText etAddPlace;
	
	StringBuilder sbWifiList = new StringBuilder();
	List<ScanResult> results;
	LocalDatabase wifiDataBase;
	PopupWindow popupWindow;
	int timeDelayNotFound;

	boolean click;
	
	static final int MAX_SIZE_WILIST = 4;
	static final int NUMBER_WIFI_INFO = 2;
	
	//initial variable when start
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.findplace);

		tvPlace = (TextView) findViewById(R.id.tvPlace);
		btStartFind = (Button) findViewById(R.id.btStartFind);
		btAddPlace = (Button) findViewById(R.id.btAddPlace);
		
		wifiDataBase = new LocalDatabase(this,"XYm");
		wifiDataBase.open();
		click = false;
		timeDelayNotFound = 0;
	}
	
	// When this activity work
	@Override
	public void onResume(){
		super.onResume();
		wifiDataBase.open();
				
		wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		wifi.setWifiEnabled(true);
			
		if (receiver == null){
			receiver = new WiFiScanReceiver();
		}
		registerReceiver(receiver, new IntentFilter(
				WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
	     
	}
	
	//When this activity close
	public void onDestroy() {
		unregisterReceiver(receiver);
		wifi.setWifiEnabled(false);
		wifiDataBase.close();
		super.onDestroy();	
	}

	//---Receive information, and use ---
	/*
	 * This class will receive wifi information
	 * and use it to show the place
	 * Before 
	 * */	
	class WiFiScanReceiver extends BroadcastReceiver {
		
		@Override
		public void onReceive(Context context, Intent intent) {
		  	 sbWifiList = new StringBuilder();
		  	 results = wifi.getScanResults();
		  	 
		  	 Functions.rankWifiListBSSID(results);
		  	 String placeList =wifiDataBase.getPlace(Functions.makeWifiBSSID(results),
		  			 										Functions.makeListWifiLevel(results));
		  	 if(!placeList.equals("Not Found")){
		  		tvPlace.setText(placeList);
		  		timeDelayNotFound = 0;
		  	 }else{
		  		timeDelayNotFound++;
		  		if(timeDelayNotFound == 10||tvPlace.getText().toString().equals("")){
		  			timeDelayNotFound = 0;
		  			tvPlace.setText(placeList);
		  		}
		  	 }
		
		}
	}
	
	
//------------------On Click Functions-------------
/*
 * we have some button here:
 * Find =  onClickStartFind to find place
 * Add Place = onClickAddPlace to add new place
 * Import = onClickImport to load informations from file
 * Export = onClickExport to save informations to file
 * */
	
	public void onClickStartFind(View view){
		wifi.setWifiEnabled(true);
		if (view.getId() == R.id.btStartFind) {
			final Timer ttt = new Timer();
			ttt.schedule(
					new TimerTask() {
						@Override
						public void run() {
							if(!click) wifi.startScan();
							else ttt.cancel();
						}
					},0,1000);
		}
	}
	
	public void onClickAddPlace(View view){
		if(!click){
			click = true;
        
			//---Create a popUp window to input new place---
			LayoutInflater layoutInflater 
            	= (LayoutInflater)getBaseContext()
            		.getSystemService(LAYOUT_INFLATER_SERVICE);  
			View popupView = layoutInflater.inflate(R.layout.addplacepopup, null);  
			popupWindow = new PopupWindow(popupView,450,250); 
			popupWindow.setTouchable(true);
			popupWindow.setFocusable(true);
			popupWindow.setOutsideTouchable(true);
                    
			etAddPlace = (EditText) popupView.findViewById(R.id.etAddPlace);
			etAddPlace.setHint("enter the place");
			InputMethodManager imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			if(imm != null) 
				imm.showSoftInput(etAddPlace, 0); 
            		
			popupWindow.showAsDropDown(btAddPlace,-450,100);
		}	
	}	
	
	public void onClickImport(View view){
		try {
			wifiDataBase.importDataBase();
			Toast.makeText(getApplicationContext(), "sucessful!",
					Toast.LENGTH_SHORT).show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Toast.makeText(getApplicationContext(), "unsucessful!",
					Toast.LENGTH_SHORT).show();
		}
	}
	
	public void onClickExport(View view){
		try {
			wifiDataBase.exportDataBase();
			Toast.makeText(getApplicationContext(), "sucessful!",
					Toast.LENGTH_SHORT).show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Toast.makeText(getApplicationContext(), "unsucessful!",
					Toast.LENGTH_SHORT).show();
		}
	}
	
//---OnClick Functions in popup AddPlace---
	public void onClickCancelAddPlace(View view){
			// TODO Auto-generated method stub
		etAddPlace.setText("");
		click = false;
		final Timer ttt = new Timer();
		ttt.schedule(
				new TimerTask() {
					@Override
					public void run() {
						if(!click){
							wifi.startScan();
						}
						else{
							ttt.cancel();
						}
					}
				},0,1000);
    	
		popupWindow.dismiss();
	}
	
	public void onClickOKAdd(View view){                				
		newPlace = etAddPlace.getText().toString();
		etAddPlace.setText("");
		wifiDataBase.insertPlace(newPlace,results );
		
		click = false;
		final Timer ttt = new Timer();
		ttt.schedule(
				new TimerTask() {
					@Override
					public void run() {
						if(!click) wifi.startScan();
						else ttt.cancel();
					}
				},0,1000);
		tvPlace.setText(newPlace);
		popupWindow.dismiss();
	}
	
}


