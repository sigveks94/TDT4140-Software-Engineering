package tdt4140.gr1814.app.core;
public class Punkter {
	/*USING 6 DIGITS FOR ADEQUATE, BUT NOT ABUNDANT ACCURACY*/



private float latitude;
private float longtitude;

public Punkter(float lat, float longt) {
	this.latitude=reduceDigitAmount(lat);
	this.longtitude=reduceDigitAmount(longt);
}
public float reduceDigitAmount(float num){
	float myFloat = (float)((int)( num *10000f))/10000f;
	return myFloat;
}

}
