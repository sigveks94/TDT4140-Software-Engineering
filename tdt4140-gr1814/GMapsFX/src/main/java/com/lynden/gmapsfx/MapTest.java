package com.lynden.gmapsfx;

import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MapOptions;
import com.lynden.gmapsfx.shapes.Circle;
import com.lynden.gmapsfx.shapes.CircleOptions;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEvent;
import javafx.stage.Stage;

public class MapTest extends Application implements MapComponentInitializedListener{

	GoogleMapView GMap;
	GoogleMap map;
	
	public static void main(String[] args) {
		System.setProperty("java.net.useSystemProxies", "true");
		launch(args);
	}

    @Override
    public void mapInitialized() {
        LatLong center = new LatLong(63.446827, 10.421906);
        
        MapOptions options = new MapOptions();
        options.center(center).zoom(7);
        map = GMap.createMap(options);
        
        CircleOptions copt = new CircleOptions()
        		.center(center)
        		.radius(3000);
        
        Circle c = new Circle(copt);
        map.addMapShape(c);
        
        
    }
    
	@Override
	public void start(Stage arg0) throws Exception {
		GMap = new GoogleMapView();
		GMap.addMapInitializedListener(this);
		GMap.setDisableDoubleClick(true);
		GMap.getWebview().getEngine().setOnAlert((WebEvent<String> event) -> {});
		
		BorderPane p = new BorderPane();
		p.setCenter(GMap);
		
		Scene scene = new Scene(p);
		arg0.setScene(scene);
		arg0.show();
	}
}
