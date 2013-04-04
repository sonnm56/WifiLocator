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
    DatabaseHelper DBHelper;
    SQLiteDatabase db;
    
    public WiFiDataBase(Context ctx)
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }
    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db)

        {
            try {
                db.execSQL(DATABASE_CREATE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS places");
            onCreate(db);
        }
    }
    //---opens the database---    
    public WiFiDataBase open() throws SQLException 
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }
    //---closes the database---    
    public void close() 
    {
        DBHelper.close();
    }
    //---insert a place into the database---    
    public long insertPlace(String place, String WiFiInfo) 
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_PLACE, place);
        initialValues.put(KEY_WIFI, WiFiInfo);
        return db.insert(DATABASE_TABLE, null, initialValues);
    }
    //---deletes a particular place---    
    public boolean deletePlace(String place) 
    {
        return db.delete(DATABASE_TABLE, KEY_PLACE + "=?",new String[] {place}) > 0;
    }
    //---retrieves all the places---    
    public Cursor getAllPlaces()
    {
        return db.query(DATABASE_TABLE, new String[] {KEY_PLACE,
                KEY_WIFI}, null, null, null, null, null);
    }
    //---retrieves a particular place---    
    public Cursor getPlace(String WiFiInfo) throws SQLException 
    {

        Cursor mCursor =
                db.query(DATABASE_TABLE, new String[] {
                KEY_PLACE, KEY_WIFI}, KEY_WIFI + "=?",new String[] {WiFiInfo},
                null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
            return mCursor;
        
    }
    
    //---updates a place---    
    public boolean updatePlace(String place, String WiFiInfo) 
    {
        ContentValues args = new ContentValues();
        args.put(KEY_PLACE, place);
        args.put(KEY_WIFI, WiFiInfo);
        return db.update(DATABASE_TABLE, args, KEY_PLACE + "=?",new String[] {place}) > 0;
    }
}