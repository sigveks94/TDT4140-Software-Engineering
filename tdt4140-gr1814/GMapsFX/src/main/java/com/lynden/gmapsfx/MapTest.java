package com.lynden.gmapsfx;

import java.util.ArrayList;

import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MVCArray;
import com.lynden.gmapsfx.javascript.object.MapOptions;
import com.lynden.gmapsfx.javascript.object.MapShape;
import com.lynden.gmapsfx.shapes.Circle;
import com.lynden.gmapsfx.shapes.CircleOptions;
import com.lynden.gmapsfx.shapes.Polygon;
import com.lynden.gmapsfx.shapes.PolygonOptions;
import com.sun.prism.paint.Color;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEvent;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

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
        options.center(center).
        	zoom(7).
        	clickableIcons(false)
        	.zoomControl(true)
        	.streetViewControl(false);
        map = GMap.createMap(options);
        
        LatLong lat1 = new LatLong(63.0,10.0);
        
        CircleOptions cOpt = new CircleOptions()
        		.center(center)
        		.draggable(true)
        		.editable(true)
        		.clickable(true)
        		.fillColor("green")
        		.fillOpacity(0.4)
        		.strokeColor("red")
        		.strokeOpacity(0.6)
        		.radius(50000);
        
        Circle c = new Circle(cOpt);
        map.addMapShape(c);
        
        System.out.println(c.getCenter());
        System.out.println(c.getRadius());
        
        /*
        LatLong lat1 = new LatLong(63.0,10.0);
        LatLong lat2 = new LatLong(64.0,10.0);
        LatLong lat3 = new LatLong(64.0,11.0);
        LatLong lat4 = new LatLong(63.0,11.0);
        LatLong lat5 = new LatLong(62.5,10.5);
        
        LatLong[] latArr = new LatLong[] {lat1,lat2,lat3,lat4,lat5};
        MVCArray mvc = new MVCArray(latArr);
        
        PolygonOptions polyOpts = new PolygonOptions()
        		.paths(mvc)
        		.strokeColor("red")
        		.fillColor("green")
        		.editable(true)
        		.strokeWeight(2)
        		.fillOpacity(0.4);
        
        Polygon pg = new Polygon(polyOpts);
        map.addMapShape(pg);
        pg.getPath().clear();
        
        GetCoordinatesFromMap getCoo = new GetCoordinatesFromMap(pg.getPath());
        getCoo.calculate();
        ArrayList<double[]> arDo = getCoo.getLatLongSave();
        
        for(int k = 0 ; k < arDo.size() ; k++) {
        	System.out.println(arDo.get(k)[0]);
        	System.out.println(arDo.get(k)[1]);
        	System.out.println("-----------------");
        }
        */
        
        /*
        MVCArray mvc2 = pg.getPath();
        System.out.println(mvc2.getAt(0));
        System.out.println(mvc2.getAt(1));
        System.out.println(mvc2.getAt(2));
        System.out.println(mvc2.getAt(3));
        System.out.println(mvc2.getAt(4));
        LatLong latLong = new LatLong(mvc2.getAt(0));
        System.out.println(latLong.getLatitude());
        System.out.println(latLong.getLongitude());
        */
        
        /*
        InsideCheckTemp check = new InsideCheckTemp(latArr);
        System.out.println(check.checkInside(new LatLong(64.01,10.01)));
        */
        /*
        MVCArray polyBounds = pg.getPaths();
        JSObject jso = polyBounds.getAt(0);
		//LatLong k = (LatLong) jso.getMember("latLng");
        System.out.println(jso.getClass());
        */
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
