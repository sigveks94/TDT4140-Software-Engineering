package tdt4140.gr1814.app.ui;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MVCArray;
import com.lynden.gmapsfx.javascript.object.MapOptions;
import com.lynden.gmapsfx.javascript.object.MapTypeIdEnum;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.javascript.object.MarkerOptions;
import com.lynden.gmapsfx.shapes.Circle;
import com.lynden.gmapsfx.shapes.MapShapeOptions;
import com.lynden.gmapsfx.shapes.Polygon;
import com.lynden.gmapsfx.shapes.PolygonOptions;

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
	private boolean newZoneMap;//screen when adding new zone to a patient.
	private Polygon mapPolygon;
	
	//The hashmap holds track of all the patients that are currently being displayed in the map. Each patient has a marker associated with it, and by passing the patient as key the associated marker is return as the value
	private Map<Patient, Marker> patientsOnMap;
	
	public MapViewController() {
		this.patientsOnMap = new HashMap<Patient, Marker>();
	}
	
	@FXML
	GoogleMapView mapView;
	@FXML
	GoogleMap map;
	@FXML
	Button overview_btn;
	@FXML
	Button menu_btn;

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
	}

	
	@Override
	public void mapInitialized() {
		//Sets the mapview center
		LatLong mapCenter = new LatLong(63.423000, 10.400000);		
		
		//Sets the mapview type, denies clickable icons like markers marking shops and other facilities, disables streetview and enables zoomcontrol.
		MapOptions mapOptions = new MapOptions();
		mapOptions.center(mapCenter)
				  .zoom(13).mapType(MapTypeIdEnum.ROADMAP)
				  .clickableIcons(false)
				  .streetViewControl(false)
				  .zoomControl(true)
				  .fullscreenControl(false);
		
		map = mapView.createMap(mapOptions);
		
		//For every patient a marker is created and placed on the map on the location associated with each patient. The hashmap is updated aswell
		for(Patient p: this.patientsOnMap.keySet()) {
			if(p.getCurrentLocation() == null) {
				continue;
			}
			MarkerOptions markerOption = new MarkerOptions()
					.position(new LatLong(p.getCurrentLocation().getLat(), p.getCurrentLocation().getLongt()))
					.title(String.valueOf(p.getSSN())).visible(true)
					.label(p.getFullName());
			Marker marker = new Marker(markerOption);			
			map.addMarker(marker);
			this.patientsOnMap.replace(p, marker);
		}
		//Implementing tailored zone
		/*
		//Adds the zone for each patient to the map so its visible for the user
		for(Patient p : this.patientsOnMap.keySet()) {
			if(p.getZone() == null) {
				continue;
			}
			Circle zone = new Circle();
			zone.setCenter(p.getZone().getCentre().getLatLong());
			zone.setRadius(p.getZone().getRadius());
			map.addMapShape(zone);
		}*/
		
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
		marker = new Marker(new MarkerOptions().position(latlong).visible(true).label(patient.getFullName()));
		this.patientsOnMap.replace(patient, marker);
		if (!newZoneMap) {
		map.addMarker(marker);
		}
	}

	@Override
	public void setScreenParent(ScreensController screenParent) {
		myController = screenParent;	
	}
	
	@FXML
	public void goToHome(ActionEvent event) {
		patientView();
		myController.setScreen(ApplicationDemo.HomescreenID);
	}
	
	@FXML
	public void goToOverview() {
		patientView();
		myController.setScreen(ApplicationDemo.PatientOverviewID);
	}
	
	public void zoneView(Patient currentPatient) {
		menu_btn.setVisible(false);
		overview_btn.setVisible(true);
		newZoneMap = true;
		for (Patient p: Patient.patients) {
			Marker marker = this.patientsOnMap.get(p);
			if (marker != null) {
			map.removeMarker(marker);
		}}
		PolygonOptions polyOpts;
		LatLong[] latArr;
		if (currentPatient.getZone() == null) {
	        LatLong lat1 = new LatLong(63.3,10.1);
	        LatLong lat2 = new LatLong(63.4,10.1);
	        LatLong lat3 = new LatLong(63.4,10.2);
	        LatLong lat4 = new LatLong(63.3,10.2);
	        
	        latArr = new LatLong[] {lat1,lat2,lat3,lat4};
	        
		} else {
			ArrayList<LatLong> latLongArrayList = new ArrayList<>();
			for (Point poi : currentPatient.getZone().getPoints()) {
				latLongArrayList.add(new LatLong(poi.getLat(),poi.getLongt()));
			}
			latArr = new LatLong[latLongArrayList.size()];
			for (int i = 0; i < latLongArrayList.size(); i++) {
				latArr[i] = latLongArrayList.get(i);
			}
			
		}
		MVCArray mvc = new MVCArray(latArr);
        polyOpts = new PolygonOptions()
        		.paths(mvc)
        		.strokeColor("red")
        		.fillColor("green")
        		.editable(true)
        		.strokeWeight(2)
        		.fillOpacity(0.4);
        mapPolygon = new Polygon(polyOpts);
        map.addMapShape(mapPolygon);
	}
	
	public void patientView() {
		menu_btn.setVisible(true);
		overview_btn.setVisible(false);
		newZoneMap = false;
		if (mapPolygon != null) {mapPolygon.getPath().clear();}
	}
	
	
}