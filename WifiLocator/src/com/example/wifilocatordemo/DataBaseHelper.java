package com.example.wifilocatordemo;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

@SuppressLint("SdCardPath")
public class DataBaseHelper extends SQLiteOpenHelper{
 
	public static final String  DB_PATH_SOURCE = Environment.getExternalStorageDirectory()
														.getAbsolutePath() +  "/MyFiles/";
	public static final String  DB_PATH_DEST = "/data/data/com.example.wifilocatordemo/databases/";
    
    private String dataBaseName;
 
    private SQLiteDatabase myDataBase; 
 
   
    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public DataBaseHelper(Context context,String dbName) {
    	super(context, dbName, null, 1);
        dataBaseName = dbName;
    }	
 
     /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    public boolean checkDataBase(){
    	SQLiteDatabase checkDB = null;
    	try{
    		String myPath = DB_PATH_DEST + dataBaseName;
    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    	}catch(SQLiteException e){
    		//database does't exist yet.
    	}
    	if(checkDB != null)checkDB.close();
    	
    	return checkDB != null ? true : false;
    }
 
    /**
     * Copies your database from your local folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    public void importDataBase() throws IOException{
 
    	//Open your local db as the input stream
    	String inFileName = DB_PATH_SOURCE + dataBaseName;
    	InputStream myInput = new FileInputStream(inFileName);
        
    	// Path to the just created empty db
    	String outFileName = DB_PATH_DEST + dataBaseName;
 
    	//Open the empty db as the output stream
    	OutputStream myOutput = new FileOutputStream(outFileName);
 
    	//transfer bytes from the inputfile to the outputfile
    	byte[] buffer = new byte[1024];
    	int length;
    	while ((length = myInput.read(buffer))>0){
    		myOutput.write(buffer, 0, length);
    	}
 
    	//Close the streams
    	myOutput.flush();
    	myOutput.close();
    	myInput.close();
 
    }
    public void exportDataBase() throws IOException{
    	 
    	//Open your local db as the input stream
    	String inFileName = DB_PATH_DEST + dataBaseName;
    	 
    	InputStream myInput = new FileInputStream(inFileName);
        
    	// Path to the just created empty db
    	String outFileName = DB_PATH_SOURCE + dataBaseName;
 
    	//Open the empty db as the output stream
    	OutputStream myOutput = new FileOutputStream(outFileName);
 
    	//transfer bytes from the inputfile to the outputfile
    	byte[] buffer = new byte[1024];
    	int length;
    	while ((length = myInput.read(buffer))>0){
    		myOutput.write(buffer, 0, length);
    	}
 
    	//Close the streams
    	myOutput.flush();
    	myOutput.close();
    	myInput.close();
 
    }
    
    @Override
	public synchronized void close() {
    	if(myDataBase != null) myDataBase.close();
    	super.close();
	}
 
	@Override
	public void onCreate(SQLiteDatabase db) {}
 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
 
}