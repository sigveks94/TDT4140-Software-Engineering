package tdt4140.gr1814.app.ui;

import java.sql.SQLException;
import java.util.List;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tdt4140.gr1814.app.core.CareTaker;
import tdt4140.gr1814.app.core.Database;
import tdt4140.gr1814.app.core.InputController;
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
	    	for (Patient p: Patient.patients) {
	    		InputController.addPatientInList(p);
	    	}
	    	CareTaker morentilharald = new CareTaker("blabla","jadajada");
	    	//changing location to Gløshaugen
	    	Point start = new Point(Patient.patients.get(0).getID(), 63.418474, 10.402892);
	    	Point start2 = new Point(Patient.patients.get(1).getID(), 63.418000, 10.402000);
	    	
	    	Patient.patients.get(0).addZone(start, null);
	    	Patient.	patients.get(0).changeLocation(start);
	    	Patient.patients.get(1).addZone(start2, null);
	    	Patient.	patients.get(1).changeLocation(start2);
	    	
	    	Patient.	patients.get(0).addListeners(morentilharald);
	    	
	    launch(args);
	    }
}


