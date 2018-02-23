package tdt4140.gr1814.app.core;

import java.util.ArrayList;
import java.util.UUID;


//This is the patien-class containing necessary information for the users of the system. 
//TODO - implement a interface making patient-objects listeners with updateCurrentPos() function.
public class Patient implements ChangedLocationListener{
	
	private ArrayList<CareTaker> Caretaker = null;
	
	private String FirstName;
	private String Surname;
	private char  Gender;
	private Long SSN; //We will use the SSN as a key for finding the patient profile in the database
	private int NoK_cellphone; //NoK  = next of kin
	private String NoK_email;
	
	private String DeviceID; //We will use the DeviceID to connect the incomming GPS-signals to the corresponding patient profile
	private Point CurrentPoint = null;
	private ZoneRadius Zone = null;
	
	public Patient(String FirstName, String Surname, char Gender, Long SSN, int NoK_cellphone, String NoK_email) {
		this.FirstName = FirstName;
		this.Surname = Surname;
		this.Gender = Gender;
		this.SSN = SSN;
		this.NoK_cellphone = NoK_cellphone;
		this.NoK_email = NoK_email;
		this.DeviceID =  UUID.randomUUID().toString(); //generates a 'random' ID. This will be used as a part of the gps-data.  	
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

	public ZoneRadius getZone() {
		return Zone;
	}

	public void setZone(ZoneRadius zone) {
		Zone = zone;
	}

	public Point getCurrentPoint() {
		return CurrentPoint;
	}
	@Override
	public void onLocationChanged(String deviceID,Point point) {
		if (deviceID.equals(this.getID())) { //double-checking ;) not necessary.
		CurrentPoint = point;
		}
		//add logic for updating position on caretakers UI?
		
	}

	public ArrayList<CareTaker> getCaretaker() {
		return Caretaker;
	}

	public void addCaretaker(CareTaker caretaker) {
		if(!Caretaker.contains(caretaker)) {
		Caretaker.add(caretaker);
		}
	}


	
}
