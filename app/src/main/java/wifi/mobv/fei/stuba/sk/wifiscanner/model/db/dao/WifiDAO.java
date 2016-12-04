package wifi.mobv.fei.stuba.sk.wifiscanner.model.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.ScanResult;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

import wifi.mobv.fei.stuba.sk.wifiscanner.controller.SQLController;
import wifi.mobv.fei.stuba.sk.wifiscanner.model.db.DBHelper;
import wifi.mobv.fei.stuba.sk.wifiscanner.model.db.Location;
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
	private SQLController controller;
	private DBHelper dbHelper;

	public WifiDAO(SQLController controller, Context context)
	{
		this.controller = controller;
		dbHelper = DBHelper.getInstance(context);
	}

	public long create(Wifi wifi)
	{
		// Gets the data repository in write mode
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		// Create a new map of values, where column names are the keys
		ContentValues values = new ContentValues();
		values.put(WifiEntry.COLUMN_NAME_ID_LOCATION, wifi.getLocationID());
		values.put(WifiEntry.COLUMN_NAME_BSSID, wifi.getBSSID());
		values.put(WifiEntry.COLUMN_NAME_SSID, wifi.getSSID());
		values.put(WifiEntry.COLUMN_NAME_MAX_LEVEL, wifi.getMaxLevel());

		// Insert the new row, returning the primary key value of the new row
		return db.insert(WifiEntry.TABLE_NAME, null, values);
	}

	public void create(List<Wifi> wifi)
	{
		// loop through list
		for( int i = 0; i < wifi.size(); ++i )
		{
			// insert each entry into database
			if( create(wifi.get(i)) == -1 )
			{
				// if entry exists try to update
				SQLiteDatabase db = dbHelper.getReadableDatabase();

				String level = WifiEntry.COLUMN_NAME_MAX_LEVEL;

				String[] columns = {WifiEntry._ID, level};
				String selection = WifiEntry.COLUMN_NAME_BSSID + " = ?";
				String[] selectionArgs = {String.valueOf(wifi.get(i).getBSSID())};

				Cursor c = db.query(WifiEntry.TABLE_NAME, columns, selection, selectionArgs, null, null, null);

				if( c != null )
				{
					if( c.moveToFirst() )
					{
						if( wifi.get(i).getMaxLevel() < c.getInt(c.getColumnIndex(level)) )
						{
							db = dbHelper.getWritableDatabase();

							// New value for one column
							ContentValues values = new ContentValues();
							values.put(WifiEntry.COLUMN_NAME_ID_LOCATION, wifi.get(i).getLocationID());
							values.put(WifiEntry.COLUMN_NAME_SSID, wifi.get(i).getSSID());
							values.put(WifiEntry.COLUMN_NAME_MAX_LEVEL, wifi.get(i).getMaxLevel());

							// Which row to update, based on the title
							String whereClause = WifiEntry._ID + " = ?";
							String[] whereArgs = {String.valueOf(c.getLong(c.getColumnIndex(WifiEntry._ID)))};

							db.update(WifiEntry.TABLE_NAME, values, whereClause, whereArgs);
						}
					}
				}
			}
		}
	}

	public Wifi read(long wifiID)
	{
		SQLiteDatabase db = dbHelper.getReadableDatabase();

		// Define a projection that specifies which columns from the database
		// you will actually use after this query.
		String[] projection = {WifiEntry._ID, WifiEntry.COLUMN_NAME_ID_LOCATION, WifiEntry.COLUMN_NAME_BSSID, WifiEntry.COLUMN_NAME_SSID, WifiEntry.COLUMN_NAME_MAX_LEVEL,};

		// Filter results WHERE "title" = 'My Title'
		String selection = WifiEntry._ID + " = ?";
		String[] selectionArgs = {String.valueOf(wifiID)};

		// How you want the results sorted in the resulting Cursor
		// String sortOrder = WifiEntry.COLUMN_NAME_SUBTITLE + " DESC";

		Cursor cursor = db.query(WifiEntry.TABLE_NAME,        // The table to query
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

	public List<Wifi> readAll()
	{
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		List<Wifi> list = new ArrayList<Wifi>();
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

					list.add(wc);
				} while( c.moveToNext() );
			}
		}

		if( c != null && !c.isClosed() )
		{
			c.close();
		}

		return list;
	}

	public Cursor readWifiForLocation(Location location){

		SQLiteDatabase db = dbHelper.getReadableDatabase();
		List<Wifi> list = new ArrayList<Wifi>();
		String selectQuery = "SELECT * FROM " + WifiEntry.TABLE_NAME +
				" WHERE "+ WifiEntry.COLUMN_NAME_ID_LOCATION+ "=" + location.getId() + ";";

		Cursor c = db.rawQuery(selectQuery, null);

		return c;
	}

	public int update(Wifi wifi)
	{
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		// New value for one column
		ContentValues values = new ContentValues();
		values.put(WifiEntry.COLUMN_NAME_ID_LOCATION, wifi.getLocationID());

		// Which row to update, based on the title
		String whereClause = WifiEntry._ID + " = ?";
		String[] whereArgs = {String.valueOf(wifi.getId())};

		return db.update(WifiEntry.TABLE_NAME, values, whereClause, whereArgs);
	}

	public int delete(long wifiID)
	{
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		// Define 'where' part of query.
		String selection = WifiEntry._ID + " = ?";
		// Specify arguments in placeholder order.
		String[] selectionArgs = {String.valueOf(wifiID)};
		// Issue SQL statement.
		return db.delete(WifiEntry.TABLE_NAME, selection, selectionArgs);
	}

	public int deleteAll()
	{
		SQLiteDatabase db = dbHelper.getReadableDatabase();

		return db.delete(WifiEntry.TABLE_NAME, "1", null);
	}

	public void printAll()
	{
		List<Wifi> list = readAll();
		for( int i = 0; i < list.size(); ++i )
		{
			System.out.println(list.get(i));
		}
	}

	public Location locateMe(List<ScanResult> wifiList)
	{
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		List<Wifi> wifi = new ArrayList<Wifi>();

		String[] tableColumns = new String[]{"Count(" + WifiEntry.COLUMN_NAME_ID_LOCATION + ")"};

		String whereClause = WifiEntry.COLUMN_NAME_BSSID + " = ?";
		String[] whereArgs = new String[]{wifiList.get(0).BSSID};

		for( int i = 1; i < wifiList.size(); i++ )
		{
			whereClause = whereClause + " OR " + WifiEntry.COLUMN_NAME_BSSID + " = ?";
			whereArgs = new String[]{String.valueOf(whereArgs), wifiList.get(i).BSSID};
		}

		String orderBy = "Count(" + WifiEntry.COLUMN_NAME_ID_LOCATION + ") DESC";
		String groupBy = WifiEntry.COLUMN_NAME_ID_LOCATION;

		Cursor c = db.query(WifiEntry.TABLE_NAME, tableColumns, whereClause, whereArgs, groupBy, null, orderBy);

		if( c != null )
		{
			c.moveToFirst();
			//určenie polohy na základe ID_LOCATION
			return controller.getLocationDAO().read(c.getLong(c.getColumnIndex(WifiEntry.COLUMN_NAME_ID_LOCATION)));
		}

		return null;
	}
}