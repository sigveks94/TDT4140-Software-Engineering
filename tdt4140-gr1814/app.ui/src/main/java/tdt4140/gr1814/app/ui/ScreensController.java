package tdt4140.gr1814.app.ui;

import tdt4140.gr1814.app.core.OnPatientAlarmListener;
import tdt4140.gr1814.app.core.Patient;

import java.util.HashMap;
import java.util.List;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;



public class ScreensController  extends StackPane implements OnPatientAlarmListener{//StackPane lays out its children in a back-to-front stack.
    
	//Holds the screens to be displayed
    private HashMap<String, Node> screens = new HashMap<>();
    public static MapViewController MapController = new MapViewController();
    
    public ScreensController() {
        super();
        for (Patient p : Patient.patients) {
        		p.addAlarmListener(this);
        }
    }

    //Add the screen to the hashmap
    public void addScreen(String name, Node screen) {
        screens.put(name, screen);
    }

    //Returns the Node(screen) with the appropriate name
    public Node getScreen(String name) {
        return screens.get(name);
    }

    //Loads the fxml file, add the screen to the screens collection and
    //finally injects the screenPane to the controller.
    public void loadScreen(String name, String resource) {
        try {
            FXMLLoader myLoader = new FXMLLoader(getClass().getResource(resource));
            if (name.equals("MapViewLayout")) {//Different way to add the controller for the MapViewLayout.fxml screen. 
			MapController.addAllViewables(Patient.getAllPatients());//Adds all patient-objects from database to the map.
			myLoader.setController(MapController);//make this controller, the controller of the screen
			}
            Parent loadScreen = (Parent) myLoader.load();
            ControlledScreen myScreenControler = ((ControlledScreen) myLoader.getController());
            myScreenControler.setScreenParent(this);
            addScreen(name, loadScreen);
        } catch (Exception e) {
        		System.out.println("Did not add Screen");
            e.printStackTrace();
            
        }
    }

    //Tries to displayed the screen with a predefined name.
    //First it makes sure the screen has been already loaded.  Then if there is more than
    //one screen the new screen is been added second, and then the current screen is removed.
    // If there isn't any screen being displayed, the new screen is just added to the root.
    public void setScreen(final String name) {       
        if (screens.get(name) != null) {  
            if (!getChildren().isEmpty()) {    //if there is more than one screen
                getChildren().remove(0);                    //remove the displayed screen
                getChildren().add(0, screens.get(name));
            } 
            else {getChildren().add(screens.get(name));}//no one else been displayed, then just show       
        } 
        else {System.out.println("screen hasn't been loaded!!!");}
    }

    //This method will remove the screen with the given name from the collection of screens
    public void unloadScreen(String name) {
        if (screens.remove(name) == null) {
            System.out.println("Screen didn't exist");
        }
    }

	@Override
	public void OnPatientAlarm() {
		System.out.println("alarm in controller");
		
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				Stage stage = new Stage();
				Node alarmscreen = getScreen(ApplicationDemo.AlarmID);
				Scene scene = new Scene((Parent) alarmscreen,300,300);
		        stage.setScene(scene);
		        stage.show();
			}
			
		});
		
	}
    
    
}
