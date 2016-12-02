package wifi.mobv.fei.stuba.sk.wifiscanner.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import wifi.mobv.fei.stuba.sk.wifiscanner.model.db.dao.LocationDAO.LocationEntry;
import wifi.mobv.fei.stuba.sk.wifiscanner.model.db.dao.WifiDAO.WifiEntry;
import wifi.mobv.fei.stuba.sk.wifiscanner.model.db.dao.HistoryDAO.HistoryEntry;

/**
 * Created by Feri on 25.11.2016.
 */

public class DBHelper extends SQLiteOpenHelper {
    public static final String TAG = DBHelper.class.getSimpleName();
    private static DBHelper instance;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "wifi.db";

    public static final String SQL_CREATE_LOCATION_ENTRY =
            "CREATE TABLE IF NOT EXISTS " + LocationEntry.TABLE_NAME + " (" +
                    LocationEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    LocationEntry.COLUMN_NAME_BLOCK_NAME + " TEXT, " +
                    LocationEntry.COLUMN_NAME_FLOOR + " TEXT);";

    public static final String SQL_CREATE_WIFI_ENTRY =
            "CREATE TABLE IF NOT EXISTS " + WifiEntry.TABLE_NAME + " (" +
                    WifiEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    WifiEntry.COLUMN_NAME_ID_LOCATION + " INTEGER REFERENCES " + LocationEntry.TABLE_NAME + ", " +
                    WifiEntry.COLUMN_NAME_BSSID + " TEXT UNIQUE, " +
                    WifiEntry.COLUMN_NAME_SSID + " TEXT, " +
                    WifiEntry.COLUMN_NAME_MAX_LEVEL + " INTEGER);";

    public static final String SQL_CREATE_HISTORY_ENTRY =
            "CREATE TABLE IF NOT EXISTS " + HistoryEntry.TABLE_NAME + " (" +
                    HistoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    HistoryEntry.COLUMN_NAME_ID_LOCATION + " INTEGER REFERENCES " + LocationEntry.TABLE_NAME + ", " +
                    HistoryEntry.COLUMN_NAME_DATE + " TEXT);";

    private DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DBHelper getInstance(Context context)
    {
        if( instance == null )
        {
            instance = new DBHelper(context);
        }

        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(SQL_CREATE_LOCATION_ENTRY);
        db.execSQL(SQL_CREATE_WIFI_ENTRY);
        db.execSQL(SQL_CREATE_HISTORY_ENTRY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        if( oldVersion != newVersion )
        {
            db.execSQL("DROP TABLE IF EXISTS " + WifiEntry.TABLE_NAME);
            onCreate(db);
        }
    }

    public void closeDB()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        if( db != null && db.isOpen() )
        {
            db.close();
        }
    }

}