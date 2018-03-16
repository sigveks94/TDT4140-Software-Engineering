package tdt4140.gr1814.app.ui;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import tdt4140.gr1814.app.core.Caretaker;
import tdt4140.gr1814.app.core.Database;
import tdt4140.gr1814.app.core.InputController;
import tdt4140.gr1814.app.core.Patient;
import tdt4140.gr1814.app.core.Point;
import tdt4140.gr1814.app.core.Zone;
import tdt4140.gr1814.app.core.ZoneTailored;

public class ApplicationDemo extends Application{
	
	//temporary info for caretaker
	public static Caretaker applicationUser;
	//nickname and filename for screens used in the application
    public static String LoginID = "LoginScreen";
    public static String LoginFile = "LoginScreen.fxml";
    public static String HomescreenID = "HomeScreen";
    public static String HomescreenFile = "HomeScreenGUI.fxml";
    public static String NewPatientID = "NewPatient";
    public static String NewPatientFile = "CreateNewPatient.fxml";
    public static String MapViewLayoutID = "MapViewLayout";
    public static String MapViewLayoutFile = "MapViewLayout.fxml";
    public static String PatientOverviewID = "PatientOverview";
    public static String PatientOverviewFile = "PatientOverview.fxml";
    
    
    @Override
    public void start(Stage stage) {
        //Create a container for the different scenes. Add all scenes to the containers hashmap
        ScreensController ScreensContainer = new ScreensController();
        
        ScreensContainer.loadScreen(ApplicationDemo.LoginID, ApplicationDemo.LoginFile);
        ScreensContainer.loadScreen(ApplicationDemo.HomescreenID, ApplicationDemo.HomescreenFile);
        ScreensContainer.loadScreen(ApplicationDemo.NewPatientID, ApplicationDemo.NewPatientFile);
        ScreensContainer.loadScreen(ApplicationDemo.MapViewLayoutID, ApplicationDemo.MapViewLayoutFile);
        ScreensContainer.loadScreen(ApplicationDemo.PatientOverviewID, ApplicationDemo.PatientOverviewFile);
        
        ScreensContainer.setScreen(ApplicationDemo.LoginID);//screen is added to the root (set screen to the front of the stack).
        
        StackPane root = new StackPane();//Back-to-front stack of children
        root.getChildren().addAll(ScreensContainer);//adds all screens from the ScreensContainer to the StackPane
        Scene scene = new Scene(root,700,500);
        stage.setScene(scene);
        //just for fun. Adding logo to the Java-application bar.
        stage.getIcons().add(new Image(ApplicationDemo.class.getResourceAsStream("RedCross.png"))); 
        stage.show();
    }


	public static void main(String[] args) throws SQLException, IOException {
	    	//Demo Simulation. 
		ApplicationDemo.applicationUser = new Caretaker("Tempe Omsorgsenter","passord","Valøyvegen 12,\n7031 Trondheim, Norge");

	    //Loading pre-existing Person objects form database.
		Database database = new Database();
	    	database.connect();
	    	database.retrievePatients();
	    	
		/*
	    	Solution from home without vpn, or for when database is down;
	    	Patient oscar = Patient.newPatient("OSCAR", "VIK", 'M', 12345678910l, 92484769, "oscar@mail.no", "id1");
	    	Patient hakon = Patient.newPatient("HAKON", "COLLETT", 'M', 12345678911l, 92484760, "Haakon-CB@mail.no", "id2");
	    	Patient sigve = Patient.newPatient("SIGVE", "SVENKERUD", 'M', 90987654321l, 92809043, "sigves_mor@mail.no", "id3");
	    */
	    	
	    	//Make 'morentilharald' responsible person for harald (from database 'id1'). This allows alarm finctionality
	    	Caretaker HaraldsMother = new Caretaker("Harald's mother","pasword","Heimstadveien 88");
	    	Patient.getPatient("id1").addListeners(HaraldsMother); 	
	    	
	    //run both inputcontroller, handling inputstream, and the UI(application)
	    InputController.metamorphise(); //this is running on a seperate thread
	    launch(args);    
		}
		
	//import removed by scenebuilder in MapViewLayout.fxml; <?import com.lynden.gmapsfx.*?>
    
}


