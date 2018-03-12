package tdt4140.gr1814.app.core;

import java.util.ArrayList;


public class Caretaker {

	private String Username;
	private String Password;
	private ArrayList <Patient> Patients= new ArrayList<Patient>();
	public static final String PasswordRegex = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";
	
	
	public Caretaker(String Username, String Password) {
		this.setUsername(Username);
		this.setPassword(Password);	
	}
	
	public void addPatient(Patient...patient) {
		for (Patient p: patient) {
			if (!(Patients.contains(p))) {
				Patients.add(p);
				if (!(p.getListeners().contains(this))) {
					p.addListeners(this);
				}
			}
		}
	}
	
	public void incomingAlert(Patient patient, Point point) {
		System.out.println("ALARM!! For caretaker: " + this.getUsername() +  ". Patient: " + patient.getFullName() + " is currently outside allowed zone. Current position: " + 
		point.getLat() + " " + point.getLongt());
	}
	
	public String getUsername() {
		return Username;
	}


	public void setUsername(String username) {
		Username = username;
	}


	public String getPassword() {
		return Password;
	}


	public void setPassword(String password) {
	    if (password.matches(PasswordRegex)) {
	    	Password = password;
	    }
		//PasswordRegex explained;
		//(?=.*[0-9]) a digit must occur at least once
		//(?=.*[a-z]) a lower case letter must occur at least once
		//(?=.*[A-Z]) an upper case letter must occur at least once
		//(?=.*[@#$%^&+=]) a special character must occur at least once
		//(?=\\S+$) no whitespace allowed in the entire string
		//.{8,} at least 8 characters
	}

	public ArrayList <Patient> getPatients() {
		return Patients;
	}

	public void addPatients(Patient patient) {
		if (!Patients.contains(patient)) {
			Patients.add(patient);
		}
	}


}
