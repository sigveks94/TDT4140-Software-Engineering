package listeners;

import zones.Point;

public interface ChangedLocationListener {
	
	public void onLocationChanged(String deviceId,Point point);

}
