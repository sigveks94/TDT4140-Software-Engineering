package tdt4140.gr1814.app.ui;


import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.AnchorPane;
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
    @FXML
    private AnchorPane profile_pane;
    @FXML
    private Button user_btn;
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
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
    		myController.getMapViewController().patientView();
		myController.setScreen(ApplicationDemo.MapViewLayoutID);
    }
    
    @FXML
    public void goToDeletePatient() {
    		myController.setScreen(ApplicationDemo.PatientOverviewID);
    }
    
    @FXML
    public void toggleProfile() {
    		if(profile_pane.isVisible()) {profile_pane.setVisible(false);}
    		else {profile_pane.setVisible(true);}
    }
    
    //live changes in button color when mouse hover over.
    
    @FXML
    public void Pdarken() {
    		ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.05);
        user_btn.setEffect(colorAdjust);
    }
    @FXML
    public void Pbrighten() {
    		ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(0.0);
        user_btn.setEffect(colorAdjust);
    }
    
    @FXML
    public void POdarken() {
		ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.1);
        MyPatients_btn.setEffect(colorAdjust);
    }
    @FXML
    public void PObrighten() {
		ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(0.0);
        MyPatients_btn.setEffect(colorAdjust);
    }
    
    @FXML
    public void PMdarken() {
		ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.1);
        ViewMap_btn.setEffect(colorAdjust);
    }
    @FXML
    public void PMbrighten() {
		ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(0.0);
        ViewMap_btn.setEffect(colorAdjust);
    }
    
    @FXML
    public void NPdarken() {
		ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.1);
        newProfile_btn.setEffect(colorAdjust);
    }
    @FXML
    public void NPbrighten() {
		ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(0.0);
        newProfile_btn.setEffect(colorAdjust);
    }
    
    @FXML
    public void UBdarken() {
		ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.1);
    		Settings_btn.setEffect(colorAdjust);
    }
    @FXML
    public void UBbrighten() {
		ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(0.0);
    		Settings_btn.setEffect(colorAdjust);
    }
}
