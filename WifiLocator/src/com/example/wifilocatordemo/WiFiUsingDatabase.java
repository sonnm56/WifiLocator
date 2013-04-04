package com.example.wifilocatordemo;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class WiFiUsingDatabase extends Activity {
	private static final String TAG = "WifiInfo";
	WifiManager wifi;
	BroadcastReceiver receiver;
	
	TextView tvPlace;
	Button btStartFind;
	Button btAddPlace;
	String newPlace;
	EditText etAddPlace;
	
	StringBuilder sb = new StringBuilder();
	WiFiDataBase WFdb;
	PopupWindow popUp;
	
	
	boolean click;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.findplace);

		tvPlace = (TextView) findViewById(R.id.tvPlace);
		btStartFind = (Button) findViewById(R.id.btStartFind);
		btAddPlace = (Button) findViewById(R.id.btAddPlace);
		
		
		WFdb = new WiFiDataBase(this);
		
	}
	public void onStart(){
		super.onStart();
		
	}
	public void onResume(){
		super.onResume();
		btStartFind.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				wifi.setWifiEnabled(true);

				if (view.getId() == R.id.btStartFind) {
					Log.d(TAG, "onClick() wifi.startScan()");
					final Timer ttt = new Timer();
					ttt.schedule(
							new TimerTask() {
								@Override
								public void run() {
									if(!click)
										wifi.startScan();
									else{
										ttt.cancel();
									}
								}
							},0,1000);
				}
			}

		});

		btAddPlace.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
            	click = true;
                 	
            	LayoutInflater layoutInflater 
                = (LayoutInflater)getBaseContext()
                 .getSystemService(LAYOUT_INFLATER_SERVICE);  
               View popupView = layoutInflater.inflate(R.layout.addplacepopup, null);  
                        final PopupWindow popupWindow = new PopupWindow(popupView,350,350); 
                        popupWindow.setTouchable(true);
                        popupWindow.setFocusable(true);
                        popupWindow.setOutsideTouchable(true);
                        Button btOKAdd = (Button) popupView.findViewById(R.id.btOKAdd);
                        Button btCancelAdd = (Button) popupView.findViewById(R.id.btCancelAdd);
                        etAddPlace = (EditText) popupView.findViewById(R.id.etAddPlace);
                		InputMethodManager imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                		if(imm != null) {
                		    imm.showSoftInput(etAddPlace, 0); 
                		}


                		btCancelAdd.setOnClickListener(new Button.OnClickListener(){

                			@Override
                			public void onClick(View v) {
                				// TODO Auto-generated method stub
                				etAddPlace.setText("");
                	    		  click = false;
                	    		  final Timer ttt = new Timer();
                					ttt.schedule(
                							new TimerTask() {
                								@Override
                								public void run() {
                									if(!click)
                										wifi.startScan();
                									else{
                										ttt.cancel();
                									}
                								}
                							},0,1000);
                	    	
                				popupWindow.dismiss();
                			}});
                		btOKAdd.setOnClickListener(new Button.OnClickListener(){

                			@Override
                			public void onClick(View v) {
                				// TODO Auto-generated method stub
                				newPlace = etAddPlace.getText().toString();
                				etAddPlace.setText("");
                				
                				WFdb.open(); 
                				WFdb.insertPlace(newPlace, sb.toString());
                				WFdb.close();
                				
                    			click = false;
                    			final Timer ttt = new Timer();
                				ttt.schedule(
                						new TimerTask() {
                							@Override
                							public void run() {
                								if(!click)
                									wifi.startScan();
                									
                								else{
                									ttt.cancel();
                								}
                							}
                						},0,1000);
                				tvPlace.setText(newPlace);
                				
                				popupWindow.dismiss();
                			}});

                        popupWindow.showAsDropDown(btAddPlace,0,0);
                    
              }});	
		
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
		
		super.onDestroy();	
	}
	
	class WiFiScanReceiver extends BroadcastReceiver {
		
		@Override
		public void onReceive(Context c, Intent intent) {
		  	 sb = new StringBuilder();
		  	 List<ScanResult> results = wifi.getScanResults();
		  	 rankWifiList(results);
		     for(int i = 0; i < results.size(); i++){
		  		sb.append((results.get(i)).BSSID.toString());
		  		sb.append("|");
		  		sb.append((int)(results.get(i)).level/5);
		  		sb.append("|\n");
		  	 }
		     tvPlace.setText(sb.toString());
		     WFdb.open(); 
		     Cursor c1 =WFdb.getPlace(sb.toString());
		     WFdb.close();
		     if(c1.moveToFirst())
		    	 tvPlace.setText(c1.getString(0));
		    /*	if (c1.moveToFirst())
					do {
					Toast.makeText(getBaseContext(),"id: " + c1.getString(0) + "\n" +
			                "Name: " + c1.getString(1) + "\n",
			                Toast.LENGTH_LONG).show();	        
				
	                
	                } while (c1.moveToNext());*/
				
		}

	}
	public void rankWifiList(List<ScanResult> wifiList){
		 for(int i = 0; i < wifiList.size(); i++){
			 for(int j = i; j < wifiList.size(); j++){
		  		if((wifiList.get(i).BSSID.toString()).compareTo(wifiList.get(j).BSSID.toString())>0){
		  			
		  			ScanResult change;
		  			change = wifiList.get(i);
		  			wifiList.set(i, wifiList.get(j));
		  			wifiList.set(j, change);
		  		
		  		}
		  	 }
		 }   
	}
}


