package com.example.wifilocatordemo;

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
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;

import android.view.inputmethod.InputMethodManager;

import android.widget.Button;
import android.widget.EditText;

import android.widget.PopupWindow;

import android.widget.TextView;

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
	PopupWindow popupWindow;

	boolean click;

	int MAX_SIZE_WIFI_LIST = 5;
	int NUMBER_WIFI_INFO = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.findplace);

		tvPlace = (TextView) findViewById(R.id.tvPlace);
		btStartFind = (Button) findViewById(R.id.btStartFind);
		btAddPlace = (Button) findViewById(R.id.btAddPlace);

		WFdb = new WiFiDataBase(this);
		WFdb.open();
	}

	public void onStart() {
		super.onStart();

	}

	public void onResume() {
		super.onResume();
		WFdb.open();

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
		WFdb.close();
		super.onDestroy();
	}

	class WiFiScanReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context c, Intent intent) {
			sb = new StringBuilder();
			List<ScanResult> results = wifi.getScanResults();

			int t = (results.size() < MAX_SIZE_WIFI_LIST) ? results.size()
					: MAX_SIZE_WIFI_LIST;
			rankWifiListLevel(results, t);
			rankWifiListBSSID(results, t);
			if (NUMBER_WIFI_INFO < t) {
				for (int i = 0; i < NUMBER_WIFI_INFO; i++) {
					sb.append((results.get(i)).BSSID.toString());
					sb.append("|");
					sb.append((int) (results.get(i)).level / 5);
					sb.append("|\n");
				}
				for (int i = NUMBER_WIFI_INFO; i < 3; i++) {
					sb.append((results.get(i)).BSSID.toString());
					sb.append("|\n");
				}
			} else
				for (int i = 0; i < t; i++) {
					sb.append((results.get(i)).BSSID.toString());
					sb.append("|");
					sb.append((int) (results.get(i)).level / 5);
					sb.append("|\n");
				}
			if (tvPlace.getText().toString().equals(""))
				tvPlace.setText("Not Found");
			String place = WFdb.getPlace(sb.toString());

			if (!place.equals("Not Found"))
				tvPlace.setText(place);

		}

	}

	// ------------------On Click Functions-------------

	// click button to find position
	public void onClickStartFind(View view) {
		wifi.setWifiEnabled(true);
		if (view.getId() == R.id.btStartFind) {
			Log.d(TAG, "onClick() wifi.startScan()");
			final Timer ttt = new Timer();
			ttt.schedule(new TimerTask() {
				@Override
				public void run() {
					if (!click)
						wifi.startScan();
					else
						ttt.cancel();
				}
			}, 0, 1000);
		}
	}

	// click button to add place you want to save
	public void onClickAddPlace(View view) {
		click = true;

		LayoutInflater layoutInflater = (LayoutInflater) getBaseContext()
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		View popupView = layoutInflater.inflate(R.layout.addplacepopup, null);
		popupWindow = new PopupWindow(popupView, 450, 250);
		popupWindow.setTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);

		etAddPlace = (EditText) popupView.findViewById(R.id.etAddPlace);
		etAddPlace.setHint("enter the place");
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null)
			imm.showSoftInput(etAddPlace, 0);

		popupWindow.showAsDropDown(btAddPlace, -450, 100);

	}

	// button to cancel adding place
	public void onClickCancelAddPlace(View view) {
		// TODO Auto-generated method stub
		etAddPlace.setText("");
		click = false;
		final Timer ttt = new Timer();
		ttt.schedule(new TimerTask() {
			@Override
			public void run() {
				if (!click)
					wifi.startScan();
				else
					ttt.cancel();
			}
		}, 0, 1000);
		popupWindow.dismiss();
	}

	// button to accept adding place
	public void onClickOKAdd(View view) {
		newPlace = etAddPlace.getText().toString();
		etAddPlace.setText("");
		WFdb.insertPlace(newPlace, sb.toString());
		click = false;
		final Timer ttt = new Timer();
		ttt.schedule(new TimerTask() {
			@Override
			public void run() {
				if (!click)
					wifi.startScan();
				else
					ttt.cancel();
			}
		}, 0, 1000);
		tvPlace.setText(newPlace);
		popupWindow.dismiss();
	}

	// ---------------Rank Wifi List Functions---------

	public void rankWifiListBSSID(List<ScanResult> wifiList, int number) {

		number = (wifiList.size() < number) ? wifiList.size() : number;
		for (int i = 0; i < number; i++) {
			for (int j = i + 1; j < number; j++) {
				if ((wifiList.get(i).BSSID.toString()).compareTo(wifiList
						.get(j).BSSID.toString()) > 0) {
					ScanResult change;
					change = wifiList.get(i);
					wifiList.set(i, wifiList.get(j));
					wifiList.set(j, change);
				}
			}
		}
	}

	public void rankWifiListLevel(List<ScanResult> wifiList, int number) {

		number = (wifiList.size() < number) ? wifiList.size() : number;
		for (int i = 0; i < number; i++) {
			for (int j = i + 1; j < wifiList.size(); j++) {
				if (wifiList.get(i).level < wifiList.get(j).level) {
					ScanResult change;
					change = wifiList.get(i);
					wifiList.set(i, wifiList.get(j));
					wifiList.set(j, change);
				}
			}
		}
	}
}
