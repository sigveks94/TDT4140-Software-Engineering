package tdt4140.gr1814.app.core;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;

public class ZoneTailoredTest {

	private ZoneTailored zone1;
	private ZoneTailored zone2;
	
	@Before
	public void setUp() {
		Point p1 = new Point("A72",52.0,32.0);
		Point p2 = new Point("A73",53.0,32.0);
		Point p3 = new Point("A74",53.0,33.0);
		Point p4 = new Point("A75",52.0,33.0);
		zone1 = new ZoneTailored(p1,p2,p3,p4);
		Point p11 = new Point("A15",50.0,39.0);
		Point p12 = new Point("A25",51.0,39.0);
		Point p13 = new Point("A35",51.0,41.0);
		Point p14 = new Point("A45",50.0,40.0);
		zone2 = new ZoneTailored(p11,p12,p13,p14);
	}
	
	@Test
	public void testNumberOfPoints() {
		assertEquals(zone1.getPoints().size(),4);
	}
	
	@Test
	public void isInsideTest1() {
		Point p5 = new Point("A76",52.5,32.5);
		assertTrue(zone1.isInsideZone(p5));
	}
	
	@Test
	public void isInsideAndOutsideTest2() {
		Point p15 = new Point("A92",50.5,40.49);
		Point p16 = new Point("A93",50.5,40.51);
		assertTrue(zone2.isInsideZone(p15));
		assertFalse(zone2.isInsideZone(p16));
	}
	
	@Test
	public void isOutsideTest1() {
		Point p6 = new Point("A77",53.5,32.5);
		assertFalse(zone1.isInsideZone(p6));
	}
	
	@After
	public void tearDown() {
		zone1 = null;
		zone2 = null;
	}
}
