package tdt4140.gr1814.webserver;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;

public class LoginServletIT{
	
	ConnectionHandler databaseConnection;
	
	@Before
	public void setUp() {
		
	}
	
	@Test
	public void loginFail() {
		
	}
	
	
	@After
	public void tearDown() {
		databaseConnection = null;
	}
}