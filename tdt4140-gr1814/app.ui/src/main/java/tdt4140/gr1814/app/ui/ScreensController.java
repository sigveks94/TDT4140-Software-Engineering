
package tdt4140.gr1814.app.ui;

import java.util.HashMap;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import tdt4140.gr1814.app.core.listeners.OnPatientAlarmListener;
import tdt4140.gr1814.app.core.participants.Patient;



public class ScreensController  extends StackPane implements OnPatientAlarmListener{//StackPane lays out its children in a back-to-front stack.
    
	//Holds the screens to be displayed
    private HashMap<String, Node> screens = new HashMap<>();
    private static MapViewController MapController = new MapViewController();
    private static PatientOverviewController OverviewController = new PatientOverviewController();
    // added for testing purposes
    private static HomeScreenGUIController hsc = new HomeScreenGUIController();
    
    public ScreensController() {
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
            if (name.equals("PatientOverview")) {//Different way to add the controller for the PatientOverview.fxml screen. 
        			myLoader.setController(OverviewController);//make this controller, the controller of the screen
            }         
            Parent loadScreen = (Parent) myLoader.load();
            loadScreen.setUserData(myLoader);//saves loader as user data in the Node. Makes it possible to access conroller-instance.
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
            }else {getChildren().add(screens.get(name));}//no one else been displayed, then just show       
        }else {System.out.println("screen hasn't been loaded!!!");}
    }
    
    //Getters for map- and overview-controller
    public MapViewController getMapViewController() {return MapController;}
    
    public PatientOverviewController getOverviewController() {return OverviewController;}
    
    public HomeScreenGUIController getHomeScreenGUIController() {return hsc;}
    
    //Function 'catches' alarm set of in patient class, and shows it as an alert on current screen.
	@Override
	public void OnPatientAlarm(Patient patient) { 
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				FXMLLoader loader =(FXMLLoader) getChildren().get(0).getUserData();
				ControlledScreen controller = loader.getController();
				controller.showAlarm(patient);
			}
		});
	}
    
} 
