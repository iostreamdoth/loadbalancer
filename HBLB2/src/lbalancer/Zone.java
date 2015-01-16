package lbalancer;

import java.util.ArrayList;
import java.util.List;

public class Zone {
	double latitude;
	double longitude;
	String zone;
	public static List<Zone> lstZone = null;
	int request_count = 0;
	int request_served = 0;
	int serverCount = 0;

	public int requestcount = 0;
	public int memorycount = 0;
	public int computecount = 0;
	public int storagecount = 0;
	public int comissionedServer = 0;

	public void comissionAServer(int i) {
		lstServerUnderZone.get(i).isCommissioned = true;
		comissionedServer++;
	}

	public void unComissionAServer(int i) {
		lstServerUnderZone.get(i).isCommissioned = false;
		comissionedServer--;
	}

	public List<ServerInfo> lstServerUnderZone = new ArrayList<ServerInfo>();

	public ServerInfo findNextComissionableServer(Zone oZone,
			List<Zone> lstNAZones) {
		ServerInfo oServerInfo = null;
		for (ServerInfo o : lstServerUnderZone) {
			if (o.isCommissioned == false && oServerInfo != null) {
				oServerInfo = o;
				break;

			}
		}
		if (oServerInfo == null) {
			lstNAZones.add(oZone);
			oServerInfo = findNextComissionableServer(
					getNearestZoneGZero(oZone.latitude, oZone.longitude,
							lstNAZones), lstNAZones);
		}

		return oServerInfo;
	}

	public Zone() {
		
		//System.out.println("Sum is");

		if (lstZone == null) {
			
			lstZone = new ArrayList<Zone>() {
				{
					add(new Zone(38.030786, -78.793945, "zone1"));
					add(new Zone(43.92955, -120.54199, "zone2"));
					add(new Zone(40.913513, -122.34375, "zone3"));
					add(new Zone(53.120405, -8.4375, "zone4"));
					add(new Zone(50.111002, 8.683319, "zone5"));
					add(new Zone(1.279801, 103.846893, "zone6"));
					add(new Zone(35.748741, 139.21463, "zone7"));
					add(new Zone(-33.86814, 151.2117, "zone8"));
					add(new Zone(-23.46325, -46.625977, "zone9"));
				}
			};

		}
		//System.out.println("Sum is " + lstZone.size());
	}

	public Zone(double lat, double lng, String zoneid) {
		latitude = lat;
		longitude = lng;
		zone = zoneid;
	}

	public static Zone getZoneObject(String zoneid) {
		Zone oZone = new Zone();
		for (int i = 0; i < lstZone.size(); i++) {
			
			if (lstZone.get(i).zone.equals(zoneid)) {
				System.out.println(zoneid+"," + lstZone.get(i).zone);
				try {
					
					// oZone = (Zone) lstZone.get(i).clone();
					oZone = lstZone.get(i);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return oZone;
	}

	public static Zone getNearestZone(double lat, double lng) {
		Zone oZone = null;
		double minDistance = Double.MAX_VALUE;
		int pos = 0;
		double distance;
		String testl="";
		for (int i = 0; i < lstZone.size(); i++) {
			Zone o = lstZone.get(i);
			distance = CalculateDistance(lat, lng, o.latitude, o.longitude);
			testl += "," + distance;
			
			if (minDistance > distance) {
				minDistance = distance;
				pos = i;
			}
		}
		
		oZone = lstZone.get(pos);
		//System.out.println(testl + ", Selected Zone=" + oZone.zone);
		return oZone;
	}

	public static Zone getNearestZoneGZero(double lat, double lng,
			List<Zone> lstNAZones) {
		Zone oZone = null;
		double minDistance = Double.MAX_VALUE;
		int pos = 0;
		double distance;
		boolean inNAList = false;
		for (int i = 0; i < lstZone.size(); i++) {
			Zone o = lstZone.get(i);

			for (Zone o1 : lstNAZones) {
				if (o1.zone == o.zone) {
					inNAList = true;
				}
			}

			if (!inNAList) {
				distance = CalculateDistanceGZero(lat, lng, o.latitude,
						o.longitude);
				if (distance < 0) {
					distance = -distance;
				}
				if (minDistance > distance && distance > 0) {
					minDistance = distance;
					pos = i;
					
				}
				
			}
		}
		oZone = lstZone.get(pos);
		return oZone;
	}

	public static double CalculateDistance(double l1, double lon1, double l2,
			double lon2) {

		double R = 6371;
		double phi1 = Math.toRadians(l1);
		double phi2 = Math.toRadians(l2);
		double deltaPhi = Math.toRadians(l1 - l2); // (lat2-lat1).toRadians();
		double deltaLambda = Math.toRadians(lon1 - lon2);

		double a = Math.sin(deltaPhi / 2) * Math.sin(deltaPhi / 2)
				+ Math.cos(phi1) * Math.cos(phi2) * Math.sin(deltaLambda / 2)
				* Math.sin(deltaLambda / 2);
		a = Math.abs(a);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		double d = R * c;
		return d;
	}
	private static double deg2rad(double deg) {
	    return (deg * Math.PI / 180.0);
	}
	public static double CalculateDistanceGZero(double l1, double lon1,
			double l2, double lon2) {

		double R = 6371;
		double phi1 = Math.toRadians(l1);
		double phi2 = Math.toRadians(l2);
		double deltaPhi = Math.toRadians(l1 - l2); // (lat2-lat1).toRadians();
		double deltaLambda = Math.toRadians(lon1 - lon2);

		double a = Math.sin(deltaPhi / 2) * Math.sin(deltaPhi / 2)
				+ Math.cos(phi1) * Math.cos(phi2) * Math.sin(deltaLambda / 2)
				* Math.sin(deltaLambda / 2);
		a = Math.abs(a);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		double d = R * c;
		return d;
	}

}
