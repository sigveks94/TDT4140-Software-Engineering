package tdt4140.gr1814.app.ui;

import java.sql.SQLException;
import java.util.List;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tdt4140.gr1814.app.core.Database;
import tdt4140.gr1814.app.core.Patient;
import tdt4140.gr1814.app.core.Point;

public class ApplicationDemo extends Application{


    
    public static String HomescreenID = "HomeScreen";
    public static String HomescreenFile = "HomeScreenGUI.fxml";
    public static String NewPatientID = "NewPatient";
    public static String NewPatientFile = "CreateNewPatient.fxml";
    public static String MapViewLayoutID = "MapViewLayout";
    public static String MapViewLayoutFile = "MapViewLayout.fxml";
    
    
    @Override
    public void start(Stage primaryStage) {
        //Create a container for the different scenes. Add all static scenes to the containers hashmap
        ScreensController ScreensContainer = new ScreensController();
        ScreensContainer.loadScreen(ApplicationDemo.HomescreenID, ApplicationDemo.HomescreenFile);
        ScreensContainer.loadScreen(ApplicationDemo.NewPatientID, ApplicationDemo.NewPatientFile);
        ScreensContainer.loadScreen(ApplicationDemo.MapViewLayoutID, ApplicationDemo.MapViewLayoutFile);
       
        ScreensContainer.setScreen(ApplicationDemo.HomescreenID);//screen is added to the root.
        
        Group root = new Group();//ObservableList of children
        root.getChildren().addAll(ScreensContainer);//adds all screens from the ScreensContainer to the Group
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


	public static void main(String[] args) throws SQLException {
	    	//Temporary Simulation. Loading pre-existing Person objects form database. 
	    	Database database = new Database();
	    	database.connect();
	    	database.retrievePatients();
	    	List<Patient> patients = Patient.getAllPatients();
	    	//changing location to Gløshaugen
	    	Point start = new Point(patients.get(0).getID(), 63.418474, 10.402892);
	    	patients.get(0).addZone(start, null);
	    	patients.get(0).updateCurrentLocation(start);
	    launch(args);
	    }
}


