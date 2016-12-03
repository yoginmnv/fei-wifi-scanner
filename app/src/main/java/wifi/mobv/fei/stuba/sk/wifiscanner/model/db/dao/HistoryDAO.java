package wifi.mobv.fei.stuba.sk.wifiscanner.model.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import wifi.mobv.fei.stuba.sk.wifiscanner.controller.SQLController;
import wifi.mobv.fei.stuba.sk.wifiscanner.model.db.DBHelper;
import wifi.mobv.fei.stuba.sk.wifiscanner.model.db.History;
import wifi.mobv.fei.stuba.sk.wifiscanner.model.db.Location;

/**
 * Created by maros on 1.12.2016.
 */

public class HistoryDAO
{
	/* Inner class that defines the table contents */
	public static class HistoryEntry implements BaseColumns
	{
		public static final String TABLE_NAME = "history";
		public static final String COLUMN_NAME_ID_LOCATION = "id_location";
		public static final String COLUMN_NAME_DATE = "date";
	}

	public static final String TAG = HistoryDAO.class.getSimpleName();
	private SQLController controller;
	private DBHelper dbHelper;

	public HistoryDAO(SQLController controller, Context context)
	{
		this.controller = controller;
		dbHelper = DBHelper.getInstance(context);
	}

	public long create(History history)
	{
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(HistoryEntry.COLUMN_NAME_ID_LOCATION, history.getLocationID());
		values.put(HistoryEntry.COLUMN_NAME_DATE, getDateTime());

		return db.insert(HistoryEntry.TABLE_NAME, null, values);
	}

	public History read(long historyID)
	{
		SQLiteDatabase db = dbHelper.getReadableDatabase();

		String selectQuery = "SELECT * FROM " + HistoryEntry.TABLE_NAME + " WHERE " + HistoryEntry._ID + " = " + String.valueOf(historyID) + ";";

		Cursor c = db.rawQuery(selectQuery, null);

		if( c != null )
		{
			if( c.moveToFirst() )
			{
				History h = new History();
				h.setId(c.getLong(c.getColumnIndex(HistoryEntry._ID)));
				h.setLocationID(c.getLong(c.getColumnIndex(HistoryEntry.COLUMN_NAME_ID_LOCATION)));
				h.setDate(c.getString(c.getColumnIndex(HistoryEntry.COLUMN_NAME_DATE)));

				return h;
			} else
			{
				Log.w(TAG, "Location not found");
			}
		}

		return null;
	}

	public List<History> readAll()
	{
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		List<History> list = new ArrayList<History>();
		String selectQuery = "SELECT * FROM " + HistoryEntry.TABLE_NAME + ";";

		Cursor c = db.rawQuery(selectQuery, null);

		if( c != null )
		{
			if( c.moveToFirst() )
			{
				do
				{
					History h = new History();
					h.setId(c.getLong(c.getColumnIndex(HistoryEntry._ID)));
					h.setLocationID(c.getLong(c.getColumnIndex(HistoryEntry.COLUMN_NAME_ID_LOCATION)));
					h.setDate(c.getString(c.getColumnIndex(HistoryEntry.COLUMN_NAME_DATE)));

					list.add(h);
				} while( c.moveToNext() );
			}
		}

		if( c != null && !c.isClosed() )
		{
			c.close();
		}

		return list;
	}

	public int update(History history)
	{
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(HistoryEntry.COLUMN_NAME_ID_LOCATION, history.getLocationID());
		values.put(HistoryEntry.COLUMN_NAME_DATE, getDateTime());

		return db.update(HistoryEntry.TABLE_NAME, values, HistoryEntry._ID + " = ?", new String[]{String.valueOf(history.getId())});
	}

	public int delete(long historyID)
	{
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		return db.delete(HistoryEntry.TABLE_NAME, HistoryEntry._ID + " = ?", new String[] { String.valueOf(historyID) });
	}

	public int deleteAll()
	{
		SQLiteDatabase db = dbHelper.getReadableDatabase();

		return db.delete(HistoryEntry.TABLE_NAME, "1", null);
	}

	public void printAll()
	{
		List<History> list = readAll();
		for( int i = 0; i < list.size(); ++i )
		{
			History actual = list.get(i);
			System.out.println(actual.getId() + " " +
					actual.getLocationID() + " " +
					actual.getDate());
		}
	}

	private String getDateTime()
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		Date date = new Date();

		return dateFormat.format(date);
	}
}
