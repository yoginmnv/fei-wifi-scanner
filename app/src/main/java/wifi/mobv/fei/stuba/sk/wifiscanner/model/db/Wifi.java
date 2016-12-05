package wifi.mobv.fei.stuba.sk.wifiscanner.model.db;

import java.io.Serializable;

/**
 * Created by maros on 1.12.2016.
 */

public class Wifi implements Serializable {
	public Wifi(long id, long idLocation, String BSSID, String SSID, int maxLevel)
	{
		this.id = id;
		this.idLocation = idLocation;
		this.BSSID = BSSID;
		this.SSID = SSID;
		this.maxLevel = maxLevel;
	}

	/**
	 * Updating location an existing wifi
	 */
	public Wifi(long id, long idLocation, String SSID)
	{
		this.id = id;
		this.idLocation = idLocation;
		this.SSID = SSID;
	}

	/**
	 * Creating new wifi
	 */
	public Wifi(long idLocation, String BSSID, String SSID, int maxLevel)
	{
		this.idLocation = idLocation;
		this.BSSID = BSSID;
		this.SSID = SSID;
		this.maxLevel = maxLevel;
	}

	public Wifi() {}

	private long id;
	private long idLocation;
	private String BSSID;
	private String SSID;
	private int maxLevel;

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

	public void setLocationID(long locationID)
	{
		this.idLocation = locationID;
	}

	public String getBSSID()
	{
		return BSSID;
	}

	public void setBSSID(String BSSID)
	{
		this.BSSID = BSSID;
	}

	public String getSSID()
	{
		return SSID;
	}

	public void setSSID(String SSID)
	{
		this.SSID = SSID;
	}

	public int getMaxLevel()
	{
		return maxLevel;
	}

	public void setMaxLevel(int maxLevel)
	{
		this.maxLevel = maxLevel;
	}

	@Override
	public String toString()
	{
		return "Wifi{" +
				"id=" + id +
				", idLocation=" + idLocation +
				", BSSID='" + BSSID + '\'' +
				", SSID='" + SSID + '\'' +
				", maxLevel=" + maxLevel +
				'}';
	}
}
