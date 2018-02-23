package tdt4140.gr1814.app.core;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


//This is the patient-class containing necessary information for the users of the system. Also contains the caretakers to be notified
//TODO - implement a interface making patient-objects listeners with updateCurrentPos() function.
public class Patient {
	
	//Static
	
	private static List<Patient> patients = new ArrayList<Patient>();
	
	public static Patient newPatient(String FirstName, String Surname, char Gender, Long SSN, int NoK_cellphone, String NoK_email) {
		Patient patient = getPatient(SSN);
		if(patient != null) {
			return patient;
		}
		else {
			patient =  new Patient(FirstName, Surname, Gender, SSN, NoK_cellphone, NoK_email);
			patients.add(patient);
			return patient;
		}
	}
	
	public static Patient getPatient(Long SSN) {
		for(Patient p: patients) {
			if(p.SSN == SSN) {
				return p;
			}
		}
		return null;
	}
	
	public static Patient getPatient(String deviceId) {
		for(Patient p: patients) {
			if(p.DeviceID.contentEquals(deviceId)) {
				return p;
			}
		}
		return null;
	}
	
	
	
	//Instance
	private String FirstName;
	private String Surname;
	private char  Gender;
	private Long SSN; //We will use the SSN as a key for finding the patient profile in the database
	private int NoK_cellphone; //NoK  = next of kin
	private String NoK_email;
	private String DeviceID; //We will use the DeviceID to connect the incoming GPS-signals to the corresponding patient profile
	private ArrayList<CareTaker> listeners = new ArrayList<CareTaker>();
	private ZoneRadius zone;
	private Point currentLocation;
	
	public Patient(String FirstName, String Surname, char Gender, Long SSN, int NoK_cellphone, String NoK_email, String devideid) {
		this.FirstName = FirstName;
		this.Surname = Surname;
		this.Gender = Gender;
		this.SSN = SSN;
		this.NoK_cellphone = NoK_cellphone;
		this.NoK_email = NoK_email;
		this.DeviceID =  devideid; //generates a 'random' ID. This will be used as a part of the gps-data.  	
	}
	public void updateCurrentLocation(Point p) {
		this.currentLocation=p;
		this.Alarming(currentLocation);
	}
	
	public void addZone(Point p, Double radius){
		this.zone= new ZoneRadius(p, radius);
	}
	
	public void Alarming(Point p) {
		if (!(zone.isInsideZone(p))) {
			for (CareTaker c: listeners) {
				c.incomingAlert(this, p);
			}
		}
	}
	
	public void addListeners(CareTaker... caretakers) {
		for (CareTaker c: caretakers) {
			if (!(listeners.contains(c))) {
				listeners.add(c);
				if (!(c.getPatients().contains(this))) {
					c.addPatient(this);
				}
			}
		}
	}
	
	public String getFirstName() {
		return FirstName;
	}
	
	public ArrayList<CareTaker> getListeners(){
		return this.listeners;
	}
	
	public String getSurname() {
		return Surname;
	}
	
	public String getFullName() {
		return FirstName+" "+Surname;
	}

	public String getGender() {
		switch(Gender) {
			case 'M': return "Male";
			case 'F': return "Female";
			default: return "Uncertain";
		}
	}
	
	public Long getSSN() {
		return SSN;
	}

	public int getNoK_cellphone() {
		return NoK_cellphone;
	}
	
	public String getNoK_email() {
		return NoK_email;
	}
	
	public String getID() {
		return DeviceID;
	}
	
	@Override
	public String toString() {
		String output = "Patient Profile\nName: "+this.getFullName()+"\nGender: "+this.getGender()+"\nSSN: "+this.getSSN()+"\nDevice ID: "+this.getID()+"\nNext of kind\nMobile: "+this.getNoK_cellphone()+"\nEmail: "+this.getNoK_email();
		return output;
	}

}
