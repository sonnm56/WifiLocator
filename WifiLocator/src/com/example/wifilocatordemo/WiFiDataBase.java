package com.example.wifilocatordemo;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class WiFiDataBase {
    
	static final String KEY_PLACE = "place";
    static final String KEY_WIFI = "WiFiInfo";
    static final String TAG = "WiFiDataBase";
    static final String DATABASE_NAME = "WiFiDB";
    static final String DATABASE_TABLE = "places";
    static final int DATABASE_VERSION = 1;
    static final String DATABASE_CREATE =
        "create table places ("
        + "place text not null, WiFiInfo text not null);";
    
    final Context context;
    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    
    //---Constructor---  
    public WiFiDataBase(Context ctx){
        this.context = ctx;
        dbHelper = new DatabaseHelper(context);
    }
    
    //---Main class---
    private static class DatabaseHelper extends SQLiteOpenHelper{
        
    	DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        
    	@Override
        public void onCreate(SQLiteDatabase db){
            try {
                db.execSQL(DATABASE_CREATE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS places");
            onCreate(db);
        }
    }
    
    
/**************************FUNCTIONS*****************************/
    
    //---opens the database---  
    public WiFiDataBase open() throws SQLException{
        db = dbHelper.getWritableDatabase();
        return this;
    }
    
    //---closes the database---    
    public void close(){
        dbHelper.close();
    }
    
    //---insert a place into the database--- 
    public long insertPlace(String place, String WiFiInfo){ 
        ContentValues initialValues = new ContentValues();
        
        initialValues.put(KEY_PLACE, place);
        initialValues.put(KEY_WIFI, WiFiInfo);
        
        return db.insert(DATABASE_TABLE, null, initialValues);
    }
    
    //---deletes a place---    
    public boolean deletePlace(String place){
        return db.delete(DATABASE_TABLE, KEY_PLACE + "=?",new String[] {place}) > 0;
    }
    
    //---retrieves all the places---    
    public Cursor getAllPlaces(){
        return db.query(DATABASE_TABLE, new String[] {KEY_PLACE,
                KEY_WIFI}, null, null, null, null, null);
    }
    
    //---retrieves a place information---    
    public Cursor getPlaceInfo(String WiFiInfo) throws SQLException{
        Cursor mCursor =
                db.query(DATABASE_TABLE, new String[] {
                KEY_PLACE, KEY_WIFI}, KEY_WIFI + "=?",new String[] {WiFiInfo},
                null, null, null, null);
        if (mCursor != null) mCursor.moveToFirst();
        
        return mCursor;        
    }
    
    //---retrieves a place---  
    public String getPlace(String WiFiInfo) throws SQLException{
    	Cursor mCursor =getPlaceInfo(WiFiInfo);
		   
		if(mCursor.moveToFirst())
		    return (mCursor.getString(0));
		else 
			return "Not Found";
    }
    
    //---updates a place---    
    public boolean updatePlace(String place, String WiFiInfo){
        ContentValues args = new ContentValues();
        args.put(KEY_PLACE, place);
        args.put(KEY_WIFI, WiFiInfo);
        
        return db.update(DATABASE_TABLE, args, KEY_WIFI + "=?",
        		new String[] {WiFiInfo}) > 0;
    }
}