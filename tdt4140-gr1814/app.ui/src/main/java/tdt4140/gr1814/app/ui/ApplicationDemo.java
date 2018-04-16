package tdt4140.gr1814.app.ui;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


import tdt4140.gr1814.app.core.participants.Caretaker;
import tdt4140.gr1814.app.core.participants.Patient;


public class ApplicationDemo extends Application{
	
	//temporary info for caretaker
	public static Caretaker applicationUser;
	
	public static ScreensController ScreensContainer = new ScreensController();
	public static StackPane root = new StackPane();
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
        ScreensContainer.loadScreen(ApplicationDemo.LoginID, ApplicationDemo.LoginFile);        
        ScreensContainer.setScreen(ApplicationDemo.LoginID);//screen is added to the root (set screen to the front of the stack).

        root.getChildren().add(ScreensContainer);
        Scene scene = new Scene(root,700,500);
        stage.setScene(scene);
        //Adding logo to the Java-application bar.
        stage.getIcons().add(new Image(ApplicationDemo.class.getResourceAsStream("pictures/RedCross.png"))); 
        stage.show();
    }
    
    public static void loadScreens() {
        ScreensContainer.loadScreen(ApplicationDemo.HomescreenID, ApplicationDemo.HomescreenFile);
        ScreensContainer.loadScreen(ApplicationDemo.NewPatientID, ApplicationDemo.NewPatientFile);
        ScreensContainer.loadScreen(ApplicationDemo.MapViewLayoutID, ApplicationDemo.MapViewLayoutFile);
        ScreensContainer.loadScreen(ApplicationDemo.PatientOverviewID, ApplicationDemo.PatientOverviewFile);
        ScreensContainer.setScreen(ApplicationDemo.HomescreenID);
        root.getChildren().remove(0);
        root.getChildren().addAll(ScreensContainer);//adds all screens from the ScreensContainer to the StackPane
        for (Patient p : Patient.getAllPatients()) {p.addAlarmListener(ScreensContainer);}
    }


	public static void main(String[] args){launch(args);}		
	//import removed by scenebuilder in MapViewLayout.fxml; <?import com.lynden.gmapsfx.*?>
}


