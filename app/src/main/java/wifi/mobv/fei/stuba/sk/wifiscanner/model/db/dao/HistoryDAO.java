package wifi.mobv.fei.stuba.sk.wifiscanner.model.db.dao;

import android.provider.BaseColumns;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

	private String getDateTime()
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		Date date = new Date();

		return dateFormat.format(date);
	}
}
