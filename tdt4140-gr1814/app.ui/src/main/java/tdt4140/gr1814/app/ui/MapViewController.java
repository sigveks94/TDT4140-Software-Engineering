package tdt4140.gr1814.app.ui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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

public class MapViewController implements Initializable, MapComponentInitializedListener{

	private List<MapViewable> viewables;
	
	public MapViewController() {
		this.viewables = new ArrayList<MapViewable>();
	}
	
	@FXML
	GoogleMapView mapView;
	
	GoogleMap map;
	
	public void addViewables(MapViewable... viewables) {
		for(MapViewable v : viewables) {
			this.viewables.add(v);
			System.out.println("A:" + v.getSSN());
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
		
		for(MapViewable v: this.viewables) {
			MarkerOptions markerOption = new MarkerOptions().position(v.getLatLong()).title(String.valueOf(v.getSSN())).visible(true).icon("mymarker.png");
			map.addMarker(new Marker(markerOption));
		}
		
	}
	
}