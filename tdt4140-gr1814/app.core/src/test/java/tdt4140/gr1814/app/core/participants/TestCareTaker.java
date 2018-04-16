package tdt4140.gr1814.app.core.participants;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import tdt4140.gr1814.app.core.participants.Caretaker;
import tdt4140.gr1814.app.core.participants.Patient;
import tdt4140.gr1814.app.core.zones.Point;

public class TestCareTaker {

	private Caretaker cT, ct2;
	
	@Before
	public void setUp() {
		cT = new Caretaker("Example1","1H8j24s4@","test","nedrevei 3");
		ct2 = new Caretaker("Example2","1H8j24s4@","test","nedrevei 3");
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
		Patient pat = Patient.newPatient("ForN","EtterN",'M',24076787291L,19747298,"example@ntnu.no","A92", true, false);
		// standard check
		assertFalse(cT.getPatients().contains(pat));
		cT.addPatients(pat);
		assertEquals(cT.getPatients().get(0),pat);
		// checking that it doesn't add duplicates
		int size1 = cT.getPatients().size();
		cT.addPatient(pat);
		int size2 = cT.getPatients().size();
		assertEquals(size1, size2);
		// check that patient dooes not contain this (caretaker) as
		// listener
		pat.addListeners(cT);
		ct2.addPatient(pat);
	}
	@Test
	public void listenerTest() {
		Patient pat = Patient.newPatient("ForN","EtterN",'M',24076787391L,19747298,"example@ntnu.no","A92", true, false);
		pat.addListeners(cT);
		assertEquals(pat.getListeners().get(0),cT);
	}
	
	@Test
	public void testGettersAndSetters() {
		cT.setName("leif leif");
		assertEquals("leif leif", cT.getName());
		cT.setAddress("veienjaveien 54");
		assertEquals("veienjaveien 54", cT.getAddress());
	}
	
	@Test
	public void testCheckPassword() {
		assertTrue(Caretaker.checkPassword("1H8j24s4@"));
		assertFalse(Caretaker.checkPassword("jallaballa"));
	}
	
	@Test
	public void testIncomingAlert() {
		Patient pat = Patient.newPatient("ForN","EtterN",'M',24076787391L,19747298,"example@ntnu.no","A92", true, false);
		Point point = new Point(pat.getID(), 10, 10);
		cT.incomingAlert(pat, point);
	}
	
	@After
	public void shutdown() {
		cT = null;
	}

}
