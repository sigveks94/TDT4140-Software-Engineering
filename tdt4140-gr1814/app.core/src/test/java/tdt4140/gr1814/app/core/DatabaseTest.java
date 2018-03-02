package tdt4140.gr1814.app.core;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.After;
import static org.junit.Assert.*;
import tdt4140.gr1814.app.core.Database;
import org.junit.Before;
import org.junit.Test;


import junit.framework.TestCase;

public class DatabaseTest{
	
	Database db = null;
	ArrayList<Patient> patients = null;
	Patient p1 = Patient.newPatient("Edvard", "Munch", 'M', 76859483627l, 90875462, "munch@skrik.com", "devideID123");
	
	@Test
	public void testTest() {
		assertTrue(true);
	}
	@Before
	public void setUp() throws SQLException {
		db = new Database();
		db.connect();
		db.insert(p1);
	}
	
	
	
	
	@Test public void retrievePatientsTest() throws SQLException{
		patients = db.retrievePatients();
		assertNotNull(patients);
	}
	
	@Test
	public void insertTest() throws SQLException {
		Patient p10 = Patient.newPatient("Johan", "Bach", 'M', 75648923657l, 90846431, "mozart@gmail.com","ID123");
		db.insert(p10);
		ArrayList<ArrayList<String>> actual = db.query("SELECT * FROM Patient WHERE SSN = 75648923657");
		ArrayList<ArrayList<String>> expected = new ArrayList();
		ArrayList<String> testArray3 = new ArrayList();
		
		testArray3.add("75648923657");
		testArray3.add("Johan");
		testArray3.add("Bach");
		testArray3.add("Male");
		testArray3.add("90846431");
		testArray3.add("mozart@gmail.com");
		testArray3.add("ID123");
		
		expected.add(testArray3);
		db.delete(p10);
		assertEquals(expected, actual);
	}
	
	
	
	@Test
	public void deleteTest() throws SQLException {
		Patient p2 = Patient.newPatient("Frida", "Kahlo", 'F', 18274635178l, 55558888, "kahlo@gmail.com","dID123");
		db.insert(p2);
		db.delete(p2);
		ArrayList<String> arr = new ArrayList();
		assertEquals(arr, db.query("SELECT * FROM Patient WHERE SSN = 18274635178;"));
	}
	
	@Test
	public void queryTest() throws SQLException {
		db.update("UPDATE Patient SET FirstName = 'Kasper' WHERE SSN = 76859483627;");
		ArrayList<ArrayList<String>> hei= db.query("SELECT FirstName FROM Patient WHERE SSN = 76859483627;");
		String expectedName = "Kasper";
		String actualName = hei.get(0).get(0);
		assertEquals(expectedName,actualName);
		
	}
	
	@After
	public void tearDown() {
		db.delete(p1);
	}

}

