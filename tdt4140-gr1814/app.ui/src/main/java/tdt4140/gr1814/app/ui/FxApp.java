package tdt4140.gr1814.app.ui;

import java.sql.SQLException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tdt4140.gr1814.app.core.Database;
import tdt4140.gr1814.app.core.Patient;
import tdt4140.gr1814.app.core.Point;

public class FxApp extends Application {
	
	
	@Override
    public void start(Stage stage) throws Exception {
        MapViewController MapController = new MapViewController();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MapViewLayout.fxml"));
        MapController.addAllViewables(Patient.getAllPatients());//Adds all patient-objects from database to the map. None here..
		loader.setController(MapController);
		Parent root = (Parent) loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws SQLException {
    		Patient.newPatient("OSCAR", "VIK", 'M', 12345678910l, 92484769, "osca@mail.no", "id1");
        launch(args); 
    }  
}

