package com.example.wifilocatordemo;

import java.util.List;

import android.net.wifi.ScanResult;

public class Functions {
	
	private final static int BSSID_NUMBER = 4;
	
	private final static int LEVELS_NUMBER = 4;
	
	//---------------Rank Wifi List Functions---------	
	public static void rankWifiListBSSID(List<ScanResult> wifiList){
		
		int number = (wifiList.size()<LEVELS_NUMBER)? wifiList.size():LEVELS_NUMBER;
		for(int i = 0; i < number; i++){
			for(int j = i+1; j < wifiList.size(); j++){
				if((wifiList.get(i).BSSID.toString()).compareTo(wifiList.get(j).BSSID.toString())>0){
			  
					ScanResult change;
					change = wifiList.get(i);
					wifiList.set(i, wifiList.get(j));
					wifiList.set(j, change);	  		
				}
			}
		}   
	}
		
	public static void rankWifiListLevel(List<ScanResult> wifiList){
			
		int number = (wifiList.size()<LEVELS_NUMBER)? wifiList.size():LEVELS_NUMBER;
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
	
	public static String makeWifiBSSID(List<ScanResult> wifiList){
		StringBuilder wifiBSSID;
		int number;
		
		wifiBSSID = new StringBuilder();
		number = (wifiList.size()<BSSID_NUMBER)? wifiList.size():BSSID_NUMBER;
		
		for(int i = 0; i < number; i++){
			wifiBSSID.append(wifiList.get(i).BSSID.toString()) ;
		}

		return wifiBSSID.toString();
	}
	public static int makeWifiLevel(List<ScanResult> wifiList){
		int wifiLevel = 0;
		int number;
		
		number = (wifiList.size()<BSSID_NUMBER)? wifiList.size():BSSID_NUMBER;
		
		for(int i = number-1; i >=0; i--){
			wifiLevel = wifiLevel*100 - wifiList.get(i).level;
		}

		return wifiLevel;
	}
	public static int[] makeListWifiLevel(int wifiLevel){
		int number = wifiLevel;
		int i = 0;
		int[] wifiLevelList = new int[4];
		while(number!=0){
			wifiLevelList[i] = - (number%100);
			number =(int) (number/100);
			i++;
		}
		return wifiLevelList;
	}
	public static int[] makeListWifiLevel(List<ScanResult> wifiList){
		int numberLevel = wifiList.size();
		int[] wifiLevelList = new int[numberLevel];
		for(int i=0;i<numberLevel;i++){
			wifiLevelList[i] = wifiList.get(i).level;
		}
		return wifiLevelList;
	}
	
	public static String getPlaceAndPercent(List<String> place, List<Integer> maxPercent){
		int otherPercent = 100;
		String sPlace = "";
		for(int i = 0;i<place.size();i++){ 
			otherPercent = otherPercent - maxPercent.get(i);
		}
		if(otherPercent >0){
			for(int i = 0;i<place.size();i++){ 
				sPlace = sPlace + place.get(i)+"\t"+maxPercent.get(i)+"\n";
			}
			sPlace = sPlace + "Other\t"+otherPercent+"\n";
		}
		else{
			for(int i = 0;i<place.size();i++){ 
				sPlace = sPlace + place.get(i)+"\t"+(int)(maxPercent.get(i)*90.0/(100-otherPercent))+"\n";
			}
			sPlace = sPlace + "Other\t"+10+"\n";
		}
		
		return sPlace;
	}
	
}