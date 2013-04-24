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
	
	StringBuilder sb = new StringBuilder();
	WiFiDataBase wifiDataBase;
	PopupWindow popupWindow;

	boolean click;
	
	final int MAX_SIZE_WIFI_LIST = 1;
	final int NUMBER_WIFI_INFO = 1;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.findplace);

		tvPlace = (TextView) findViewById(R.id.tvPlace);
		btStartFind = (Button) findViewById(R.id.btStartFind);
		btAddPlace = (Button) findViewById(R.id.btAddPlace);
		
		wifiDataBase = new WiFiDataBase(this,"XYz");
		wifiDataBase.open();
	}
	public void onStart(){
		super.onStart();
		
	}
	public void onResume(){
		super.onResume();
		wifiDataBase.open();
				
		wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		wifi.setWifiEnabled(true);
			
		if (receiver == null)
			receiver = new WiFiScanReceiver();

		registerReceiver(receiver, new IntentFilter(
				WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
	     
	}
	
	@Override
	public void onStop() {
		super.onStop();	
	}
	public void onDestroy() {
		unregisterReceiver(receiver);
		wifi.setWifiEnabled(false);
		wifiDataBase.close();
		super.onDestroy();	
	}
	
	class WiFiScanReceiver extends BroadcastReceiver {
		
		@Override
		public void onReceive(Context c, Intent intent) {
		  	 sb = new StringBuilder();
		  	 List<ScanResult> results = wifi.getScanResults();
		  	 
		  	 int t = (results.size()<MAX_SIZE_WIFI_LIST)? results.size():MAX_SIZE_WIFI_LIST;
		  	 rankWifiListLevel(results,t);
		  	 rankWifiListBSSID(results,t);
		  	 if(NUMBER_WIFI_INFO<t){
		  		 for(int i = 0; i <NUMBER_WIFI_INFO; i++){
		  			 sb.append((results.get(i)).BSSID.toString());
		  			 sb.append("|");
		  			 sb.append((int)(results.get(i)).level/5);
		  			 sb.append("|\n");
		  		 }
		  		 for(int i = NUMBER_WIFI_INFO; i <t; i++){
		  			 sb.append((results.get(i)).BSSID.toString());
		  			 sb.append("|\n");
		  		 }
		  	 }
		  	 else for(int i = 0; i <t; i++){
	  			 sb.append((results.get(i)).BSSID.toString());
	  			 sb.append("|");
	  			 sb.append((int)(results.get(i)).level/5);
	  			 sb.append("|\n");
	  		 }
		     tvPlace.setText(sb.toString());
		     String place =wifiDataBase.getPlace(sb.toString());
		     
		     if(!place.equals("Not Found")) tvPlace.setText(place);
		    		
		}

	}
	
	
//------------------On Click Functions-------------
	
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
		click = true;
             	
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

	public void onClickCancelAddPlace(View view){
			// TODO Auto-generated method stub
		etAddPlace.setText("");
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
    	
		popupWindow.dismiss();
	}
	
	public void onClickOKAdd(View view){                				
		newPlace = etAddPlace.getText().toString();
		etAddPlace.setText("");
		wifiDataBase.insertPlace(newPlace, sb.toString());

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
	public void onClickImport(View view){
		try {
			wifiDataBase.dbHelper.importDataBase();
			Toast.makeText(getApplicationContext(), "sucessful!",
					Toast.LENGTH_SHORT).show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), "unsucessful!",
					Toast.LENGTH_SHORT).show();
		}
	}
	public void onClickExport(View view){
		try {
			wifiDataBase.dbHelper.exportDataBase();
			Toast.makeText(getApplicationContext(), "sucessful!",
					Toast.LENGTH_SHORT).show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), "unsucessful!",
					Toast.LENGTH_SHORT).show();
		}
	}
//---------------Rank Wifi List Functions---------	
	public void rankWifiListBSSID(List<ScanResult> wifiList, int numberOfBSSIDWifi){
	
		int number = (wifiList.size()<numberOfBSSIDWifi)? wifiList.size():numberOfBSSIDWifi;
		for(int i = 0; i < number; i++){
			 for(int j = i+1; j < number; j++){
		  		if((wifiList.get(i).BSSID.toString()).compareTo(wifiList.get(j).BSSID.toString())>0){
		  			
		  			ScanResult change;
		  			change = wifiList.get(i);
		  			wifiList.set(i, wifiList.get(j));
		  			wifiList.set(j, change);	  		
		  		}
		  	 }
		 }   
	}
	
	public void rankWifiListLevel(List<ScanResult> wifiList,int numberOfLevelWifi){
		
		int number = (wifiList.size()<numberOfLevelWifi)? wifiList.size():numberOfLevelWifi;
		for(int i = 0; i < number; i++){
			for(int j = i+1; j < wifiList.size(); j++){
				if(wifiList.get(i).level < wifiList.get(j).level){
			  			
					ScanResult change;
					change = wifiList.get(i);
					wifiList.set(i, wifiList.get(j));
					wifiList.set(j, change);
			  		
				}
			}
		}
	}
}


