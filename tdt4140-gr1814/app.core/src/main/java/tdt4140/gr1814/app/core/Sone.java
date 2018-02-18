package tdt4140.gr1814.app.core;
import java.util.ArrayList;
import java.util.Collections;

public class Sone {
private ArrayList<Integer> punkter;
public Sone() {
	this.punkter = new ArrayList<Integer>();
}
public void extractInts(Integer ...args) {
	for (Integer arg : args) {
		this.punkter.add(arg);
	}
}
public ArrayList<Integer> getPoints(){
	return this.punkter;
}
public int gettopBorderLine() {
	return Collections.max(punkter);
}
public int getbottomBorderLine() {
	return Collections.min(punkter);
}
public Boolean checkIfInsideBorder(Integer t) {
	return ((this.getbottomBorderLine()<=t)&&(this.gettopBorderLine()>=t));
}
}
