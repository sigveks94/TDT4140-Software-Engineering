package tdt4140.gr1814.app.ui;

import java.io.IOException;
import java.sql.SQLException;

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
    //public static String AlarmID = "alarmScreen";                 Alarm not yet implemented
    //public static String AlarmFile = "alarmScreen.fxml"; 

    
    
    @Override
    public void start(Stage primaryStage) {
        //Create a container for the different scenes. Add all static scenes to the containers hashmap
        ScreensController ScreensContainer = new ScreensController();
        
        ScreensContainer.loadScreen(ApplicationDemo.HomescreenID, ApplicationDemo.HomescreenFile);
        ScreensContainer.loadScreen(ApplicationDemo.NewPatientID, ApplicationDemo.NewPatientFile);
        ScreensContainer.loadScreen(ApplicationDemo.MapViewLayoutID, ApplicationDemo.MapViewLayoutFile);
        //ScreensContainer.loadScreen(ApplicationDemo.AlarmID, ApplicationDemo.AlarmFile);
        
        
        ScreensContainer.setScreen(ApplicationDemo.HomescreenID);//screen is added to the root.
        
        Group root = new Group();//ObservableList of children
        root.getChildren().addAll(ScreensContainer);//adds all screens from the ScreensContainer to the Group
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


	public static void main(String[] args) throws SQLException, IOException {
	    	//Temporary Simulation. Loading pre-existing Person objects form database. 
		
	    	Database database = new Database();
	    	database.connect();
	    	database.retrievePatients();
	    	for (Patient p: Patient.patients) {
	    		InputController.addPatientInList(p); 
	    		}
		/*
	    	//Solution while database is down;
	    	Patient o = Patient.newPatient("OSCAR", "VIK", 'M', 12345678910l, 92484769, "osca@mail.no", "id1");
	    	InputController.addPatientInList(o);
	    	Patient h = Patient.newPatient("HAKON", "COLLETT", 'M', 12345678911l, 92484760, "HCB@mail.no", "id2");
	    	InputController.addPatientInList(h);
	    	*/
	    	//Setting location to Gløshaugen-area
	    	Point start = new Point(Patient.patients.get(0).getID(), 63.418140, 10.402736);
	    	Patient.patients.get(0).addZone(start, 300.0);
	    	Patient.	patients.get(0).changeLocation(start);
	    	
	    	Point start2 = new Point(Patient.patients.get(1).getID(), 63.418000, 10.402000);	    	
	    	Patient.patients.get(1).addZone(start2, null);
	    	Patient.	patients.get(1).changeLocation(start2);
	    	
	    	//Make 'morentilharald' responsible person for both person-objects from database. This allows alarm finctionality
	    	CareTaker morentilharald = new CareTaker("Moren til Harald","passord");
	    	Patient.	patients.get(0).addListeners(morentilharald);
	    	Patient.patients.get(1).addListeners(morentilharald); 
	    	
	    //run both inputcontroller, handling inputstream, and the UI(application)
	    InputController.metamorphise();
	    launch(args);    
		}
		
	    
	    

	
    
}


