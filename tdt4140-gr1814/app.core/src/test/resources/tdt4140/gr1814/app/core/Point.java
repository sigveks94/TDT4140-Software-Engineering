#USING 6 DIGITS FOR ADEQUATE, BUT NOT ABUNDANT ACCURACY


public class Point {
private float latitude;
private float longtitude;

public Point(float lat, float longt) {
	this.latitude=reduceDigitAmount(lat);
	this.longtitude=reduceDigitAmount(longt);
}
public float reduceDigitAmount(float num){
	float myFloat = (float)((int)( myFloat *10000f))/10000f;
	return myFloat;
}
}
