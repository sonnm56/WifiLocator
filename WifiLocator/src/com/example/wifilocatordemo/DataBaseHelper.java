package com.example.wifilocatordemo;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

@SuppressLint("SdCardPath")
public class DataBaseHelper extends SQLiteOpenHelper {

	public static final String DB_PATH_SOURCE = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/MyFiles/";
	public static final String DB_PATH_DEST = "/data/data/com.example.wifilocatordemo/databases/";

	private transient final String dataBaseName;

	/**
	 * Constructor Takes and keeps a reference of the passed context in order to
	 * access to the application assets and resources.
	 * 
	 * @param context
	 */
	public DataBaseHelper(final Context context, final String dbName) {
		super(context, dbName, null, 1);
		dataBaseName = dbName;
	}

	/**
	 * Copies your database from your local folder to the just created empty
	 * database in the system folder, from where it can be accessed and handled.
	 * This is done by transfering bytestream.
	 * */
	public void importDataBase() throws IOException {

		// Open your local db as the input stream
		final InputStream myInput = new FileInputStream(DB_PATH_SOURCE
				+ dataBaseName);

		// Open the empty db as the output stream
		final OutputStream myOutput = new FileOutputStream(DB_PATH_DEST
				+ dataBaseName);

		// transfer bytes from the inputfile to the outputfile
		final byte[] buffer = new byte[1024];
		int length;
		for (length = myInput.read(buffer); length > 0;) {
			myOutput.write(buffer, 0, length);
			length = myInput.read(buffer);
		}

		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();

	}

	public void exportDataBase() throws IOException {

		// Open your local db as the input stream
		final InputStream myInput = new FileInputStream(DB_PATH_DEST
				+ dataBaseName);

		// Open the empty db as the output stream
		final OutputStream myOutput = new FileOutputStream(DB_PATH_SOURCE
				+ dataBaseName);

		// transfer bytes from the inputfile to the outputfile
		final byte[] buffer = new byte[1024];
		int length;
		for (length = myInput.read(buffer); length > 0;) {
			myOutput.write(buffer, 0, length);
			length = myInput.read(buffer);
		}

		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();

	}

	@Override
	public void onCreate(final SQLiteDatabase database) {
		// do nothing
	}

	@Override
	public void onUpgrade(final SQLiteDatabase database, final int oldVersion,
			final int newVersion) {
		// do nothings
	}

}