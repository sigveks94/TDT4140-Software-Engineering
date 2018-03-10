package tdt4140.gr1814.app.core;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.After;

public class ZoneTailoredTest {

	private ZoneTailored zone1;
	private ZoneTailored zone2;
	
	@Before
	public void setUp() {
		ArrayList<Point> arP1 = new ArrayList<>(); 
		arP1.add(new Point("A72",52.0,32.0));
		arP1.add(new Point("A73",53.0,32.0));
		arP1.add(new Point("A74",53.0,33.0));
		arP1.add(new Point("A75",52.0,33.0));
		zone1 = new ZoneTailored(arP1);
		ArrayList<Point> arP2 = new ArrayList<>(); 
		arP2.add(new Point("A15",50.0,39.0));
		arP2.add(new Point("A25",51.0,39.0));
		arP2.add(new Point("A35",51.0,41.0));
		arP2.add(new Point("A45",50.0,40.0));
		zone2 = new ZoneTailored(arP2);
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
