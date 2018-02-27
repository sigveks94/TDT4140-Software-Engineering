package tdt4140.gr1814.app.ui;

import java.sql.SQLException;
import java.util.ArrayList;

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
    public static ArrayList<Patient> Patients;
    
    
    @Override
    public void start(Stage primaryStage) {
        //Create a container for the different scenes. Add all static scenes to the containers hashmap
        ScreensController ScreensContainer = new ScreensController();
        ScreensContainer.loadScreen(ApplicationDemo.HomescreenID, ApplicationDemo.HomescreenFile);
        ScreensContainer.loadScreen(ApplicationDemo.NewPatientID, ApplicationDemo.NewPatientFile);
        ScreensContainer.loadScreen(ApplicationDemo.MapViewLayoutID, ApplicationDemo.MapViewLayoutFile);
        
        ScreensContainer.setScreen(ApplicationDemo.HomescreenID);
        
        Group root = new Group();
        root.getChildren().addAll(ScreensContainer);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /*
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     */
	public static void main(String[] args) throws SQLException {
	    	//Temporary Simulation. Loading pre-existing Person objects form database. 
	    	Database database = new Database();
	    	database.connect();
	    	ApplicationDemo.Patients = database.retrievePatients();
	    	System.out.println(ApplicationDemo.Patients.get(0));
	    	ApplicationDemo.Patients.get(0).updateCurrentLocation(new Point(ApplicationDemo.Patients.get(0).getID(), 63.418474, 10.402892));
	    launch(args);
	    }
}


