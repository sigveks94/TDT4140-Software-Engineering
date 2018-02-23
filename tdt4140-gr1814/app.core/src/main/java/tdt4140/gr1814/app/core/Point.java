package tdt4140.gr1814.app.core;

import com.lynden.gmapsfx.javascript.object.LatLong;

public class Point {
	/*USING 5 DECIMALS FOR ADEQUATE, BUT NOT ABUNDANT ACCURACY*/

	
private String keyPatient;
private double latitude;
private double longtitude;

public Point(String keyPatient, double d, double e) {
	this.keyPatient=keyPatient;
	this.latitude=d;
	this.longtitude=e;
}

public double getLat() {
	return this.latitude;
}
public double getLongt(){
	return this.longtitude;
}
public String getPatientKey() {
	return this.keyPatient;
}

public LatLong getLatLong() {
	return new LatLong(this.latitude, this.longtitude);
}

@Override
public String toString() {
	return keyPatient + " " + latitude + " " + longtitude;
}
}
