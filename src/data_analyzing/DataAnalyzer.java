package data_analyzing;

import java.util.List;
import java.util.LinkedList;

import data_analyzing.StayPoint;
import Interfaces.GpsWrapper;

public class DataAnalyzer {
	private long Tmin;
	private long Tmax;
	private double Dmax;
	
	public DataAnalyzer(long Tmin, long Tmax, double Dmax) {
		this.Tmin = Tmin;
		this.Tmax = Tmax;
		this.Dmax = Dmax;
	}
	
	public List<StayPoint> AnalyzeStayPoints(List<GpsWrapper> points) {
		List<StayPoint> StayPoints = new LinkedList<StayPoint>();
		if (points == null)
			return StayPoints;
		
		int i = 0,j, N = points.size();
		
		while (i < N - 1) {
			j = i + 1;
			while (j < N) {
				long TimeDifference = points.get(j).getDate().getTime() - points.get(j-1).getDate().getTime();			
				if (Math.abs(TimeDifference) > Tmax) {
					TimeDifference = points.get(i).getDate().getTime() - points.get(j-1).getDate().getTime();
					if (Math.abs(TimeDifference) > Tmin)
						StayPoints.add(EstimateCentroid(points, i, j-1));
					i = j;
					break;
				}
				double Distance = calculateDifference(points.get(i).getLatitude(), points.get(i).getLongitude(), points.get(j).getLatitude(), points.get(j).getLongitude());
				if (Distance > Dmax) {
					TimeDifference = points.get(i).getDate().getTime() - points.get(j-1).getDate().getTime();
					if (Math.abs(TimeDifference) > Tmin) {
						StayPoints.add(EstimateCentroid(points, i, j-1));
						i = j;
						break;
					}
					i++;
					break;
				}
				if (j == N - 1) {
					TimeDifference = points.get(i).getDate().getTime() - points.get(j).getDate().getTime();
					if (Math.abs(TimeDifference) > Tmin)
						StayPoints.add(EstimateCentroid(points, i, j));
					i = j;
					break;
				}
				j++;
			}	
		}
		
		return StayPoints;
	}
	
	private StayPoint EstimateCentroid(List<GpsWrapper> points, int from, int to) {
		double latitude = 0, longitude = 0;
		int n = 0;
		
		for (int i = from; i <= to; i++) {
			GpsWrapper curr = points.get(i);
			latitude += curr.getLatitude();
			longitude += curr.getLongitude();
			n++;
		}
		
		return new StayPoint(latitude/n, longitude/n, points.get(from).getDate().getTime(), points.get(to).getDate().getTime());
	}
	
	
	public static double calculateDifference(double lat1, double lon1, double lat2, double lon2) {
		double R = 6378.137;
		double dLat = (lat2 - lat1) * Math.PI / 180;
		double dLon = (lon2 - lon1) * Math.PI / 180;
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double d = R * c;
		
		return d * 1000;
	}
	
}
