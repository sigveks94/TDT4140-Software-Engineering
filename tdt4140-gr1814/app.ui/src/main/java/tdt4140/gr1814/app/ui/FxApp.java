package tdt4140.gr1814.app.ui;

import com.lynden.gmapsfx.javascript.object.LatLong;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tdt4140.gr1814.app.core.MapViewable;

public class FxApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("MapViewLayout.fxml"));
    	Parent root = loader.load();
    	((MapViewController) loader.getController()).addViewables( new MapViewable() {

			@Override
			public LatLong getLatLong() {
				return new LatLong(63.634643, 10.635653);
			}

			@Override
			public int getSSN() {
				return 12;
			}
        	
        }, new MapViewable() {

			@Override
			public LatLong getLatLong() {
				return new LatLong(60.360320, 10.002211);
			}

			@Override
			public int getSSN() {
				return 52;
			}
        	
        });
    	
    	
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
