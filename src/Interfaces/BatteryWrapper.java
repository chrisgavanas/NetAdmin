package Interfaces;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class BatteryWrapper {
    private String userId;
    private int batteryLevel;
    private int plugged;
    private int temperature;
    private int voltage;
    private Date timeRegistered;  
    
    public BatteryWrapper(String userId, int batteryLevel, int plugged, int temperature,int voltage,String timeRegistered) {
    	this.userId = userId;
    	this.batteryLevel = batteryLevel;
    	this.plugged = plugged;
    	this.temperature=temperature;
    	this.voltage=voltage;
		try {
			this.timeRegistered = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timeRegistered);
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
		else if (key.equals("plugged"))
		{
			Integer i=new Integer(isPlugged());
			return i.toString();
		}
		else if  (key.equals("batteryLevel"))
		{
			Integer i=new Integer(getBatteryLevel());
			return i.toString();
		}
		else if  (key.equals("voltage"))
		{
			Integer i=new Integer(getVoltage());
			return i.toString();
		}
		else if  (key.equals("temperature"))
		{
			Integer i=new Integer(getTemperature());
			return i.toString();
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
    
    public int getBatteryLevel() {
    	return batteryLevel;
    }
    
    public int isPlugged() {
    	return plugged;
    }
    
    public Date getDate() {
    	return timeRegistered;
    }
    
    @Override
    public String toString() {
    	return userId + " " + batteryLevel + " " + plugged + " " + timeRegistered;
    }

	public int getTemperature() {
		return temperature;
	}

	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}

	public int getVoltage() {
		return voltage;
	}

	public void setVoltage(int voltage) {
		this.voltage = voltage;
	}
}
