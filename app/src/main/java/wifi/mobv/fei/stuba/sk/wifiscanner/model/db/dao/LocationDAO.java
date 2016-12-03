package wifi.mobv.fei.stuba.sk.wifiscanner.model.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import wifi.mobv.fei.stuba.sk.wifiscanner.controller.SQLController;
import wifi.mobv.fei.stuba.sk.wifiscanner.model.db.DBHelper;
import wifi.mobv.fei.stuba.sk.wifiscanner.model.db.History;
import wifi.mobv.fei.stuba.sk.wifiscanner.model.db.Location;

/**
 * Created by maros on 1.12.2016.
 */

public class LocationDAO
{
	/* Inner class that defines the table contents */
	public static class LocationEntry implements BaseColumns
	{
		public static final String TABLE_NAME = "location";
		public static final String COLUMN_NAME_BLOCK_NAME = "block_name";
		public static final String COLUMN_NAME_FLOOR = "floor";
	}
	
	public static final String TAG = LocationDAO.class.getSimpleName();
	private SQLController controller;
	private DBHelper dbHelper;

	public LocationDAO(SQLController controller, Context context)
	{
		this.controller = controller;
		dbHelper = DBHelper.getInstance(context);
	}

	public long create(Location location)
	{
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(LocationEntry.COLUMN_NAME_BLOCK_NAME, location.getBlockName());
		values.put(LocationEntry.COLUMN_NAME_FLOOR, location.getFloor());

		return db.insert(LocationEntry.TABLE_NAME, null, values);
	}

	public Location read(long locationID)
	{
		SQLiteDatabase db = dbHelper.getReadableDatabase();

		String selectQuery = "SELECT * FROM " + LocationEntry.TABLE_NAME + " WHERE " + LocationEntry._ID + " = " + String.valueOf(locationID) + ";";

		Cursor c = db.rawQuery(selectQuery, null);

		if( c != null )
		{
			if( c.moveToFirst() )
			{

			}
			else
			{
				Log.w(TAG, "Location not found");
			}
		}

		Location l = new Location();
		l.setId(c.getLong(c.getColumnIndex(LocationEntry._ID)));
		l.setBlockName(c.getString(c.getColumnIndex(LocationEntry.COLUMN_NAME_BLOCK_NAME)));
		l.setFloor(c.getString(c.getColumnIndex(LocationEntry.COLUMN_NAME_FLOOR)));

		return l;
	}

	public List<Location> readAll()
	{
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		List<Location> location = new ArrayList<Location>();
		String selectQuery = "SELECT * FROM " + LocationEntry.TABLE_NAME + ";";

		Cursor c = db.rawQuery(selectQuery, null);

		if( c != null )
		{
			if( c.moveToFirst() )
			{
				do
				{
					Location lc = new Location();
					lc.setId(c.getLong(c.getColumnIndex(LocationEntry._ID)));
					lc.setBlockName(c.getString(c.getColumnIndex(LocationEntry.COLUMN_NAME_BLOCK_NAME)));
					lc.setFloor(c.getString(c.getColumnIndex(LocationEntry.COLUMN_NAME_FLOOR)));

					location.add(lc);
				} while( c.moveToNext() );
			}
		}

		if( c != null && !c.isClosed() )
		{
			c.close();
		}

		return location;
	}

	public int update(Location location)
	{
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(LocationEntry.COLUMN_NAME_BLOCK_NAME, location.getBlockName());
		values.put(LocationEntry.COLUMN_NAME_FLOOR, location.getFloor());

		return db.update(LocationEntry.TABLE_NAME, values, LocationEntry._ID + " = ?", new String[] { String.valueOf(location.getId()) });
	}

	public int delete(long locationID)
	{
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		return db.delete(LocationEntry.TABLE_NAME, LocationEntry._ID + " = ?", new String[] { String.valueOf(locationID) });
	}

	public int deleteAll()
	{
		SQLiteDatabase db = dbHelper.getReadableDatabase();

		return db.delete(LocationEntry.TABLE_NAME, "1", null);
	}

	public void printAll()
	{
		List<Location> list = readAll();
		for( int i = 0; i < list.size(); ++i )
		{
			Location actual = list.get(i);
			System.out.println(
					actual.getId() + " " +
					actual.getBlockName() + " " +
					actual.getFloor());
		}
	}
}
