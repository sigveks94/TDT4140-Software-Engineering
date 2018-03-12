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
	public static String username;
	public static String adress;
	public static String password;

   
    public static String HomescreenID = "HomeScreen";
    public static String HomescreenFile = "HomeScreenGUI.fxml";
    public static String NewPatientID = "NewPatient";
    public static String NewPatientFile = "CreateNewPatient.fxml";
    public static String MapViewLayoutID = "MapViewLayout";
    public static String MapViewLayoutFile = "MapViewLayout.fxml";
    public static String AlarmID = "alarmScreen";                 
    public static String AlarmFile = "alarmScreen.fxml"; 
    //public static String DeletePatientID = "DeletePatient";
    //public static String DeletePatientFile = "DeletePatient.fxml";
    public static String PatientOverviewID = "PatientOverview";
    public static String PatientOverviewFile = "PatientOverview.fxml";
    
    
    @Override
    public void start(Stage stage) {
        //Create a container for the different scenes. Add all scenes to the containers hashmap
        ScreensController ScreensContainer = new ScreensController();
        
        ScreensContainer.loadScreen(ApplicationDemo.HomescreenID, ApplicationDemo.HomescreenFile);
        ScreensContainer.loadScreen(ApplicationDemo.NewPatientID, ApplicationDemo.NewPatientFile);
        ScreensContainer.loadScreen(ApplicationDemo.MapViewLayoutID, ApplicationDemo.MapViewLayoutFile);
        ScreensContainer.loadScreen(ApplicationDemo.AlarmID, ApplicationDemo.AlarmFile);
        //ScreensContainer.loadScreen(ApplicationDemo.DeletePatientID, ApplicationDemo.DeletePatientFile);
        ScreensContainer.loadScreen(ApplicationDemo.PatientOverviewID, ApplicationDemo.PatientOverviewFile);
        //ScreensContainer.loadScreen(ApplicationDemo.MapZoneViewID, ApplicationDemo.MapZoneViewFile);
        
        ScreensContainer.setScreen(ApplicationDemo.HomescreenID);//screen is added to the root (set screen to the front of the stack).
        
        StackPane root = new StackPane();//Back-to-front stack of children
        root.getChildren().addAll(ScreensContainer);//adds all screens from the ScreensContainer to the StackPane
        Scene scene = new Scene(root);
        stage.setScene(scene);
        //just for fun. Adding logo and title to the Java-application 
        stage.getIcons().add(new Image(ApplicationDemo.class.getResourceAsStream("RedCross.png"))); 
        stage.setTitle("Demo Sprint1");
        stage.setWidth(700);
        stage.setHeight(500);
        stage.show();
    }


	public static void main(String[] args) throws SQLException, IOException {
	    	//Demo Simulation. Loading pre-existing Person objects form database. 
		ApplicationDemo.username = "Tempe Omsorgsenter";
		ApplicationDemo.adress = "Valøyvegen 12, \n7031 Trondheim, Norge";
	    
		Database database = new Database();
	    	database.connect();
	    	database.retrievePatients();
		/*
	    	//Solution from home without vpn, or for when database is down;
	    	Patient oscar = Patient.newPatient("OSCAR", "VIK", 'M', 12345678910l, 92484769, "oscar@mail.no", "id1");
	    	Patient hakon = Patient.newPatient("HAKON", "COLLETT", 'M', 12345678911l, 92484760, "Haakon-CB@mail.no", "id2");
	    	Patient sigve = Patient.newPatient("SIGVE", "SVENKERUD", 'M', 90987654321l, 92809043, "sigves_mor@mail.no", "id3");
	    	*/
	    	//Setting zone of the two pre-existing patients from the database. Set center to Gløshaugen-area
	    	ArrayList<Point> points1 = new ArrayList<>();
	    	points1.add(new Point(Patient.getPatient("id1").getID(), 63.419943 , 10.398016));
	    	points1.add(new Point(Patient.getPatient("id1").getID(), 63.420814 , 10.404067));
	    	points1.add(new Point(Patient.getPatient("id1").getID(), 63.416578 , 10.408401));
	    	points1.add(new Point(Patient.getPatient("id1").getID(), 63.415330 , 10.401921));
	    	Zone zone1 = new ZoneTailored(points1);
	    	Patient.getPatient("id1").addZone(zone1);

	    	ArrayList<Point> points2 = new ArrayList<>();
	    	points2.add(new Point(Patient.getPatient("id1").getID(), 63.418153 , 10.400591));
	    	points2.add(new Point(Patient.getPatient("id1").getID(), 63.418825 , 10.405054));
	    	points2.add(new Point(Patient.getPatient("id1").getID(), 63.417116 , 10.407114));
	    	points2.add(new Point(Patient.getPatient("id1").getID(), 63.416636 , 10.403337)); 	
	    	Zone zone2 = new ZoneTailored(points2);
	    	Patient.getPatient("id2").addZone(zone2);

	    	//Make 'morentilharald' responsible person for harald (from database 'id1'). This allows alarm finctionality
	    	Caretaker HaraldsMother = new Caretaker("Harald's mother","pasword");
	    	Patient.getPatient("id1").addListeners(HaraldsMother); 	
	    	
	    //run both inputcontroller, handling inputstream, and the UI(application)
	    InputController.metamorphise(); //this is running on a seperate thread
	    launch(args);    
		}
		
	//import removed by scenebuilder in MapViewLayout.fxml; <?import com.lynden.gmapsfx.*?>
    
}


