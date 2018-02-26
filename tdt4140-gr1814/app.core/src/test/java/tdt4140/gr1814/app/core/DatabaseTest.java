package tdt4140.gr1814.app.core;

import org.junit.Assert;
import org.junit.Before;

import junit.framework.TestCase;

public class DatabaseTest extends TestCase{
	
	Database db = new Database();
	
	@Before
	public void setUp() {
		db.connect();
	}
	public void insertTest() {
	//	"INSERT INTO Patient(SSN, FirstName, LastName) VALUES (999999,'Mathias','Kroken');"
	}
	}
	
