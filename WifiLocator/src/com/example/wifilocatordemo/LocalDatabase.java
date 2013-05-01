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
    
	static final String KEY_PLACE = "place";
    static final String KEY_WIFI_BSSID = "wifiBSSID";
    static final String KEY_WIFI_LEVEL = "wifiLevel";
    
    static final String TAG = "LocalDataBase";
    static String dataBaseName;
    static final String DATABASE_TABLE = "LocalPlaces";
    static final int DATABASE_VERSION = 1;
    static final String DATABASE_CREATE_TABLE =
        "create table "+DATABASE_TABLE+" ("
        + KEY_PLACE+" text not null, "+KEY_WIFI_BSSID+" text not null, "+KEY_WIFI_LEVEL+" integer not null);";
    
    final Context context;
    localDatabaseHelper dbHelper;
    SQLiteDatabase db;
    
    //---Constructor---  
    public LocalDatabase(Context ctx, String dbName){
        this.context = ctx;
        dataBaseName = dbName.toLowerCase();
        dbHelper = new localDatabaseHelper(context);
    }
    
    //---Main class---
    /*
     * This class extend DataBaseHelper can be use to
     * create , load and save data base
     */
    static class localDatabaseHelper extends DataBaseHelper{
        
    	localDatabaseHelper(Context context){
            super(context,dataBaseName);    
        }
        
    	@Override
        public void onCreate(SQLiteDatabase db){
    		if(!checkDataBase())
    				db.execSQL(DATABASE_CREATE_TABLE);	
        }
    	
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE);
            onCreate(db);
        }
    }
    
    
/**************************FUNCTIONS****************************
 * 
 * These methods make database to easy to use
 * We can use these to make table, edit info
 * find info and ex-import information from file
 * 
 * */
    
    //---opens the database---  
    public LocalDatabase open() throws SQLException{
      	db = dbHelper.getWritableDatabase();	
	    return this;
    }
    
    //---closes the database---    
    public void close(){
        dbHelper.close();
    }
    
    //---import database from file---
    public void importDataBase() throws IOException{
    	dbHelper.importDataBase();
    }
    	 
    //---export database to file---
    public void exportDataBase() throws IOException{
    	dbHelper.exportDataBase();
    }
    
    //---insert a place into the database--- 
    public long insertPlace(String place, List<ScanResult> wifiList){ 
    	
    	ContentValues initialValues = new ContentValues();
        
        initialValues.put(KEY_PLACE, place);
        initialValues.put(KEY_WIFI_BSSID, Functions.makeWifiBSSID(wifiList));
        initialValues.put(KEY_WIFI_LEVEL, Functions.makeWifiLevel(wifiList));
        
        return db.insert(DATABASE_TABLE, null, initialValues);
    }
    
    //---deletes a place---    
    public boolean deletePlace(String place){
        return db.delete(DATABASE_TABLE, KEY_PLACE + "=?",new String[] {place}) > 0;
    }
    
    //---retrieves all the places---    
    public Cursor getAllPlaces(){
        return db.query(DATABASE_TABLE, new String[] {KEY_PLACE,
                KEY_WIFI_BSSID}, null, null, null, null, null);
    }
    
    //---retrieves a place information---    
    public Cursor getPlaceInfo(String wifiBSSID) throws SQLException{
        Cursor mCursor =
                db.rawQuery("SELECT * FROM "+DATABASE_TABLE+" WHERE " + KEY_WIFI_BSSID + " = '" + wifiBSSID + "'", null);
        if (mCursor != null) mCursor.moveToFirst();
        
        return mCursor;        
    }
    
    //---retrieves a place---  
	public String getPlace(String wifiBSSID, int[] listLevel) throws SQLException{
    	Cursor mCursor =getPlaceInfo(wifiBSSID);
		String sPlace="";
		int maxNumberPlace = mCursor.getCount();
		List<String> place = new ArrayList<String>();
		List<Integer> maxPercent = new ArrayList<Integer>();
		
    	if(maxNumberPlace!=0){
			mCursor.moveToFirst();
			do{	
				int check = 0, t = place.size();
				int percent = 10;
				int[] wifiLevel = Functions.makeListWifiLevel(Integer.parseInt(mCursor.getString(2)));
				for(int i= 0;i<wifiLevel.length;i++){
					if(wifiLevel[i]-5 < listLevel[i] && listLevel[i] < wifiLevel[i]+5 )
						percent = percent + 7*i+7; 
				}
				for(int i =0;i<t;i++){
					if(mCursor.getString(0).equals(place.get(i))){
						if(maxPercent.get(i)< percent) maxPercent.set(i, percent);
						check = 1;
						break;
					}
				}
				if(check == 0){
					place.add (mCursor.getString(0));
					maxPercent.add(percent); 
				}	
			}while(mCursor.moveToNext());
			sPlace = Functions.getPlaceAndPercent(place,maxPercent);
			mCursor.close();
			return sPlace;
		}
		else{
			mCursor.close();
			return "Not Found";
		}
			
    }
    
    //---updates a place---    
    public long updatePlace(String place, String wifiBSSID){
        ContentValues args = new ContentValues();
        args.put(KEY_PLACE, place);
        args.put(KEY_WIFI_BSSID, wifiBSSID);
        
        return db.update(DATABASE_TABLE, args, KEY_WIFI_BSSID + "=?",
        		new String[] {wifiBSSID}) ;
    }
}