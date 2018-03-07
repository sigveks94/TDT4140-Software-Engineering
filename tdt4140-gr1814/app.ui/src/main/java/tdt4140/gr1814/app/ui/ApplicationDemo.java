package tdt4140.gr1814.app.ui;

import java.io.IOException;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
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
    public static String AlarmID = "alarmScreen";                 
    public static String AlarmFile = "alarmScreen.fxml"; 
    public static String DeletePatientID = "DeletePatient";
    public static String DeletePatientFile = "DeletePatient.fxml";
    
    
    @Override
    public void start(Stage stage) {
        //Create a container for the different scenes. Add all scenes to the containers hashmap
        ScreensController ScreensContainer = new ScreensController();
        
        ScreensContainer.loadScreen(ApplicationDemo.HomescreenID, ApplicationDemo.HomescreenFile);
        ScreensContainer.loadScreen(ApplicationDemo.NewPatientID, ApplicationDemo.NewPatientFile);
        ScreensContainer.loadScreen(ApplicationDemo.MapViewLayoutID, ApplicationDemo.MapViewLayoutFile);
        ScreensContainer.loadScreen(ApplicationDemo.AlarmID, ApplicationDemo.AlarmFile);
        ScreensContainer.loadScreen(ApplicationDemo.DeletePatientID, ApplicationDemo.DeletePatientFile);
        
        ScreensContainer.setScreen(ApplicationDemo.HomescreenID);//screen is added to the root (set screen to the front of the stack).
        
        StackPane root = new StackPane();//Back-to-front stack of children
        root.getChildren().addAll(ScreensContainer);//adds all screens from the ScreensContainer to the StackPane
        Scene scene = new Scene(root);
        stage.setScene(scene);
        //just for fun. Adding logo and title to the Java-application 
        stage.getIcons().add(new Image(ApplicationDemo.class.getResourceAsStream("RedCross.png"))); 
        stage.setTitle("Demo Sprint1");
        
        stage.show();
    }


	public static void main(String[] args) throws SQLException, IOException {
	    	//Temporary Simulation. Loading pre-existing Person objects form database. 
	    
		Database database = new Database();
	    	database.connect();
	    	database.retrievePatients();
		/*
	    	//Solution from home without vpn, or for when database is down;
	    	Patient o = Patient.newPatient("OSCAR", "VIK", 'M', 12345678910l, 92484769, "osca@mail.no", "id1");
	    	Patient h = Patient.newPatient("HAKON", "COLLETT", 'M', 12345678911l, 92484760, "HCB@mail.no", "id2");
	    	*/
	    	
	    	//Setting zone of the two pre-existing patients from the database. Set center to Gløshaugen-area
	    	Point center1 = new Point(Patient.getPatient("id1").getID(), 63.418140, 10.402736);
	    	Patient.getPatient("id1").addZone(center1, 350.0);

	    	Point center2 = new Point(Patient.getPatient("id2").getID(), 63.418000, 10.402000);	    	
	    	Patient.getPatient("id2").addZone(center2, 100.0);

	    	//Make 'morentilharald' responsible person for harald (from database 'id1'). This allows alarm finctionality
	    	CareTaker HaraldsMother = new CareTaker("Harald's mother","passord");
	    	Patient.getPatient("id1").addListeners(HaraldsMother); 	
	    	
	    //run both inputcontroller, handling inputstream, and the UI(application)
	    InputController.metamorphise(); //this is running on a seperate thread
	    launch(args);    
		}
		
	
    
}


