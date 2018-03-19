package tdt4140.gr1814.app.core;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.lynden.gmapsfx.javascript.object.LatLong;

import tdt4140.gr1814.app.core.zones.Point;
import tdt4140.gr1814.app.core.zones.ZoneRadius;

public class RadiusTest {

	private Point center = null;
	private ZoneRadius zone = null;
	private InputController inCon = null;
	
	@Before
	public void setUp() {
		center = new Point("H8H6S3-929SKS",50.0, 30.0);
		zone = new ZoneRadius(center,(double) 5000000);
		inCon = new InputController();
	}
	
	@Test
	public void testPointInside1() {
		Point point1 = new Point("JS9KS5-9J2H8K",35.33868,16.54459);
		assertTrue(zone.isInsideZone(point1));
	}
	
	@Test
	public void testPointInside2() {
		Point point2 = new Point("HSJ9K2-9JS72H9",50.69082,21.53414);
		assertTrue(zone.isInsideZone(point2));
	}
	
	@Test
	public void testPointOutside() {
		Point point3 = new Point("HS8JS2-JKSJ82",31.57807,88.27476);
		assertFalse(zone.isInsideZone(point3));
	}
	
	@Test
	public void testPointObject() {
		Point point6 = new Point("HJSH89-HSJEJS",40.65728,65.82834);
		assertEquals(point6.getLat(),40.65728,0.00001);
		assertEquals(point6.getLongt(),65.82834,0.00001);
	}

	@Test
	public void testCenter() {
		assertEquals(zone.getCentre().getLat(),50.0,0.001);
		assertEquals(zone.getCentre().getLongt(),30.0,0.001);
	}
	@Test
	public void testRadius() {
		assertEquals(zone.getRadius(),5000000.0,0.001);
	}
	@Test
	public void setRadiusTest() {
		zone.setRadius(2000.0);
		assertEquals(zone.getRadius(),2000.0,0.001);
	}
	@Test
	public void setStandardRad() {
		ZoneRadius zone = new ZoneRadius(center,null);
		assertEquals(zone.getRadius(),100.0,0.001);
	}
	@Test
	public void testDevID() {
		assertEquals(center.getDeviceId(),"H8H6S3-929SKS");
	}
	/*
	@Test
	public void testLatLong() {
		LatLong ll = center.getLatLong();
		System.out.println(ll.getLatitude());
		assertEquals(ll.getLatitude(),50.0,0.001);
		assertEquals(ll.getLongitude(),30.0,0.001);
	}
	*/
	@After
	public void deconstruct() {
		center = null;
		zone = null;
		inCon = null;
	}
}
