/*
 * The database used by this app
 * Include some functions to easier to use
 * */
package com.example.wifilocatordemo;

import java.io.IOException;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class GlobalDatabase {

	private static final String KEY_PLACE = "place";
	private static final String KEY_WIFI = "WiFiInfo";
	private static final String TAG = "WifiDatafase";
	private static String databaseName;
	private static final String DATABASE_TABLE = "places";
	private static final String DATABASE_CREATE = "create table places ("
			+ "place text not null, WiFiInfo text not null);";

	private final Context context;
	private final GlobalDatabaseHelper dbHelper;
	private SQLiteDatabase sqDatabase;

	// ---Constructor---
	public GlobalDatabase(final Context ctx, final String dbName) {
		this.context = ctx;
		databaseName = dbName.toLowerCase();
		dbHelper = new GlobalDatabaseHelper(context);
	}

	// ---Main class---
	/*
	 * This class extend DataBaseHelper can be use to create , load and save
	 * data base
	 */
	static class GlobalDatabaseHelper extends DatabaseHelper {

		GlobalDatabaseHelper(final Context context) {
			super(context, databaseName);
		}

		@Override
		public void onCreate(final SQLiteDatabase database) {
			database.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(final SQLiteDatabase database,
				final int oldVersion, final int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			database.execSQL("DROP TABLE IF EXISTS places");
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
	public GlobalDatabase open() throws SQLException {
		sqDatabase = dbHelper.getWritableDatabase();
		return this;
	}

	// ---closes the database---
	public void close() {
		dbHelper.close();
	}

	// ---import database from file---
	public void importDatabase() throws IOException {
		dbHelper.importDatabase();
	}

	// ---export database to file---
	public void exportDatabase() throws IOException {
		dbHelper.exportDatabase();
	}

	// ---insert a place into the database---
	public long insertPlace(final String place, final String WiFiInfo) {

		final ContentValues initialValues = new ContentValues();

		initialValues.put(KEY_PLACE, place);
		initialValues.put(KEY_WIFI, WiFiInfo);

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
				KEY_WIFI }, null, null, null, null, null);
	}

	// ---retrieves a place information---
	public Cursor getPlaceInfo(final String WiFiInfo) throws SQLException {
		final Cursor mCursor = sqDatabase.query(DATABASE_TABLE, new String[] {
				KEY_PLACE, KEY_WIFI }, KEY_WIFI + "=?",
				new String[] { WiFiInfo }, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	// ---retrieves a place---
	public String getPlace(final String WiFiInfo) throws SQLException {
		final Cursor mCursor = getPlaceInfo(WiFiInfo);
		String place;
		if (mCursor.moveToFirst()) {
			place = mCursor.getString(0);
		} else {
			place = "Not Found";
		}
		return place;
	}

	// ---updates a place---
	public long updatePlace(final String place, final String WiFiInfo) {
		final ContentValues args = new ContentValues();
		args.put(KEY_PLACE, place);
		args.put(KEY_WIFI, WiFiInfo);

		return sqDatabase.update(DATABASE_TABLE, args, KEY_WIFI + "=?",
				new String[] { WiFiInfo });
	}
}