package wifi.mobv.fei.stuba.sk.wifiscanner.model.db;

/**
 * Created by maros on 1.12.2016.
 */

public class History
{
	public History(long idLocation)
	{
		this.idLocation = idLocation;
	}

	public History() {}

	private long id;
	private long idLocation;
	private String date;

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public long getLocationID()
	{
		return idLocation;
	}

	public void setLocationID(long idLocation)
	{
		this.idLocation = idLocation;
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
