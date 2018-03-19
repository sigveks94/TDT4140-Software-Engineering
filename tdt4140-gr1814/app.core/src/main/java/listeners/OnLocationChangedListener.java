package listeners;

import zones.Point;

//Functional interface which is supposed to be implemented in every class that would like to retrieve information whenever a object get its location changed.

public interface OnLocationChangedListener {

	public void onLocationChanged(String deviceId, Point newLocation);
	
}
