package tdt4140.gr1814.app.core;

public class InsideCheckTemp {
	
	public Point[] points;
	
	public InsideCheckTemp(Point[] points) {
		this.points = points;
	}
	
	public boolean checkInside(Point p) {
		int k;
		int count = 0;
		for (int i = 0 ; i < points.length ; i++) {
			if (i == points.length - 1) {
				k = 0;
			} else {
				k = i + 1;
			}
			if ((points[i].getLongt() <= p.getLongt())&&(points[k].getLongt() > p.getLongt())) {
				double midle = (p.getLongt()) - points[i].getLongt()/(points[k].getLongt() - points[i].getLongt());
				if (p.getLat() >= (midle * (points[k].getLat() - points[i].getLat())) + points[i].getLat()) {
					count += 1;
				}
			} else if ((points[i].getLongt() >= p.getLongt())&&(points[k].getLongt() < p.getLongt())) {
				double midle = (p.getLongt() - points[i].getLongt())/(points[k].getLongt() - points[i].getLongt());
				if (p.getLat() >= (midle * (points[k].getLat() - points[i].getLat())) + points[i].getLat()) {
					count += 1;
				}
			}
		}
		return ((count % 2) == 1) ;
	}
}