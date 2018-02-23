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
import com.lynden.gmapsfx.javascript.object.MapOptions;
import com.lynden.gmapsfx.javascript.object.MapTypeIdEnum;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.javascript.object.MarkerOptions;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import tdt4140.gr1814.app.core.OnLocationChangedListener;
import tdt4140.gr1814.app.core.Patient;
import tdt4140.gr1814.app.core.Point;

public class MapViewController implements Initializable, MapComponentInitializedListener, OnLocationChangedListener{

	private Map<Patient, Marker> patientsOnMap;
	
	public MapViewController() {
		this.patientsOnMap = new HashMap<Patient, Marker>();
	}
	
	@FXML
	GoogleMapView mapView;
	
	GoogleMap map;
	
	public void addViewables(Patient... patients) {
		for(Patient p : patients) {
			this.patientsOnMap.put(p, null);
			p.registerListener(this);
			
		}
	}
	
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
		LatLong mapCenter = new LatLong(63.446827, 10.421906);
		
		MapOptions mapOptions = new MapOptions();
		mapOptions.center(mapCenter).zoom(15).mapType(MapTypeIdEnum.ROADMAP).clickableIcons(false).streetViewControl(false).zoomControl(true);
		
		map = mapView.createMap(mapOptions);
		
		for(Patient p: this.patientsOnMap.keySet()) {
			MarkerOptions markerOption = new MarkerOptions().position(new LatLong(p.currentLocation.getLat(), p.currentLocation.getLongt())).title(String.valueOf(p.getSSN())).visible(true);
			Marker marker = new Marker(markerOption);
			map.addMarker(marker);
			this.patientsOnMap.replace(p, marker);
		}
	}

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
	
}