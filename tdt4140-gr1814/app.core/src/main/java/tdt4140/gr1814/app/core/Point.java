package tdt4140.gr1814.app.core;

import com.lynden.gmapsfx.javascript.object.LatLong;

public class Point {
	/*USING 5 DECIMALS FOR ADEQUATE, BUT NOT ABUNDANT ACCURACY*/

	
private String deviceId;
private double latitude;
private double longtitude;

public Point(String deviceId, double d, double e) {
	this.deviceId=deviceId;
	this.latitude=d;
	this.longtitude=e;
}

public double getLat() {
	return this.latitude;
}
public double getLongt(){
	return this.longtitude;
}
public String getDeviceId() {
	return this.deviceId;
}

public LatLong getLatLong() {
	return new LatLong(this.latitude, this.longtitude);
}

@Override
public String toString() {
	return deviceId + " " + latitude + " " + longtitude;
}
}
