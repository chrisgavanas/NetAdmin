package Interfaces;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BaseStationWrapper {
	private String userId;
	private String operator;
	private int mcc;
	private int mnc;
	private long cid;
	private int lac;
	private double latitude;
	private double longitude;
	private Date timeRegistered;

	public BaseStationWrapper(String userId, String operator, int mcc, int mnc,
			long cid, int lac, double latitude, double longitude,
			String timeRegistered) {
		this.userId = userId;
		this.operator = operator;
		this.mcc = mcc;
		this.mnc = mnc;
		this.cid = cid;
		this.lac = lac;
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
		else if (key.equals("operator"))
		{
			return getOperator().toString();
		}
		else if  (key.equals("mcc"))
		{
			Integer i=new Integer(getMcc());
			return i.toString();
		}
		else if (key.equals("mnc"))
		{

			Integer i=new Integer(getMnc());
			return i.toString();
		}
		else if (key.equals("cid"))
		{
			return String.valueOf(getCid());
		}
		else if (key.equals("lac"))
		{
			Integer i=new Integer(getLac());
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

	public String getOperator() {
		return operator;
	}

	public int getMcc() {
		return mcc;
	}

	public int getMnc() {
		return mnc;
	}

	public long getCid() {
		return cid;
	}

	public int getLac() {
		return lac;
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
		return userId + " " + operator + " " + mcc + " " + mnc + " " + cid
				+ " " + lac + " " + latitude + " " + longitude + " "
				+ timeRegistered;
	}
}
