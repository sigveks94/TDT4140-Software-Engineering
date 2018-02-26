package tdt4140.gr1814.app.core;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RadiusTest {

	protected Point center = null;
	protected ZoneRadius zone = null;
	protected InputController inCon = null;
	
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
	
	/*
	@Test
	public void testPointObject() {
		Point point6 = new Point("HJSH89-HSJEJS",40.65728,65.82834);
		assertEquals(point6.getLat(),40.65728,0.00001);
		assertEquals(point6.getLongt(),65.82834,0.00001);
		assertEquals(point6.toString(),"40.65728 65.82834");
	}
	
	
	@Test
	public void testInputHandler() {
		String str1 = "35.33868 16.54459 \r\n" + "58.20727 87.45917";
		try {
			inCon.metamorphise(str1);
		} catch (Exception e) {e.printStackTrace();}
		Point point4 = inCon.getPoints().get(0);
		Point point5 = inCon.getPoints().get(1);
		
		assertEquals(point4.getLat(),35.33868,0.00001);
		assertEquals(point4.getLongt(),16.54459,0.00001);
		assertEquals(point5.getLat(),58.20727,0.00001);
		assertEquals(point5.getLongt(),87.45917,0.00001);
	}
	*/
	@After
	public void deconstruct() {
		center = null;
		zone = null;
		inCon = null;
	}
}
