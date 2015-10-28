package Interfaces;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WifiWrapper {
	private String userId;
	private String ssid;
	private String bssid;
	private int level;
	private int frequency;
	private double latitude;
	private double longitude;
	private Date timeRegistered;

	public WifiWrapper(String userId, String ssid, String bssid, int level,
			int frequency, double latitude, double longitude,
			String timeRegistered) {
		this.userId = userId;
		this.ssid = ssid;
		this.bssid = bssid;
		this.level = level;
		this.frequency = frequency;
		this.latitude = latitude;
		this.longitude = longitude;
		try {
			this.timeRegistered = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.parse(timeRegistered);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	public WifiWrapper(String userId, String ssid, String bssid, int level,
			int frequency, double latitude, double longitude,
			Date timeRegistered) {
		this.userId = userId;
		this.ssid = ssid;
		this.bssid = bssid;
		this.level = level;
		this.frequency = frequency;
		this.latitude = latitude;
		this.longitude = longitude;
		this.timeRegistered = timeRegistered;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setSsid(String ssid) {
		this.ssid = ssid;
	}

	public void setBssid(String bssid) {
		this.bssid = bssid;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public void setTimeRegistered(Date timeRegistered) {
		this.timeRegistered = timeRegistered;
	}

	public String get(String key)
	{
		if (key.equals("userId"))
		{
			return getUserId().toString();
		}
		else if (key.equals("ssid"))
		{
			return getSsid().toString();
		}
		else if  (key.equals("bssid"))
		{
			return getBssid().toString();
		}
		else if (key.equals("level"))
		{

			Integer i=new Integer(getLevel());
			return i.toString();
		}
		else if (key.equals("frequency"))
		{
			Integer i=new Integer(getFrequency());
			return i.toString();
		}
		else if (key.equals("latitude"))
		{
			return String.valueOf(getLatitude());
		}
		else if (key.equals("longitude"))
		{
			return String.valueOf(getLongitude());
		}
		else if (key.equals("timeRegistered"))
		{
			return String.valueOf(getTimeRegistered());
		}
		return null;
	}
	public String getUserId() {
		return userId;
	}

	public String getSsid() {
		return ssid;
	}

	public String getBssid() {
		return bssid;
	}

	public int getLevel() {
		return level;
	}

	public int getFrequency() {
		return frequency;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public Date getTimeRegistered() {
		return timeRegistered;
	}

	@Override
	public String toString() {
		return userId + " " + ssid + " " + bssid + " " + level + " "
				+ frequency + " " + latitude + " " + longitude + " "
				+ timeRegistered;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bssid == null) ? 0 : bssid.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WifiWrapper other = (WifiWrapper) obj;
		if (bssid == null) {
			if (other.bssid != null)
				return false;
		} else if (!bssid.equals(other.bssid))
			return false;
		return true;
	}
}
