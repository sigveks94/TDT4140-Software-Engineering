package tdt4140.gr1814.app.core;
import java.util.ArrayList;
import java.util.Collections;

public class ZoneTailored {
private ArrayList<Point> points;
public ZoneTailored(ArrayList<Point> p) {
	this.points = new ArrayList<Point>();
}

public void extractInts(Point ...args) {
	for (Point arg : args) {
		this.points.add(arg);
	}
}
public ArrayList<Point> getPoints(){
	return this.points;
}
/*
public int gettopBorderLine() {
	return Collections.max(points);
}
public int getbottomBorderLine() {
	return Collections.min(points);
}
public Boolean checkIfInsideBorder(Integer t) {
	return ((this.getbottomBorderLine()<=t)&&(this.gettopBorderLine()>=t));
}
*/
}
