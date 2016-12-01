package wifi.mobv.fei.stuba.sk.wifiscanner.model.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

import wifi.mobv.fei.stuba.sk.wifiscanner.model.db.DBHelper;
import wifi.mobv.fei.stuba.sk.wifiscanner.model.db.Wifi;

/**
 * Created by maros on 1.12.2016.
 */

public class WifiDAO
{
	/* Inner class that defines the table contents */
	public static class WifiEntry implements BaseColumns
	{
		public static final String TABLE_NAME = "wifi";
		public static final String COLUMN_NAME_ID_LOCATION = "id_location";
		public static final String COLUMN_NAME_BSSID = "bssid";
		public static final String COLUMN_NAME_SSID = "ssid";
		public static final String COLUMN_NAME_MAX_LEVEL = "max_level";
	}
	
	public static final String TAG = WifiDAO.class.getSimpleName();
	private DBHelper dbHelper;

	public WifiDAO(Context context)
	{
		dbHelper = DBHelper.getInstance(context);
	}

	public long createWifi(Wifi wifi, long locationID)
	{
		// Gets the data repository in write mode
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		// Create a new map of values, where column names are the keys
		ContentValues values = new ContentValues();
		values.put(WifiEntry.COLUMN_NAME_ID_LOCATION, locationID);
		values.put(WifiEntry.COLUMN_NAME_BSSID, wifi.getBSSID());
		values.put(WifiEntry.COLUMN_NAME_SSID, wifi.getSSID());
		values.put(WifiEntry.COLUMN_NAME_MAX_LEVEL, wifi.getMaxLevel());

		// Insert the new row, returning the primary key value of the new row
		return db.insert(WifiEntry.TABLE_NAME, null, values);
	}

	public Wifi readWifi(long wifiID)
	{
		SQLiteDatabase db = dbHelper.getReadableDatabase();

		// Define a projection that specifies which columns from the database
		// you will actually use after this query.
		String[] projection = {
				WifiEntry._ID,
				WifiEntry.COLUMN_NAME_ID_LOCATION,
				WifiEntry.COLUMN_NAME_BSSID,
				WifiEntry.COLUMN_NAME_SSID,
				WifiEntry.COLUMN_NAME_MAX_LEVEL,
		};

		// Filter results WHERE "title" = 'My Title'
		String selection = WifiEntry._ID + " = ?";
		String[] selectionArgs = { String.valueOf(wifiID) };

		// How you want the results sorted in the resulting Cursor
		// String sortOrder = WifiEntry.COLUMN_NAME_SUBTITLE + " DESC";

		Cursor cursor = db.query(
				WifiEntry.TABLE_NAME,        // The table to query
				projection,                               // The columns to return
				selection,                                // The columns for the WHERE clause
				selectionArgs,                            // The values for the WHERE clause
				null,                                     // don't group the rows
				null,                                     // don't filter by row groups
				null                                 // The sort order
		);

		cursor.moveToFirst();
		long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(WifiEntry._ID));

		return new Wifi();
	}

	public List<Wifi> readAllWifis()
	{
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		List<Wifi> wifi = new ArrayList<Wifi>();
		String selectQuery = "SELECT * FROM " + WifiEntry.TABLE_NAME + ";";

		Cursor c = db.rawQuery(selectQuery, null);

		if( c != null )
		{
			if( c.moveToFirst() )
			{
				do
				{
					Wifi wc = new Wifi();
					wc.setId(c.getLong(c.getColumnIndex(WifiEntry._ID)));
					wc.setLocationID(c.getLong(c.getColumnIndex(WifiEntry.COLUMN_NAME_ID_LOCATION)));
					wc.setBSSID(c.getString(c.getColumnIndex(WifiEntry.COLUMN_NAME_BSSID)));
					wc.setSSID(c.getString(c.getColumnIndex(WifiEntry.COLUMN_NAME_SSID)));
					wc.setMaxLevel(c.getInt(c.getColumnIndex(WifiEntry.COLUMN_NAME_MAX_LEVEL)));

					wifi.add(wc);
				} while( c.moveToNext() );
			}
		}

		if( c != null && !c.isClosed() )
		{
			c.close();
		}

		return wifi;
	}

//	public void updateWifi()
//	{
//		SQLiteDatabase db = mDbHelper.getReadableDatabase();
//
//		// New value for one column
//		ContentValues values = new ContentValues();
//		values.put(FeedEntry.COLUMN_NAME_TITLE, title);
//
//		// Which row to update, based on the title
//		String selection = FeedEntry.COLUMN_NAME_TITLE + " LIKE ?";
//		String[] selectionArgs = { "MyTitle" };
//
//		int count = db.update(
//				FeedReaderDbHelper.FeedEntry.TABLE_NAME,
//				values,
//				selection,
//				selectionArgs);
//	}

	public void deleteWifi(long wifiID)
	{
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		// Define 'where' part of query.
		String selection = WifiEntry._ID + " LIKE ?";
		// Specify arguments in placeholder order.
		String[] selectionArgs = { String.valueOf(wifiID) };
		// Issue SQL statement.
		db.delete(WifiEntry.TABLE_NAME, selection, selectionArgs);
	}
}
