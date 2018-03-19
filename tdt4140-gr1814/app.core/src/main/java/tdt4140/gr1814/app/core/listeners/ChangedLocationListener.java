package tdt4140.gr1814.app.core.listeners;

import tdt4140.gr1814.app.core.zones.Point;

public interface ChangedLocationListener {
	
	public void onLocationChanged(String deviceId,Point point);

}
