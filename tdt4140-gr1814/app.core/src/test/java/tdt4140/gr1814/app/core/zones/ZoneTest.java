package tdt4140.gr1814.app.core.zones;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ZoneTest {

	Zone testZone = null;
	
	@Before
	public void setUp() {
		
	}
	
	@Test
	public void ZoneRadiusTest() {
		
		// =============== Instantiating ===================
		ZoneRadius zone = new ZoneRadius(new Point("id999", 63.00225d, 10.204050d), null);
		
		assertEquals("Zonecenter latitude is wrong!", 63.00225d, zone.getCentre().getLat(), 0.000f);
		assertEquals("Zonecenter longitude is wrong!", 10.204050d,  zone.getCentre().getLongt(), 0.000f);
		assertEquals("Zoneradius is wrong!", 100d, zone.getRadius(), 0.000f);
		
		assertEquals("GetPoints returns wrong point(s)", 1, zone.getPoints().size());
		assertEquals("GetPoints and getCentre returns different points!", zone.getPoints().get(0), zone.getCentre());
		
		assertNull("This method should return null for this type of zone!", zone.getPointsToDatabaseFormat());
		
		// ==================== Change Radius ====================
		zone.setRadius(300d);
		assertEquals("Zone Radius is should be 300 was: " + zone.getRadius(), 300d, zone.getRadius(), 0.00f);
		
		// ================== Calculations ======================
		
		assertEquals("Could not parse degrees to radians!", 1.57079, (double) zone.DegreestoRadians(90d), 0.00001f);
		
		Point a = new Point("a", 40.00000, 30.0000);
		Point b = new Point("b", 41.00000, 32.0000);
		
		assertEquals("Calculated wrong distance!", 202380.3467, zone.CalculateDistance(a, b), 0.01f);
		
		assertFalse("Is infact outside!", zone.isInsideZone(a));
		assertTrue("is infact inside!", zone.isInsideZone(new Point("x", 63.00223, 10.20404)));
	}
	
	
	@Test
	public void ZoneTailoredTest() {
		ArrayList<Point> points = new ArrayList<Point>();
		Point point_a = new Point("id90", 56.000, 13.000);
		Point point_b = new Point("id90", 56.001, 13.000);
		Point point_c = new Point("id90", 56.000, 13.001);
		Point point_d = new Point("id90", 56.001, 13.001);
		points.add(point_a);
		points.add(point_b);
		points.add(point_c);
		points.add(point_d);
		
		ZoneTailored zone = new ZoneTailored(points);
		
		// ================= Instantiation =====================
		
		assertEquals("Wrong number of points!", 4l, zone.getNumberOfPoints());
		assertTrue("Point is missing!", zone.getPoints().get(0).equals(point_a));
		assertTrue("Point is missing!", zone.getPoints().get(1).equals(point_b));
		assertTrue("Point is missing!", zone.getPoints().get(2).equals(point_c));
		assertTrue("Point is missing!", zone.getPoints().get(3).equals(point_d));

		
		// =================== Calculations ========================
		
		assertTrue("Is infact inside!", zone.isInsideZone(new Point("id90", 56.00005, 13.00005)));
		assertFalse("Is Infacet outside!", zone.isInsideZone(new Point("id90", 55.9998, 13.0005)));

		// ====================== PARSING =========================

		assertEquals("Parserror!", 56.000 ,zone.getPointsToDatabaseFormat().get(0).get(0), 0.000f);
		assertEquals("Parserror!", 13.000 ,zone.getPointsToDatabaseFormat().get(0).get(1), 0.000f);
		assertEquals("Parserror!", 56.001 ,zone.getPointsToDatabaseFormat().get(3).get(0), 0.000f);
		assertEquals("Parserror!", 13.001 ,zone.getPointsToDatabaseFormat().get(3).get(1), 0.000f);
		
		ArrayList<Double> onePoint = new ArrayList<>();
		onePoint.add(10.000);
		onePoint.add(63.000);
		onePoint.add(10.000);
		ArrayList<Double> twoPoint = new ArrayList<>();
		twoPoint.add(10.000);
		twoPoint.add(45.000);
		twoPoint.add(11.000);
		ArrayList<ArrayList<Double>> both_points = new ArrayList<>();
		both_points.add(onePoint);
		both_points.add(twoPoint);
		
		ArrayList<Point> returnPoints = zone.doubleArrayListToSingleArrayListPoints(both_points);
		assertEquals("Wront number of points!", 2, returnPoints.size());
		
	}
	
	@Test
	public void pointTest() {
		Point point = new Point("id9702", 63.0005, 10.2500);
		
		assertEquals("Id was wrong!", "id9702", point.getDeviceId());
		assertEquals("Latitude was wrong!", 63.0005, point.getLat(), 0.0001f);
		assertEquals("Longitude was wrong!", 10.2500, point.getLongt(), 0.0001f);
		
		assertEquals("String was on wrong format!", "id9702 63.0005 10.25", point.toString());
		
	}
	
	
	@After
	public void tearDown() {
		
	}
	
	
}
