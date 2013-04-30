/*
 * The database used by this app
 * Include some functions to easier to use
 * */
package com.example.wifilocatordemo;

import java.io.IOException;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

@SuppressLint("DefaultLocale")
public class LocalDatabase {
    
	static final String KEY_PLACE = "place";
    static final String[] wifi = {"Wifi_1", "Wifi_2", "Wifi_3", "Wifi_4"};
    static final String TAG = "WiFiDataBase";
    static String dataBaseName;
    static final String DATABASE_TABLE = "places";
    static final int DATABASE_VERSION = 1;
    static final String DATABASE_CREATE =
        "create table places ("
        + "place text not null, WiFiInfo text not null);";
    
    final Context context;
    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    
    //---Constructor---  
    public LocalDatabase(Context ctx, String dbName){
        this.context = ctx;
        dataBaseName = dbName.toLowerCase();
        dbHelper = new DatabaseHelper(context);
    }
    
    //---Main class---
    /*
     * This class extend DataBaseHelper can be use to
     * create , load and save data base
     */
    static class DatabaseHelper extends DataBaseHelper{
        
    	DatabaseHelper(Context context){
            super(context,dataBaseName);    
        }
        
    	@Override
        public void onCreate(SQLiteDatabase db){
    		if(!checkDataBase())
    				db.execSQL(DATABASE_CREATE);	
        }
    	
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS places");
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
    public long insertPlace(String place, String WiFiInfo, int which_wifi){ 
    	if(!getPlace(WiFiInfo, which_wifi).equals("Not Found"))
    		return updatePlace(place, WiFiInfo, which_wifi);
    	
        ContentValues initialValues = new ContentValues();
        
        initialValues.put(KEY_PLACE, place);
        initialValues.put(wifi[which_wifi -1], WiFiInfo);
        
        return db.insert(DATABASE_TABLE, null, initialValues);
    }
    
    //---deletes a place---    
    public boolean deletePlace(String place){
        return db.delete(DATABASE_TABLE, KEY_PLACE + "=?",new String[] {place}) > 0;
    }
    
    //---retrieves all the places---    
    public Cursor getAllPlaces(int which_wifi){
        return db.query(DATABASE_TABLE, new String[] {KEY_PLACE,
                wifi[which_wifi - 1]}, null, null, null, null, null);
    }
    
    //---retrieves a place information---    
    public Cursor getPlaceInfo(String WiFiInfo, int which_wifi) throws SQLException{
        Cursor mCursor =
                db.query(DATABASE_TABLE, new String[] {
                KEY_PLACE, wifi[which_wifi - 1]}, wifi[which_wifi - 1] + "=?",new String[] {WiFiInfo},
                null, null, null, null);
        if (mCursor != null) mCursor.moveToFirst();
        
        return mCursor;        
    }
    
    //---retrieves a place---  
    public String getPlace(String WiFiInfo, int which_wifi) throws SQLException{
    	Cursor mCursor =getPlaceInfo(WiFiInfo, which_wifi);
		   
		if(mCursor.moveToFirst())
		    return (mCursor.getString(0));
		else 
			return "Not Found";
    }
    
    //---updates a place---    
    public long updatePlace(String place, String WiFiInfo, int which_wifi){
        ContentValues args = new ContentValues();
        args.put(KEY_PLACE, place);
        args.put(wifi[which_wifi - 1], WiFiInfo);
        
        return db.update(DATABASE_TABLE, args, wifi[which_wifi - 1] + "=?",
        		new String[] {WiFiInfo}) ;
    }
}