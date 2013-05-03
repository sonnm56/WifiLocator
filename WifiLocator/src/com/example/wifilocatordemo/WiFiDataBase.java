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

public class WiFiDataBase {
    
	static final String KEY_PLACE = "place";
    static final String KEY_WIFI = "WiFiInfo";
    static final String TAG = "WiFiDataBase";
    static String dataBaseName;
    static final String DATABASE_TABLE = "places";
    static final int DATABASE_VERSION = 1;
    static final String DATABASE_CREATE =
        "create table places ("
        + "place text not null, WiFiInfo text not null);";
    
    final Context context;
    DatabaseHelper dbHelper;
    SQLiteDatabase sqDatabase;
    
    //---Constructor---  
    public WiFiDataBase(Context ctx, String dbName){
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
        public void onCreate(SQLiteDatabase database){
    		if(!checkDataBase()){
    				database.execSQL(DATABASE_CREATE);	
    		}
        }
    	
        @Override
        public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion){
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            database.execSQL("DROP TABLE IF EXISTS places");
            onCreate(database);
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
    public WiFiDataBase open() throws SQLException{
      	sqDatabase = dbHelper.getWritableDatabase();	
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
    public long insertPlace(String place, String WiFiInfo){ 
    	
    	ContentValues initialValues = new ContentValues();
        
        initialValues.put(KEY_PLACE, place);
        initialValues.put(KEY_WIFI, WiFiInfo);
        
        return sqDatabase.insert(DATABASE_TABLE, null, initialValues);
    }
    
    //---deletes a place---    
    public boolean deletePlace(String place){
        return sqDatabase.delete(DATABASE_TABLE, KEY_PLACE + "=?",new String[] {place}) > 0;
    }
    
    //---retrieves all the places---    
    public Cursor getAllPlaces(){
        return sqDatabase.query(DATABASE_TABLE, new String[] {KEY_PLACE,
                KEY_WIFI}, null, null, null, null, null);
    }
    
    //---retrieves a place information---    
    public Cursor getPlaceInfo(String WiFiInfo) throws SQLException{
        Cursor mCursor =
                sqDatabase.query(DATABASE_TABLE, new String[] {
                KEY_PLACE, KEY_WIFI}, KEY_WIFI + "=?",new String[] {WiFiInfo},
                null, null, null, null);
        if (mCursor != null){
        	mCursor.moveToFirst();
        }
        
        return mCursor;        
    }
    
    //---retrieves a place---  
    public String getPlace(String WiFiInfo) throws SQLException{
    	Cursor mCursor =getPlaceInfo(WiFiInfo);
		String place;   
		if(mCursor.moveToFirst()){
			place= (mCursor.getString(0));
		}else{ 
			place = "Not Found";
		}
		return place;
    }
    
    //---updates a place---    
    public long updatePlace(String place, String WiFiInfo){
        ContentValues args = new ContentValues();
        args.put(KEY_PLACE, place);
        args.put(KEY_WIFI, WiFiInfo);
        
        return sqDatabase.update(DATABASE_TABLE, args, KEY_WIFI + "=?",
        		new String[] {WiFiInfo}) ;
    }
}