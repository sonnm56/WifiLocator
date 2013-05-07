/*
 * The database used by this application
 * Include some functions to easier to use
 * */
package com.example.wifilocatordemo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.ScanResult;
import android.util.Log;

public class LocalDatabase {

	private static final String KEY_PLACE = "place";
	private static final String KEY_WIFI_BSSID = "wifiBSSID";
	private static final String KEY_WIFI_LEVEL = "wifiLevel";

	private static final String TAG = "LocalDataBase";
	private static String dataBaseName;
	private static final String DATABASE_TABLE = "LocalPlaces";
	private static final String CREATE_TABLE = "create table " + DATABASE_TABLE
			+ " (" + KEY_PLACE + " text not null, " + KEY_WIFI_BSSID
			+ " text not null, " + KEY_WIFI_LEVEL + " integer not null);";

	private final Context context;
	private final localDatabaseHelper dbHelper;
	private SQLiteDatabase sqDatabase;

	// ---Constructor---
	public LocalDatabase(final Context ctx, final String dbName) {
		this.context = ctx;
		dataBaseName = dbName.toLowerCase();
		dbHelper = new localDatabaseHelper(context);
	}

	// ---Main class---
	/*
	 * This class extend DataBaseHelper can be use to create , load and save
	 * data base
	 */
	static class localDatabaseHelper extends DataBaseHelper {

		localDatabaseHelper(final Context context) {
			super(context, dataBaseName);
		}

		@Override
		public void onCreate(final SQLiteDatabase database) {
			database.execSQL(CREATE_TABLE);
		}

		@Override
		public void onUpgrade(final SQLiteDatabase database,
				final int oldVersion, final int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			database.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(database);
		}
	}

	/**************************FUNCTIONS****************************
	 * 
	 * These methods make database to easy to use We can use these to make
	 * table, edit info find info and ex-import information from file
	 * 
	 * */

	// ---opens the database---
	public LocalDatabase open() throws SQLException {
		sqDatabase = dbHelper.getWritableDatabase();
		return this;
	}

	// ---closes the database---
	public void close() {
		dbHelper.close();
	}

	// ---import database from file---
	public void importDataBase() throws IOException {
		dbHelper.importDataBase();
	}

	// ---export database to file---
	public void exportDataBase() throws IOException {
		dbHelper.exportDataBase();
	}

	// ---insert a place into the database---
	public long insertPlace(final String place, final List<ScanResult> wifiList) {

		final ContentValues initialValues = new ContentValues();

		initialValues.put(KEY_PLACE, place);
		initialValues.put(KEY_WIFI_BSSID, Functions.makeWifiBSSID(wifiList));
		initialValues.put(KEY_WIFI_LEVEL, Functions.makeWifiLevel(wifiList));

		return sqDatabase.insert(DATABASE_TABLE, null, initialValues);
	}

	// ---deletes a place---
	public boolean deletePlace(final String place) {
		return sqDatabase.delete(DATABASE_TABLE, KEY_PLACE + "=?",
				new String[] { place }) > 0;
	}

	// ---retrieves all the places---
	public Cursor getAllPlaces() {
		return sqDatabase.query(DATABASE_TABLE, new String[] { KEY_PLACE,
				KEY_WIFI_BSSID }, null, null, null, null, null);
	}

	// ---retrieves a place information---
	public Cursor getPlaceInfo(final String wifiBSSID) throws SQLException {
		final Cursor mCursor = sqDatabase.rawQuery("SELECT * FROM "
				+ DATABASE_TABLE + " WHERE " + KEY_WIFI_BSSID + " = '"
				+ wifiBSSID + "'", null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	// ---retrieves a place---
	public String getPlace(final String wifiBSSID, final int[] listLevel)
			throws SQLException {
		final Cursor mCursor = getPlaceInfo(wifiBSSID);
		String sPlace = "";
		final int maxNumberPlace = mCursor.getCount();
		final List<String> place = new ArrayList<String>();
		final List<Integer> maxPercent = new ArrayList<Integer>();
		
		/*
		 * Show the places that have wifiBSSID and calculate the percent
		 * of them
		 */
		if (maxNumberPlace != 0) {
			mCursor.moveToFirst();
			do {
				int check = 0;
				final int sizePlace = place.size();
				int percent = 10;
				final int[] wifiLevel = Functions.makeListWifiLevel(Integer
						.parseInt(mCursor.getString(2)));
				
				//calculate the percent
				for (int i = 0; i < wifiLevel.length; i++) {
					if (wifiLevel[i] - 5 < listLevel[i]
							&& listLevel[i] < wifiLevel[i] + 5) {
						percent = percent + 7 * i + 7;
					}
				}
				
				//if this place have percents, choose the max percent
				for (int i = 0; i < sizePlace; i++) {
					if (mCursor.getString(0).equals(place.get(i))) {
						if (maxPercent.get(i) < percent) {
							maxPercent.set(i, percent);
						}
						check = 1;
						break;
					}
				}
				//Add new place and this percent to list
				if (check == 0) {
					place.add(mCursor.getString(0));
					maxPercent.add(percent);
				}
			} while (mCursor.moveToNext());
			
			//To String the place and percent list
			sPlace = Functions.getPlaceAndPercent(place, maxPercent);
			mCursor.close();

		} else {
			mCursor.close();
			sPlace = "Not Found";
		}
		return sPlace;
	}

	// ---updates a place---
	public long updatePlace(final String place, final String wifiBSSID) {
		final ContentValues args = new ContentValues();
		args.put(KEY_PLACE, place);
		args.put(KEY_WIFI_BSSID, wifiBSSID);

		return sqDatabase.update(DATABASE_TABLE, args, KEY_WIFI_BSSID + "=?",
				new String[] { wifiBSSID });
	}
}