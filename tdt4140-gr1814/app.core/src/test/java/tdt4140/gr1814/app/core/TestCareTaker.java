package tdt4140.gr1814.app.core;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TestCareTaker {

	protected CareTaker cT;
	
	@Before
	public void setUp() {
		cT = new CareTaker("Example1","1H8j24s4@");
	}
	
	@Test
	public void userNameCheck() {
		assertEquals(cT.getUsername(),"Example1");
	}
	
	@Test
	public void passWordCheck() {
		assertEquals(cT.getPassword(),"1H8j24s4@");
	}
	
	@Test
	public void changeUserName() {
		cT.setUsername("Example2");
		assertEquals(cT.getUsername(),"Example2");
	}
	
	@Test
	public void changePasswordToValid() {
		cT.setPassword("1j888kL@sdf");
		assertEquals(cT.getPassword(),"1j888kL@sdf");
	}
	
	@Test
	public void changePasswordToInvalid() {
		cT.setPassword("HHH");
		assertEquals(cT.getPassword(),"1H8j24s4@");
	}

}
