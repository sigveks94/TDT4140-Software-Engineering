
package tdt4140.gr1814.app.core;

import java.util.ArrayList;
import java.util.List;

import tdt4140.gr1814.app.ui.ApplicationDemo;
import tdt4140.gr1814.app.ui.ControlledScreen;
import tdt4140.gr1814.app.ui.ScreensController;

import javafx.application.Platform;


//This is the patient-class containing necessary information for the users of the system. Also contains the caretakers to be notified
//TODO - implement a interface making patient-objects listeners with updateCurrentPos() function.
public class Patient{
	
	//The static part of this class is supposed to be the interface in which the rest of the system creates and fetches the patient objects needed. The Constructor is made private in order to deny duplicates of what
	//the developer might think is the same patient object, but is in fact not.
	
	//This list contains all the patients that exists in the scope of the care taker currently using the client
	public static List<Patient> patients = new ArrayList<Patient>();
	
	//This is the only mechanism from the outside for instantiating new patient objects. If developer tries to create a new patient object with a SSN that is already registered in the system, this method will
	//simple return that patient object and skip the instantiation. If there is no patient registered with that SSN however the method will instantiate a new patient object, append it to the list of patients and return it.
	public static Patient newPatient(String FirstName, String Surname, char Gender, Long SSN, int NoK_cellphone, String NoK_email, String deviceID) {
		Patient patient = getPatient(SSN);
		if(patient != null) {
			return patient;
		}
		else {
			patient =  new Patient(FirstName, Surname, Gender, SSN, NoK_cellphone, NoK_email,deviceID);
			patients.add(patient);
			return patient;
		}
	}
	
	//This method provides a way to fetch a patient by passing the patients SSN, if no patient with provided SSN exists the method simply returns null
	public static Patient getPatient(Long SSN) {
		for(Patient p: patients) {
			if(p.SSN == SSN) {
				return p;
			}
		}
		return null;
	}
	
	//This method provides a way to fetch a patient by passing the patients device id, if no patient with provided device id exists the method simply returns null
	public static Patient getPatient(String deviceId) {
		for(Patient p: patients) {
			if(p.DeviceID.contentEquals(deviceId)) {
				return p;
			}
		}
		return null;
	}
	
	//This method returns all patients that are available for the care taker currently using the client
	public static List<Patient> getAllPatients(){
		return patients;
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
	private ArrayList<CareTaker> listeners = new ArrayList<CareTaker>();
	//Location-related:
	private String DeviceID; //We will use the DeviceID to connect the incoming GPS-signals to the corresponding patient profile
	private ZoneRadius zone;
	private Point currentLocation;
	
	public Patient(String FirstName, String Surname, char Gender, Long SSN, int NoK_cellphone, String NoK_email,String deviceID) {
		this.FirstName = FirstName;
		this.Surname = Surname;
		this.Gender = Gender;
		this.SSN = SSN;
		this.NoK_cellphone = NoK_cellphone;
		this.NoK_email = NoK_email;
		this.DeviceID =  deviceID;
		this.currentLocation = null;
		this.locationListeners = new ArrayList<OnLocationChangedListener>();
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
	
	public Point getCurrentLocation() {
		return this.currentLocation;
	}
	
	public String getNoK_email() {
		return NoK_email;
	}
	
	public String getID() {
		return DeviceID;
	}
	
	public void addZone(Point p, Double radius){
		this.zone= new ZoneRadius(p, radius);
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
	
	public void changeLocation(Point newLoc) {
		this.currentLocation = newLoc;
		if (!(zone.isInsideZone(newLoc))) { //We need to add some kind of connection to the ScreensController so that we can display the alarmScreen.fxml
			for (CareTaker c: listeners) {
				c.incomingAlert(this, newLoc);
				}
		}
		String devId = this.DeviceID;
		for(OnLocationChangedListener l: this.locationListeners) {
			
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					if (l == null) {
						locationListeners.remove(l);
						return;
					}
					l.onLocationChanged(devId, newLoc);
				}
			});
		}
	}
	
	@Override
	public String toString() {
		String output = "Patient Profile\nName: "+this.getFullName()+"\nGender: "+this.getGender()+"\nSSN: "+this.getSSN()+"\nDevice ID: "+this.getID()+"\nNext of kin\nMobile: "+this.getNoK_cellphone()+"\nEmail: "+this.getNoK_email();
		return output;
	}
	
	


}

