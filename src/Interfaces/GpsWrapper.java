package Interfaces;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GpsWrapper {
	private String userId;
	private double latitude;
	private double longitude;
	private Date timeRegistered;

	public GpsWrapper(String userId, double latitude, double longitude,
			String timeRegistered) {
		this.userId = userId;
		this.latitude = latitude;
		this.longitude = longitude;
		try {
			this.timeRegistered = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.parse(timeRegistered);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public String get(String key)
	{
		if (key.equals("userId"))
		{
			return getUserId().toString();
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
			return String.valueOf(getDate());
		}
		return null;
	}
	public String getUserId() {
		return userId;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public Date getDate() {
		return timeRegistered;
	}

	@Override
	public String toString() {
		return userId + " " + latitude + " " + longitude + " " + timeRegistered;
	}
}
