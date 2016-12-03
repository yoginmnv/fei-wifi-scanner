package wifi.mobv.fei.stuba.sk.wifiscanner.model.db;

/**
 * Created by maros on 1.12.2016.
 */

public class Location
{
	public Location(long id, String blockName, String floor)
	{
		this.id = id;
		this.blockName = blockName;
		this.floor = floor;
	}

	public Location(String blockName, String floor)
	{
		this.blockName = blockName;
		this.floor = floor;
	}

	public Location() {}

	private long id;
	private String blockName;
	private String floor;

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public String getBlockName()
	{
		return blockName;
	}

	public void setBlockName(String blockName) { this.blockName = blockName; }

	public String getFloor()
	{
		return floor;
	}

	public void setFloor(String floor)
	{
		this.floor = floor;
	}
}
