package wifi.mobv.fei.stuba.sk.wifiscanner.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import wifi.mobv.fei.stuba.sk.wifiscanner.model.db.dao.LocationDAO.LocationEntry;
import wifi.mobv.fei.stuba.sk.wifiscanner.model.db.dao.WifiDAO.WifiEntry;
import wifi.mobv.fei.stuba.sk.wifiscanner.model.db.dao.HistoryDAO.HistoryEntry;

/**
 * Created by Feri on 25.11.2016.
 * https://sqlite.org/lang_conflict.html
 */

public class DBHelper extends SQLiteOpenHelper
{
	public static final String TAG = DBHelper.class.getSimpleName();
	// If you change the database schema, you must increment the database version.
	public static final String DATABASE_NAME = "wifi.db";
	public static final int DATABASE_VERSION = 3;

	public static final String SQL_CREATE_LOCATION_ENTRY =
			"CREATE TABLE IF NOT EXISTS " + LocationEntry.TABLE_NAME + " (" +
			LocationEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			LocationEntry.COLUMN_NAME_BLOCK_NAME + " TEXT, " +
			LocationEntry.COLUMN_NAME_FLOOR + " TEXT, " +
			"UNIQUE(" + LocationEntry.COLUMN_NAME_BLOCK_NAME + ", " + LocationEntry.COLUMN_NAME_FLOOR + ") ON CONFLICT IGNORE);";

	public static final String SQL_CREATE_WIFI_ENTRY =
			"CREATE TABLE IF NOT EXISTS " + WifiEntry.TABLE_NAME + " (" +
			WifiEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			WifiEntry.COLUMN_NAME_ID_LOCATION + " INTEGER NOT NULL, " +
			WifiEntry.COLUMN_NAME_BSSID + " TEXT NOT NULL, " +
			WifiEntry.COLUMN_NAME_SSID + " TEXT NOT NULL, " +
			WifiEntry.COLUMN_NAME_MAX_LEVEL + " INTEGER NOT NULL, " +
			"FOREIGN KEY(" + WifiEntry.COLUMN_NAME_ID_LOCATION + ") REFERENCES " + LocationEntry.TABLE_NAME + "(" + LocationEntry._ID + "), " +
			"UNIQUE(" + WifiEntry.COLUMN_NAME_BSSID + ") ON CONFLICT IGNORE);";

	public static final String SQL_CREATE_HISTORY_ENTRY =
			"CREATE TABLE IF NOT EXISTS " + HistoryEntry.TABLE_NAME + " (" +
			HistoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			HistoryEntry.COLUMN_NAME_ID_LOCATION + " INTEGER NOT NULL, " +
			HistoryEntry.COLUMN_NAME_DATE + " TEXT NOT NULL, " +
			"FOREIGN KEY(" + HistoryEntry.COLUMN_NAME_ID_LOCATION + ") REFERENCES " + LocationEntry.TABLE_NAME + "(" + LocationEntry._ID + "));";

	private static DBHelper instance;

	private DBHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public static DBHelper getInstance(Context context)
	{
		if( instance == null )
		{
			Log.i(TAG, "Creating instance of DBHelper");
			instance = new DBHelper(context);
		}

		return instance;
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		Log.i(TAG, "Creating tables for " + DATABASE_NAME);
		db.execSQL(SQL_CREATE_LOCATION_ENTRY);
		db.execSQL(SQL_CREATE_WIFI_ENTRY);
		db.execSQL(SQL_CREATE_HISTORY_ENTRY);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		if( oldVersion != newVersion )
		{
			Log.i(TAG, "Updating tables for " + DATABASE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + HistoryEntry.TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + WifiEntry.TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + LocationEntry.TABLE_NAME);
			onCreate(db);
		}
	}

	public void printCreateStatements()
	{
		System.out.println(SQL_CREATE_LOCATION_ENTRY);
		System.out.println(SQL_CREATE_WIFI_ENTRY);
		System.out.println(SQL_CREATE_HISTORY_ENTRY);
	}

	public void closeDB()
	{
		SQLiteDatabase db = this.getReadableDatabase();

		if( db != null && db.isOpen() )
		{
			Log.i(TAG, "Closing database " + DATABASE_NAME);
			db.close();
		}
	}

}