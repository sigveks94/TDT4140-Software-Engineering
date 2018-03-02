package tdt4140.gr1814.app.ui;


import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import tdt4140.gr1814.app.core.Patient;


public class HomeScreenGUIController implements Initializable, ControlledScreen {
	
	private ScreensController myController;
	
    @FXML
    private Button newProfile_btn;
    @FXML
    private Button MyPatients_btn;
    @FXML
    private Button ViewMap_btn;
    @FXML
    private Button Settings_btn;
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		MyPatients_btn.setDisable(true);//not yet created UI for MyPatients_btn
		Settings_btn.setDisable(true);//not yet created UI for Settings_btn
	}
	
	@Override
	public void setScreenParent(ScreensController screenParent) {
		myController = screenParent;	
	}
	
    @FXML//Change screen to 'add new patient'-screen
    public void goToNewPatient(ActionEvent event) {
		myController.setScreen(ApplicationDemo.NewPatientID);
    }
    
    @FXML//Change screen to 'mapView'-screen
    public void goToMap(ActionEvent event) {
		myController.setScreen(ApplicationDemo.MapViewLayoutID);
    }

}
