#DISTANCES IN METERS

#CLASS USED FOR WHEN ZONE IS FUNDAMENTAL RADIUS-TYPE

public class ZoneRadius {
private int radius;
private Point centre;
private const int defaultRadius = 100;

public ZoneRadius(Point p, Integer rad){
	this.centre=p;
	if rad != null{
		this.radius=int(rad);
	}
	else {
		this.radius=defaultRadius;
	}
}
public void setRadius(int rad) {
	this.radius = rad;
}
public int getRadius() {
	return this.radius
}
}
