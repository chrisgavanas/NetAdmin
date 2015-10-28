package ap_elaboration;

import Interfaces.WifiWrapper;
import java.util.ArrayList;
import java.util.HashMap;

import records.WifiRecords;

public class ApLocator 
{
//------------------------------------------------------------------------------------------------------------------------------------//
	private WifiRecords wifi;
//------------------------------------------------------------------------------------------------------------------------------------//

//------------------------------------------------------------------------------------------------------------------------------------//
	public ApLocator(WifiRecords wifi) 
	{
		setWifi(wifi);
	}
//------------------------------------------------------------------------------------------------------------------------------------//

//------------------------------------------------------------------------------------------------------------------------------------//
//Generates the exacts location of the one access point identified by its bssid.This function caclulates the location of the access points//
//using the algorithm given in the exercise ,it finds the average location of the ap using all the available user data.//	
	public void averageLoc() 
	{
		HashMap<String, ArrayList<WifiWrapper>> ApBssidIndex = wifi.getIndex("bssid").getWifHm();
		for (String key : ApBssidIndex.keySet()) 
		{
			ArrayList<WifiWrapper> value = ApBssidIndex.get(key);
			Float latitude_sum = (float) 0;
			Float longtitude_sum = (float) 0;
			Float weight_sum = (float) 0;
			for (WifiWrapper elem2 : value) 
			{
				Integer rssi = elem2.getLevel();
				Float weight = 1000 * (float) Math.pow((double) 10,(double) (rssi / 10.0));
				Float longtitude = (float) ((elem2.getLongitude() * Math.PI) / 180.0);
				Float latitude = (float) ((elem2.getLatitude() * Math.PI) / 180.0);
				longtitude_sum = longtitude * weight + longtitude_sum;
				latitude_sum = latitude * weight + latitude_sum;
				weight_sum = weight_sum + weight;
			}
			Float final_latitude = (float)(((latitude_sum / weight_sum)*180.0)/Math.PI);
			Float final_longtitude =(float)(((longtitude_sum / weight_sum)*180.0)/Math.PI);
			for (WifiWrapper elem2 : value) 
			{
				elem2.setLatitude(final_latitude);
				elem2.setLongitude(final_longtitude);
			}
		}
	}
//------------------------------------------------------------------------------------------------------------------------------------//

//------------------------------------------------------------------------------------------------------------------------------------//
	public WifiRecords getWifi() 
	{
		return wifi;
	}
//------------------------------------------------------------------------------------------------------------------------------------//

//------------------------------------------------------------------------------------------------------------------------------------//
	public void setWifi(WifiRecords wifi) 
	{	
		this.wifi = wifi;
	}
//------------------------------------------------------------------------------------------------------------------------------------//
}