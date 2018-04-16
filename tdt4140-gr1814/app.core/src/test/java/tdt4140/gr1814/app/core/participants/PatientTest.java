package tdt4140.gr1814.app.core.participants;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import tdt4140.gr1814.app.core.listeners.OnLocationChangedListener;
import tdt4140.gr1814.app.core.participants.Caretaker;
import tdt4140.gr1814.app.core.participants.Patient;
import tdt4140.gr1814.app.core.zones.Point;
import tdt4140.gr1814.app.core.zones.Zone;
import tdt4140.gr1814.app.core.zones.ZoneRadius;

//test of patient class. some tests mainly implemented because of test coverage. 
//genderTest test actual functionality, and emailTest ensures correct string format using regex.
public class PatientTest {
	
	private Patient patient;
	private Patient patient2;
	private Patient patient3;
	
	private Caretaker ct;
	private Zone zone;

	@Before
	public void setUp() {
		String firstname = "Ola";
		String surname = "Nordmann";
		String surname2 = "Hansen";
		char gender = 'M';
		char gender2 = 'F';
		Long SSN = 12345678910L;
		Long SSN2 = 98765432190L;
		int phone = 92252233;
		int phone2 = 87263782;
		String email = "test@email.com";
		String email2 = "example@gmail.com";
		String dev1 = "H822";
		String dev2 = "A289";
		
		
		patient = Patient.newPatient(firstname,surname,gender,SSN,phone,email,dev1, true, false);
		patient2 = Patient.newPatient(firstname,surname2,gender2,SSN2,phone2,email2,dev2, true, false);
		patient3 = Patient.newPatient(firstname,"tulleberta",'D',88855544433L,phone2,email2,dev2, true, false);
		zone = new ZoneRadius(new Point(patient.getID(), 10, 10), 3D);
		ct = new Caretaker("larsern", "hoppla", "lars", "enveija");
	}
	
	@Test
	public void firstnameTest() {
		assertEquals(patient.getFirstName(), "Ola");
	}
	@Test
	public void surnameTest() {
		assertEquals(patient.getSurname(), "Nordmann");
	}
	@Test
	public void genderTest() {
		assertEquals(patient.getGender(), "Male");
		assertEquals(patient3.getGender(), "Uncertain");
	}	
	@Test
	public void SSNTest() {
		assertEquals(patient.getSSN()-12345678910L,0);
	}
	@Test
	public void emailTest() {
		Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
		Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(patient.getNoK_email());
		assertTrue(matcher.find());
	}
	@Test
	public void cellphoneTest() {
		assertEquals(patient.getNoK_cellphone(),92252233);
	}	
	@Test
	public void testAddListeners() {
		Caretaker cT = new Caretaker("Example3","1H8j24s4@","test","øvreveg 2");
		patient.addListeners(cT);
		assertEquals(cT.getPatients().get(0),patient);
		patient2.addListeners(cT);
		assertEquals(cT.getPatients().get(1),patient2);
	}
	@Test
	public void addZoneTest() {
		Point point = new Point("H89",40.0,50.0);
		Double doub = new Double(10.962723);
		ZoneRadius zone = new ZoneRadius(point,doub);
		patient.addZone(zone);
		assertEquals(patient.getZone(),zone);
	}
	@Test
	public void staticPatientListTest() {
		Patient patient3 = Patient.getPatient(12345678910L);
		assertEquals(patient,patient3);
	}
	
	@Test
	public void getAllPatients() {
		List<Patient> patLst = Patient.getAllPatients();
		List<Patient> patLst2 = new ArrayList<Patient>();
		patLst2.add(patient);
		patLst2.add(patient2);
		patLst2.add(patient3);
		assertTrue(patLst.containsAll(patLst2));
		assertTrue(patLst2.containsAll(patLst));
	}
	
	@Test
	public void makingNewPatientWithUsedSSN() {
		Patient patient3 = Patient.newPatient("Exam", "Ple", 'M', 12345678910L, 27929342, "at@at.at", "H723", true, false);
		assertEquals(patient3,patient);
	}
	@Test
	public void getGenderTest() {
		assertEquals(patient.getGender(),"Male");
		assertEquals(patient2.getGender(),"Female");
	}
	@Test
	public void getPatient() {
		assertEquals(Patient.getPatient("H822"),patient);
	}
	@Test
	public void changeLocation() {
		Point poi = new Point("H273",52.3,43.7);
		patient.changeLocation(poi);
		assertEquals(patient.getCurrentLocation(),poi);
	}
	
	@Test
	public void testGetAndDeletePatient() {
		assertNull(Patient.getPatient(19503748571L));
		Patient testP = Patient.newPatient("Huckleberry", "Finn", 'M', 19503748571L, 19856434, "ali@baba.no", "dev342", true, false);
		assertNotNull(Patient.getPatient(19503748571L));
		assertNotNull(testP.getID());
		Patient.deletePatient(19503748571L);
		assertNull(Patient.getPatient(testP.getID()));
		assertFalse(Patient.deletePatient(10000000000L));
	}
	
	@Test
	public void testSetAndGetAlarmActivated() {
		assertTrue(patient.getAlarmActivated());
		patient.setAlarmActivated(false);
		assertFalse(patient.getAlarmActivated());
	}
	
	@Test
	public void testGetFullName() {
		assertEquals("Ola Nordmann", patient.getFullName());
	}
	
	@Test
	public void testAddZoneRadius() {
		patient.addZone(zone);
		assertEquals(zone, patient.getZone());
	}
	
	@Test
	public void testToString() {
		assertEquals("Name: " + patient.getFullName() +
				"\n\nGender: " + patient.getGender() + 
				"\n\nSSN: " + patient.getSSN() + 
				"\n\nDevice ID: " + patient.getID() + 
				"\n\n\tNext of kin\n\nMobile: " + 
				patient.getNoK_cellphone()
				+ "\n\nEmail: " + patient.getNoK_email(), 
				patient.toString());
	}
	
	@After
	public void tearDown() {
		patient = null;
		patient2 = null;
		patient3 = null;
		zone = null;
	}
}
