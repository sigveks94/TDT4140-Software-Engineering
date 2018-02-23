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
import tdt4140.gr1814.app.core.MapViewable;
import tdt4140.gr1814.app.core.OnLocationChangedListener;
import tdt4140.gr1814.app.core.Patient;

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
		}
	}
	
	public void addAllViewables(List<Patient> patients) {
		for(Patient p : patients) {
			this.patientsOnMap.put(p, null);
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
		
		map.addMarker(new Marker(new MarkerOptions().position(mapCenter).visible(true).title("Heisann")));
		
		for(Patient p: this.patientsOnMap.keySet()) {
			MarkerOptions markerOption = new MarkerOptions().position(new LatLong(0,0)).title(String.valueOf(p.getSSN())).visible(true);
			map.addMarker(new Marker(markerOption));
		}
	}

	@Override
	public void onLocationChanged(String deviceId, LatLong newLocation) {
		//Update Markers according to changed location
	}
	
}