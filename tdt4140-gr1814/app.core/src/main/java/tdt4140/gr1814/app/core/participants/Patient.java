
package tdt4140.gr1814.app.core.participants;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.scene.control.CheckBox;
import tdt4140.gr1814.app.core.listeners.OnLocationChangedListener;
import tdt4140.gr1814.app.core.listeners.OnPatientAlarmListener;
import tdt4140.gr1814.app.core.zones.Point;
import tdt4140.gr1814.app.core.zones.Zone;
import tdt4140.gr1814.app.core.zones.ZoneRadius;


//This is the patient-class containing necessary information for the users of the system. Also contains the caretakers to be notified
//TODO - implement a interface making patient-objects listeners with updateCurrentPos() function.
public class Patient{
	
	//The static part of this class is supposed to be the interface in which the rest of the system creates and fetches the patient objects needed. The Constructor is made private in order to deny duplicates of what
	//the developer might think is the same patient object, but is in fact not.
	
	//This list contains all the patients that exists in the scope of the care taker currently using the client
	private static List<Patient> patients = new ArrayList<Patient>();
	
	//This is the only mechanism from the outside for instantiating new patient objects. If developer tries to create a new patient object with a SSN that is already registered in the system, this method will
	//simple return that patient object and skip the instantiation. If there is no patient registered with that SSN however the method will instantiate a new patient object, append it to the list of patients and return it.
	public static Patient newPatient(String FirstName, String Surname, char Gender, Long SSN, int NoK_cellphone, String NoK_email, String deviceID, boolean alarmon, boolean makeCheckBoxes) {
		Patient patient = getPatient(SSN);
		if(patient != null) {
			return patient;
		}
		else {
			patient =  new Patient(FirstName, Surname, Gender, SSN, NoK_cellphone, NoK_email, deviceID, alarmon, makeCheckBoxes);
			patients.add(patient);
			return patient;
		}
	}
	
	//This method provides a way to fetch a patient by passing the patients SSN, if no patient with provided SSN exists the method simply returns null
	public static Patient getPatient(Long SSN) {
		for(Patient p: patients) {
			if(p.SSN.compareTo(SSN) == 0) {
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
	
	public static boolean deletePatient(Long patientSSN) {
		for (Patient p : patients) {
			System.out.println(p.getSSN());
			if(p.getSSN().equals(patientSSN)) {
				patients.remove(p);
				return true;
			}
		}
		return false;
	}
	

	
	
//Instance
	
	//Patient information:
	private String FirstName;
	private String Surname;
	private char  Gender;
	private Long SSN; //We will use the SSN as a key for finding the patient profile in the database
	private int NoK_cellphone; //NoK  = next of kin
	private String NoK_email;
	private ArrayList<Caretaker> listeners = new ArrayList<Caretaker>(); 
	//Location-related:
	private String DeviceID; //We will use the DeviceID to connect the incoming GPS-signals to the corresponding patient profile
	private Zone zone;
	private Point currentLocation;
	private List<OnLocationChangedListener> locationListeners;//Screencontroller running with the ApplicationDemo. Used in changeLocation() if patient is outside zone.
	private boolean alarmSent = false;
	private OnPatientAlarmListener screensController;
	private boolean alarmActivated;
	
	//decides wether the patient and its zone shoud be displayed on the map
	private CheckBox viewableOnMap;
	private CheckBox viewZoneOnMap;
	
	
	public Patient(String FirstName, String Surname, char Gender, Long SSN, int NoK_cellphone, String NoK_email,String deviceID, boolean alarmon, boolean makeCheckBoxes) {
		this.FirstName = FirstName;
		this.Surname = Surname;
		this.Gender = Gender;
		this.SSN = SSN;
		this.NoK_cellphone = NoK_cellphone;
		this.NoK_email = NoK_email;
		this.DeviceID =  deviceID;
		this.currentLocation = null;
		this.alarmActivated=alarmon;
		this.locationListeners = new ArrayList<OnLocationChangedListener>();
		this.alarmActivated = alarmon;
		if(makeCheckBoxes) {
			this.viewableOnMap = new CheckBox();
			this.viewZoneOnMap = new CheckBox();
			viewableOnMap.setSelected(true);
		}
	}
	
	
	public String getFirstName() {
		return FirstName;
	}
	public String getSurname() {
		return Surname;
	}
	public boolean getAlarmActivated() {
		return this.alarmActivated;
	}
	public void setAlarmActivated(boolean state) {
		this.alarmActivated=state;
	}
	public String getFullName() {
		return FirstName+" "+Surname;
	}
	
	public ArrayList<Caretaker> getListeners(){
		return this.listeners;
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
	
	public void addZoneRadius(Point p, Double radius){
		this.zone= new ZoneRadius(p, radius);
	}
	public void addZone(Zone zone) {
		this.zone = zone;
	}
	public Zone getZone() {
		return this.zone;
	}
	
	public CheckBox getViewableOnMap() {
		return viewableOnMap;
	}

	public void setViewableOnMap(CheckBox viewableOnMap) {
		this.viewableOnMap = viewableOnMap;
	}
	
	public CheckBox getViewZoneOnMap() {
		return viewZoneOnMap;
	}

	public void setViewZoneOnMap(CheckBox viewZoneOnMap) {
		this.viewZoneOnMap = viewZoneOnMap;
	}
	
	public void registerListener(OnLocationChangedListener listener) { //connects the ScreensController to the patient
		if(!this.locationListeners.contains(listener)) {
			this.locationListeners.add(listener);
		}	
	}
	

	public void addListeners(Caretaker... caretakers) {
		for (Caretaker c: caretakers) {
			if (!(listeners.contains(c))) {
				listeners.add(c);
				if (!(c.getPatients().contains(this))) {
					c.addPatient(this);
				}
			}
		}
	}
	//This is the only way to update the current location of the patient object. 
	//Aswell as updating the location it notifies all location listeners and if needed the responsible care takers.
	public void changeLocation(Point newLoc) {
		//Updates the current location
		this.currentLocation = newLoc;
		//If the current location is outside any permitted zone the respinsible care taker is alerted
		if (zone != null && !(zone.isInsideZone(newLoc)) && (this.alarmActivated)) { 
			if(!(screensController == null) && alarmSent == false) { //alarm is only set of once, the first time the patien is outside permitted zone also checks if alarm is activated.
			screensController.OnPatientAlarm(this);
			alarmSent = true;
			}
			for (Caretaker c: listeners) {
				c.incomingAlert(this, newLoc);
				}
		}else {alarmSent = false;} //the variable controlling that we only send one alarm-signal to controller is reset if patient is inside zone. 
		
		
		//This notifies all location listeners with the new location
		for(OnLocationChangedListener l: this.locationListeners) {
			//Since this function is usually called from another thread than the UI-thread the FXML framework refuses to let the thread dictate UI-elements. Therefor the Platform.runLater is called to ask the UI-thread
			//Do the remaining part of the work which is UI-related (typical instantiating LatLong objects and updating markers on a potential map etc).
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					if (l == null) {
						locationListeners.remove(l);
						return;
					}
					l.onLocationChanged(DeviceID, newLoc);
				}
			});
		}
	}
	
	public void addAlarmListener(OnPatientAlarmListener screensController) {
		this.screensController = screensController;
	}
	
	public OnPatientAlarmListener getAlarmListener() {
		return screensController;
	}
	
	@Override
	public String toString() {
		String output = "Name: "+this.getFullName()+
						"\n\nGender: "+this.getGender()+
						"\n\nSSN: "+this.getSSN()+
						"\n\nDevice ID: "+this.getID()+
						"\n\n\tNext of kin\n\nMobile: "+this.getNoK_cellphone()+
						"\n\nEmail: "+this.getNoK_email();
		return output;
	}


}

