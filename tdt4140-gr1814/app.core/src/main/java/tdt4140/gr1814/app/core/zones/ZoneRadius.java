//DISTANCES IN METERS


//CLASS USED FOR WHEN ZONE IS FUNDAMENTAL RADIUS-TYPE
package tdt4140.gr1814.app.core.zones;

import java.util.ArrayList;

public class ZoneRadius implements Zone{
	private Double radius;
	private Point centre;
	private final Double defaultRadius = 100.0;
	private final Double radiusEarth = 6371000.0;
	
	
	public ZoneRadius(Point p, Double rad){
		this.centre=p;
		if (rad != null){
			this.radius=rad;
		}
		else {
			this.radius=defaultRadius;
		}
	}
	public void setRadius(Double rad) {
		this.radius = rad;
	}
	public Double getRadius() {
		return this.radius;
	}
	
	
	public Double DegreestoRadians(Double d) {
		return d*(2*Math.PI)/360;
	}
	//THE FORMULAS USED IN CALCULATE-METHOD CAN BE FOUND HERE: https://www.movable-type.co.uk/scripts/latlong.html WHERE YOU CAN ALSO CHECK VALIDITY
	public Double CalculateDistance(Point f, Point s) {
		Double flat = DegreestoRadians(f.getLat());
		Double flongt = DegreestoRadians(f.getLongt());
		Double slat = DegreestoRadians(s.getLat());
		Double slongt = DegreestoRadians(s.getLongt());
		Double latDist = slat-flat;
		Double longtDist = slongt-flongt;
		Double a = (Math.sin(latDist/2) * Math.sin(latDist/2)) + Math.cos(flat) * Math.cos(slat) * (Math.sin(longtDist/2) * Math.sin(longtDist/2));
		Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		Double distanceMetres = radiusEarth * c;
		
		return distanceMetres;
	}
	@Override
	public Boolean isInsideZone(Point p) {
		return (radius > CalculateDistance(centre, p));
	}
	public Point getCentre() {
		return centre;
	}
	@Override
	public ArrayList<Point> getPoints() {
		ArrayList<Point> arPoi = new ArrayList<Point>();
		arPoi.add(centre);
		return arPoi;
	}
	@Override
	public ArrayList<ArrayList<Double>> getPointsToDatabaseFormat() {
		// Never used in this class
		return null;
	}
	
}
