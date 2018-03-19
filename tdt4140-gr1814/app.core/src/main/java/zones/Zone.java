package zones;

import java.util.ArrayList;

public interface Zone {
public Boolean isInsideZone(Point p);
public ArrayList<Point> getPoints();
public ArrayList<ArrayList<Double>> getPointsToDatabaseFormat();
}
