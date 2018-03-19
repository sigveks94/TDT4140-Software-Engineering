package tdt4140.gr1814.app.core.zones;

import com.lynden.gmapsfx.javascript.object.LatLong;
import java.util.ArrayList;

public class Point {
	/*USING 5 DECIMALS FOR ADEQUATE, BUT NOT ABUNDANT ACCURACY*/

private String deviceId;
private double latitude;
private double longtitude;
private ArrayList<Double> returnArray;

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

public ArrayList<Double> getPointToArrayForm(){
	returnArray = new ArrayList<Double>();
	returnArray.add(this.latitude);
	returnArray.add(this.longtitude);
	return returnArray;
}

@Override
public String toString() {
	return deviceId + " " + latitude + " " + longtitude;
}
}
