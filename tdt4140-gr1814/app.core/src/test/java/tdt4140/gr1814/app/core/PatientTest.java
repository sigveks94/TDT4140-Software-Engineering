package tdt4140.gr1814.app.core;

import tdt4140.gr1814.app.core.Patient;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

//test of patient class. some tests mainly implemented because of test coverage. 
//genderTest test actual functionality, and emailTest ensures correct string format using regex.
public class PatientTest {
	
	Patient patient = null;

	@Before
	public void setUp() {
		String firstname = "Ola";
		String surname = "Nordmann";
		char gender = 'M';
		Long SSN = Long.parseLong("12345678910");
		int phone = 92252233;
		String email = "test@email.com";
		String deviceID = "testID";
		patient = new Patient(firstname,surname,gender,SSN,phone,email,deviceID);
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
	}	
	@Test
	public void SSNTest() {
		assertTrue(patient.getSSN().toString().length() == 11);
	}
	@Test
	public void emailTest() {
		Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
		Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(patient.getNoK_email());
		assertTrue(matcher.find());
	}
	@Test
	public void cellphoneTest() {
		assertTrue(String.valueOf(patient.getNoK_cellphone()).length() == 8);
	}	
	
	
	@After
	public void tearDown() {
		patient = null;
	}
}
