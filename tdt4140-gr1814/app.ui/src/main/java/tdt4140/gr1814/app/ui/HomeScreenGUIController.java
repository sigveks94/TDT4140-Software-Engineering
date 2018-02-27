package tdt4140.gr1814.app.ui;


import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;


public class HomeScreenGUIController implements Initializable, ControlledScreen {
	
	ScreensController myController;
	
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
		
		MyPatients_btn.setDisable(true);
		Settings_btn.setDisable(true);
	}
	
	@Override
	public void setScreenParent(ScreensController screenParent) {
		myController = screenParent;
		
	}
    @FXML
    public void goToNewPatient(ActionEvent event) {
		myController.setScreen(ApplicationDemo.NewPatientID);
    }
    @FXML
    public void goToMap(ActionEvent event) {
		myController.setScreen(ApplicationDemo.MapViewLayoutID);
    }

}
