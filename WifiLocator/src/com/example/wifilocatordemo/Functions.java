package com.example.wifilocatordemo;

import java.util.List;

import android.net.wifi.ScanResult;

public class Functions {

	private final static int BSSID_NUMBER = 4;

	private final static int LEVELS_NUMBER = 4;

	// ---------------Rank Wifi List Functions---------
	public static void rankWifiListBSSID(List<ScanResult> wifiList) {

		final int number = (wifiList.size() < LEVELS_NUMBER) ? wifiList.size()
				: LEVELS_NUMBER;
		for (int i = 0; i < number; i++) {
			for (int j = i + 1; j < wifiList.size(); j++) {
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

	public static void rankWifiListLevel(List<ScanResult> wifiList) {

		final int number = (wifiList.size() < LEVELS_NUMBER) ? wifiList.size()
				: LEVELS_NUMBER;
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

	/*
	 * convert to move information from wifiList to database.There are 2
	 * functions : -makeWifiBSSID -makeWifiLevel
	 */
	public static String makeWifiBSSID(final List<ScanResult> wifiList) {
		StringBuilder wifiBSSID;
		int number;

		wifiBSSID = new StringBuilder();
		number = (wifiList.size() < BSSID_NUMBER) ? wifiList.size()
				: BSSID_NUMBER;

		for (int i = 0; i < number; i++) {
			wifiBSSID.append(wifiList.get(i).BSSID.toString());
		}

		return wifiBSSID.toString();
	}

	public static int makeWifiLevel(final List<ScanResult> wifiList) {
		int wifiLevel = 0;
		int number;

		number = (wifiList.size() < BSSID_NUMBER) ? wifiList.size()
				: BSSID_NUMBER;

		for (int i = number - 1; i >= 0; i--) {
			wifiLevel = wifiLevel * 100 - wifiList.get(i).level;
		}

		return wifiLevel;
	}

	/*
	 * Convert information from database and wifiList to list to use later
	 */
	public static int[] makeListWifiLevel(final int wifiLevel) {
		int number = wifiLevel;
		int increase = 0;
		int[] wifiLevelList = new int[4];
		while (number != 0) {
			wifiLevelList[increase] = -(number % 100);
			number = (int) (number / 100);
			increase++;
		}
		return wifiLevelList;
	}

	public static int[] makeListWifiLevel(final List<ScanResult> wifiList) {
		final int numberLevel = wifiList.size();
		int[] wifiLevelList = new int[numberLevel];
		for (int i = 0; i < numberLevel; i++) {
			wifiLevelList[i] = wifiList.get(i).level;
		}
		return wifiLevelList;
	}

	/*
	 * Convert to string the place and percent
	 * to show it later
	 */
	public static String getPlaceAndPercent(final List<String> place,
			final List<Integer> maxPercent) {
		int otherPercent = 100;
		final StringBuilder sPlace = new StringBuilder();
		for (int i = 0; i < place.size(); i++) {
			otherPercent = otherPercent - maxPercent.get(i);
		}
		if (otherPercent > 0) {
			for (int i = 0; i < place.size(); i++) {
				sPlace.append(place.get(i) + "\t\t\t" + maxPercent.get(i)
						+ "%\n");
			}
			sPlace.append("Other" + "\t\t\t" + otherPercent + "%\n");
		} else {
			for (int i = 0; i < place.size(); i++) {
				sPlace.append(place.get(i)
						+ "\t"
						+ (int) (maxPercent.get(i) * 90.0 / (100 - otherPercent))
						+ "%\n");
			}
			sPlace.append("Other" + "\t" + 10 + "%\n");
		}

		return sPlace.toString();
	}

}
