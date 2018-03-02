package tdt4140.gr1814.app.ui;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MapOptions;
import com.lynden.gmapsfx.javascript.object.MapTypeIdEnum;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.javascript.object.MarkerOptions;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import tdt4140.gr1814.app.core.OnLocationChangedListener;
import tdt4140.gr1814.app.core.Patient;
import tdt4140.gr1814.app.core.Point;

//This is the controller class that controls the mapview window
public class MapViewController implements Initializable, MapComponentInitializedListener, OnLocationChangedListener,ControlledScreen{

	private ScreensController myController;
	
	//The hashmap holds track of all the patients that are currently being displayed in the map. Each patient has a marker associated with it, and by passing the patient as key the associated marker is return as the value
	private Map<Patient, Marker> patientsOnMap;
	
	public MapViewController() {
		this.patientsOnMap = new HashMap<Patient, Marker>();
	}
	
	@FXML
	GoogleMapView mapView;
	@FXML
	Button back_button;
	
	GoogleMap map;
	
	//This method recieves a number of patient objects that will appear on the map. Aswell as adding the patient to the hashmap this mapview controller adds itself as a listener to the patient object. Whenever
	//a patient gets it location updated this controller object will be notified in order to update the marker on the map
	public void addViewables(Patient... patients) {
		for(Patient p : patients) {
			this.patientsOnMap.put(p, null);
			p.registerListener(this);	
		}
	}
	
	//Does the same as the method above, argument is List instead of varargs
	public void addAllViewables(List<Patient> patients) {
		for(Patient p : patients) {
			this.patientsOnMap.put(p, null);
			p.registerListener(this);
		}
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		mapView.addMapInitializedListener(this); 
		//this.track();
	}


	/*public void track() {//this is a temporary simulation of the tracking.
		Task task = new Task<Void>() {
            @Override
            public Void call() {
                while (true) {
                	for (Patient p: Patient.patients) {
                		 	p.changeLocation(new Point(p.getID(), p.getCurrentLocation().getLat() - 0.00003, p.getCurrentLocation().getLongt() + 0.00007 ));
                	}
                 try {
						Thread.sleep(1000);
				}catch (InterruptedException e) {
						System.out.println("error in: Thread.sleep(1000);");
						e.printStackTrace();
					}
                }
            }
        }; 
        Thread simu_thread = new Thread(task);
        simu_thread.setDaemon(true);
        simu_thread.start();
	}*/
	
	@Override
	public void mapInitialized() {
		//Sets the mapview center
		LatLong mapCenter = new LatLong(63.423000, 10.400000);		
		
		//Sets the mapview type, denies clickable icons like markers marking shops and other facilities, disables streetview and enables zoomcontrol.
		MapOptions mapOptions = new MapOptions();
		mapOptions.center(mapCenter).zoom(14).mapType(MapTypeIdEnum.ROADMAP).clickableIcons(false).streetViewControl(false).zoomControl(true);
		
		map = mapView.createMap(mapOptions);
		
		//For every patient a marker is created and placed on the map on the location associated with each patient. The hashmap is updated aswell
		for(Patient p: this.patientsOnMap.keySet()) {
			MarkerOptions markerOption = new MarkerOptions().position(new LatLong(p.getCurrentLocation().getLat(), p.getCurrentLocation().getLongt())).title(String.valueOf(p.getSSN())).visible(true);
			Marker marker = new Marker(markerOption);
			map.addMarker(marker);
			this.patientsOnMap.replace(p, marker);
		}
	}

	//This is the method inherited from the "OnLocationChangedListener" interface. Whenever a patient gets it location changed it will notify its listener. This map will be one of its listeners. When the location changes
	//The old marker associated with the patient is removed and a new is placed on the map with the new location. The hashmap is also updated.
	@Override
	public void onLocationChanged(String deviceId, Point newLocation) {
		
		Patient patient = Patient.getPatient(deviceId);
		
		Marker marker = this.patientsOnMap.get(patient);
		if(marker != null) {
			map.removeMarker(this.patientsOnMap.get(patient));
		}
		LatLong latlong = null;
		latlong = new LatLong(newLocation.getLat(), newLocation.getLongt());
		marker = new Marker(new MarkerOptions().position(latlong).visible(true));
		this.patientsOnMap.replace(patient, marker);
		map.addMarker(marker);
	}

	@Override
	public void setScreenParent(ScreensController screenParent) {
		myController = screenParent;	
	}
	
	@FXML
	public void goToHome(ActionEvent event) {
		myController.setScreen(ApplicationDemo.HomescreenID);
	}
	
	
}