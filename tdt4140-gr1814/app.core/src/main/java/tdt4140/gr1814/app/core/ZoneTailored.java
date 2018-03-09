package tdt4140.gr1814.app.core;
import java.util.ArrayList;

// IT IS CRUCIAL THAT FUTHER HANDLING OF THIS CLASS TAKES INTO CONSIDERATION THAT THE ORDER OF THE POINTS NEED TO BE UNCHANGED TO KEEP A 
//POLYGON FORM INSTEAD OF OVERLAPPING FORMS LIKE A DAVIDS STAR

public class ZoneTailored implements Zone{
private ArrayList<Point> points;
public ZoneTailored(Point ...args) {//CONSTRUCTOR TAKES VARARG PARAMETRES FROM MAP INTERFACE AND CONSTRUCTS A TAILORED ZONE OBJECT
	points = new ArrayList<Point>();
	for (Point arg : args) {
		this.points.add(arg);
	}
}

public int getNumberOfPoints() {
	return points.size();
}

public ArrayList<Point> getPoints(){
	return this.points;
}
public ArrayList<ArrayList<Double>> getPointsToDatabaseFormat(){ //RETURNS THIS ZONE WITH ITS POINTS AS A DOUBLE ARRAY IN ORDER TO USE INDEXES AS ORDER NUMBER
	ArrayList<ArrayList<Double>> arrayToDB = new ArrayList<>();
	for (Point p:points) {
		arrayToDB.add(p.getPointToArrayForm());
	}
	return arrayToDB;	
}
@Override
public Boolean isInsideZone(Point p) {
	//Checks how many times the point crosses any line when you go south from the point
	//If it crosses an odd number of lines then it is inside the zone, else outside
	//Acting strange when on lines but that is not a problem in this project
	int k;
	int count = 0;
	for (int i = 0 ; i < points.size() ; i++) {
		if (i == points.size() - 1) {
			k = 0;
		} else {
			k = i + 1;
		}
		if ((points.get(i).getLongt() <= p.getLongt())&&(points.get(k).getLongt() > p.getLongt())) {
			double midle = (p.getLongt()) - points.get(i).getLongt()/(points.get(k).getLongt() - points.get(i).getLongt());
			if (p.getLat() >= (midle * (points.get(k).getLat() - points.get(i).getLat())) + points.get(i).getLat()) {
				count += 1;
			}
		} else if ((points.get(i).getLongt() >= p.getLongt())&&(points.get(k).getLongt() < p.getLongt())) {
			double midle = (p.getLongt() - points.get(i).getLongt())/(points.get(k).getLongt() - points.get(i).getLongt());
			if (p.getLat() >= (midle * (points.get(k).getLat() - points.get(i).getLat())) + points.get(i).getLat()) {
				count += 1;
			}
		}
	}
	return ((count % 2) == 1) ;
}
}
