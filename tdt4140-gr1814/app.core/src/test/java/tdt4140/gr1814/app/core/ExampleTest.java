package tdt4140.gr1814.app.core;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ExampleTest {
	
	
	List<String> lst;
	
	@Before
	public void setUp() {
		lst = new ArrayList<String>();
		lst.add("#String 1");
		lst.add("#String 2");
	}
	
	@Test
	public void simpleTest() {
		assertTrue(lst.size() > 1);
	}
	
	@After
	public void tearDown() {
		lst = null;
	}
	

}
