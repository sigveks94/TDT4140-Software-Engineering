package tdt4140.gr1814.app.core;
public class Point {
	/*USING 5 DECIMALS FOR ADEQUATE, BUT NOT ABUNDANT ACCURACY*/

private double latitude;
private double longtitude;

public Point(double d, double e) {
	this.latitude=d;
	this.longtitude=e;
}

public double getLat() {
	return this.latitude;
}
public double getLongt(){
	return this.longtitude;
}

@Override
public String toString() {
	return latitude + " " + longtitude;
}
}
