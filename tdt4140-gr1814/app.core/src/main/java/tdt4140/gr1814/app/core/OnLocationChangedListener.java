package tdt4140.gr1814.app.core;

import com.lynden.gmapsfx.javascript.object.LatLong;

public interface OnLocationChangedListener {

	public void onLocationChanged(String deviceId, Point newLocation);
	
}
