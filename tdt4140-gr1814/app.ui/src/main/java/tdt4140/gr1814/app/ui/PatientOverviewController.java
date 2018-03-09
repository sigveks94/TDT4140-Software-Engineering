package tdt4140.gr1814.app.ui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import tdt4140.gr1814.app.core.Patient;

public class PatientOverviewController implements Initializable, ControlledScreen{
	
	private ScreensController myController;
	
	@FXML
	Button menue_btn;
	@FXML
	TextField search_txt;
	@FXML
	ListView<String> patient_list;

	@Override
	public void setScreenParent(ScreensController screenParent) {
		myController = screenParent;
		
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ArrayList<String> patientStringList = Patient.getPatientNames();
		ObservableList<String> patients = FXCollections.observableArrayList(patientStringList);
		patient_list.setItems(patients);
	}
	
	public void goToMenue() {
		myController.setScreen(ApplicationDemo.HomescreenID);
	}

}
