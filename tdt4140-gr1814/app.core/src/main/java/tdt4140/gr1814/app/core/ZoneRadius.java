//DISTANCES IN METERS

//CLASS USED FOR WHEN ZONE IS FUNDAMENTAL RADIUS-TYPE
package tdt4140.gr1814.app.core;
public class ZoneRadius {
private int radius;
private Point centre;
private int defaultRadius = 100;

public ZoneRadius(Point p, Integer rad){
	this.centre=p;
	if (rad != null){
		this.radius=rad;
	}
	else {
		this.radius=defaultRadius;
	}
}
public void setRadius(int rad) {
	this.radius = rad;
}
public int getRadius() {
	return this.radius;
}
}
