package wifi.mobv.fei.stuba.sk.wifiscanner.model.db;

import android.provider.BaseColumns;

/**
 * Created by maros on 1.12.2016.
 */

public class History
{
	public History(long id, String date)
	{
		this.id = id;
		this.date = date;
	}

	public History(String date)
	{
		this.date = date;
	}

	public History() {}

	private long id;
	private String date;

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public String getDate()
	{
		return date;
	}

	public void setDate(String date)
	{
		this.date = date;
	}
}
