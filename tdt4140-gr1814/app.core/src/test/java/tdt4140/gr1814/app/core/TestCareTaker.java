package tdt4140.gr1814.app.core;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import participants.Caretaker;
import participants.Patient;

public class TestCareTaker {

	private Caretaker cT;
	
	@Before
	public void setUp() {
		cT = new Caretaker("Example1","1H8j24s4@","test","test","nedrevei 3");
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
	
	@Test
	public void checkGetAndAddPatients() {
		Patient pat = Patient.newPatient("ForN","EtterN",'M',24076787291L,19747298,"example@ntnu.no","A92");
		cT.addPatients(pat);
		assertEquals(cT.getPatients().get(0),pat);
	}
	@Test
	public void listenerTest() {
		Patient pat = Patient.newPatient("ForN","EtterN",'M',24076787391L,19747298,"example@ntnu.no","A92");
		pat.addListeners(cT);
		assertEquals(pat.getListeners().get(0),cT);
	}
	
	
	@After
	public void shutdown() {
		cT = null;
	}

}
