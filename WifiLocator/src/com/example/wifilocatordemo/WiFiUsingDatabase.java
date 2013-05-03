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
import android.app.ProgressDialog;

import android.content.BroadcastReceiver;
import android.content.Context;

import android.content.Intent;
import android.content.IntentFilter;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;


import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import android.widget.PopupWindow;

import android.widget.TextView;


public class WiFiUsingDatabase extends Activity {
	WifiManager wifi;
	BroadcastReceiver receiver;
	
	TextView tvGlobalPlace;
	TextView tvLocalPlace;
	Button btStartFind;
	Button btAddPlace;
	EditText etAddGlobal;
	EditText etAddLocal;
	
	StringBuilder sbWifiList = new StringBuilder();
	List<ScanResult> results;
	LocalDatabase localDataBase;
	WiFiDataBase wifiDataBase;
	PopupWindow popupWindow;
	int timeDelayNotFound;

	boolean click;
	
	static final int MAX_SIZE_WILIST = 4;
	static final int NUMBER_WIFI_INFO = 2;
	
	//initial variable when start
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.findplace);

		tvGlobalPlace = (TextView) findViewById(R.id.tvGlobalPlace);
		tvLocalPlace = (TextView) findViewById(R.id.tvLocalPlace);
		
		btStartFind = (Button) findViewById(R.id.btStartFind);
		btAddPlace = (Button) findViewById(R.id.btAddPlace);
		
		wifiDataBase = new WiFiDataBase(this, "globalplace");
		wifiDataBase.open();
		localDataBase = new LocalDatabase(this,"XYm");
		localDataBase.open();
		click = false;
		timeDelayNotFound = 0;
	}
	
	// When this activity work
	@Override
	public void onResume(){
		super.onResume();
		localDataBase.open();
				
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
		localDataBase.close();
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
		  	 String lovalPlaceList =localDataBase.getPlace(Functions.makeWifiBSSID(results),
		  			 										Functions.makeListWifiLevel(results));
		  	 String globalPlace = "Not Found";
		  	 for(int i=0;i<results.size();i++){
		  		globalPlace = wifiDataBase.getPlace(results.get(i).BSSID);
		  		if (!globalPlace.equals("Not Found")) break;
		  	 }
		  	 tvGlobalPlace.setText(globalPlace);
		  	 
		  	 if(!lovalPlaceList.equals("Not Found")){
		  		tvLocalPlace.setText(lovalPlaceList);
		  		timeDelayNotFound = 0;
		  	 }else{
		  		timeDelayNotFound++;
		  		if(timeDelayNotFound == 5||tvLocalPlace.getText().toString().equals("")){
		  			timeDelayNotFound = 0;
		  			tvLocalPlace.setText(lovalPlaceList);
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
							if(!click){
								wifi.startScan();
							}
							else{
								ttt.cancel();
							}
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
                    
			etAddGlobal = (EditText) popupView.findViewById(R.id.etAddGlobal);
			etAddGlobal.setHint("enter the global place");
			etAddLocal = (EditText) popupView.findViewById(R.id.etAddLocal);
			etAddLocal.setHint("enter the local place");
					
			popupWindow.showAsDropDown(btAddPlace,-450,100);
		}	
	}	
	
	public void onClickImport(View view){
		try {
			localDataBase.importDataBase();
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
			localDataBase.exportDataBase();
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
		
		localDataBase.insertPlace(etAddLocal.getText().toString(),results );
		for(int i=0;i<results.size();i++)
			wifiDataBase.insertPlace(etAddGlobal.getText().toString(),results.get(i).BSSID);
		
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
		tvGlobalPlace.setText(etAddGlobal.getText().toString());
		tvLocalPlace.setText(etAddLocal.getText().toString());
		popupWindow.dismiss();
	}
	
}


