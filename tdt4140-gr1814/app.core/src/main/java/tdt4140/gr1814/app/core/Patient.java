package tdt4140.gr1814.app.core;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javafx.application.Platform;


//This is the patien-class containing necessary information for the users of the system. 
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
	
	public static List<Patient> getAllPatients(){
		List<Patient> lst = new ArrayList<Patient>();
		for(Patient p: patients) {
			lst.add(p);
		}
		
		return lst;
	}
	
	
	
	//Instance
	
	List<OnLocationChangedListener> locationListeners;
	
	public void registerListener(OnLocationChangedListener listener) {
		if(!this.locationListeners.contains(listener)) {
			this.locationListeners.add(listener);
		}
		
	}
	
	
	
	private String FirstName;
	private String Surname;
	private char  Gender;
	private Long SSN; //We will use the SSN as a key for finding the patient profile in the database
	private int NoK_cellphone; //NoK  = next of kin
	private String NoK_email;
	private String DeviceID; //We will use the DeviceID to connect the incomming GPS-signals to the corresponding patient profile
	private Point currentLocation;
	
	private Patient(String FirstName, String Surname, char Gender, Long SSN, int NoK_cellphone, String NoK_email) {
		this.FirstName = FirstName;
		this.Surname = Surname;
		this.Gender = Gender;
		this.SSN = SSN;
		this.NoK_cellphone = NoK_cellphone;
		this.NoK_email = NoK_email;
		this.DeviceID =  UUID.randomUUID().toString(); //generates a 'random' ID. This will be used as a part of the gps-data.
		
		this.currentLocation = new Point(63.446827, 10.421906);
		this.locationListeners = new ArrayList<OnLocationChangedListener>();
	}
	
	public String getFirstName() {
		return FirstName;
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
	
	public Point getCurrentLocation() {
		return this.currentLocation;
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
	
	public void changeLocation(Point newLoc) {
		this.currentLocation = newLoc;
		String devId = this.DeviceID;
		for(OnLocationChangedListener l: this.locationListeners) {
			
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					try {
					l.onLocationChanged(devId, newLoc);
					}
					catch(NullPointerException e) {
						locationListeners.remove(l);
					}
				}
			});
		}
	}

}
