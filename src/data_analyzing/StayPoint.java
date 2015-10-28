package data_analyzing;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.commons.math3.ml.clustering.Clusterable;

public class StayPoint implements Clusterable {
	private double latitude;
	private double longitude;
	private long Tstart;
	private long Tend;
	
	public StayPoint(double latitude, double longitude, long Tstart, long Tend) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.Tstart = Tstart;
		this.Tend = Tend;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public long getTstart() {
		return Tstart;
	}

	public long getTend() {
		return Tend;
	}
	
	@Override
	public String toString() {
		Date date1 = new Date(Tstart);
		Date date2 = new Date(Tend);

		DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		String dateFormatted1 = formatter.format(date1);
		String dateFormatted2 = formatter.format(date2);

		return "[" + latitude + "," + longitude + "], Tstart = " + date1 + " " + dateFormatted1 + ", Tend = " + date2 + " " + dateFormatted2;
	}

	@Override
	public double[] getPoint() {
		double[] points = new double[2];
		points[0] = latitude;
		points[1] = longitude;
		
		return points;
	}
}