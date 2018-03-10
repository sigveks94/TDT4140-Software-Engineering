package tdt4140.gr1814.app.ui;

import java.net.URL;
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
import com.lynden.gmapsfx.shapes.Circle;
import com.lynden.gmapsfx.shapes.MapShapeOptions;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import tdt4140.gr1814.app.core.OnLocationChangedListener;
import tdt4140.gr1814.app.core.Patient;
import tdt4140.gr1814.app.core.Point;

//This is the controller class that controls the mapview window
public class MapZoneViewController implements Initializable, MapComponentInitializedListener, ControlledScreen{

	private ScreensController myController;

	
	private Patient currentPatient;
	
	@FXML
	GoogleMapView mapView;
	@FXML
	GoogleMap map;
	
	
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
		/*
		if currentPatient.getZone()
		for(Patient p : this.patientsOnMap.keySet()) {
			if(p.getZone() == null) {
				continue;
			}
			Circle zone = new Circle();
			zone.setCenter(p.getZone().getCentre().getLatLong());
			zone.setRadius(p.getZone().getRadius());
			map.addMapShape(zone);
		}
		*/
	}



	@Override
	public void setScreenParent(ScreensController screenParent) {
		myController = screenParent;	
	}
	
	@FXML
	public void goToHome(ActionEvent event) {
		myController.setScreen(ApplicationDemo.HomescreenID);
	}
	
	@FXML
	public void setCurrentPatient(Patient patient) {
		currentPatient = patient;
	}
	
	
}