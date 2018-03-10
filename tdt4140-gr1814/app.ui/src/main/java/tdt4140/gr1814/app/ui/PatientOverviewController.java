package tdt4140.gr1814.app.ui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import tdt4140.gr1814.app.core.Patient;

public class PatientOverviewController implements Initializable, ControlledScreen{
	
	private ScreensController myController;
	
	private Patient currentPatientProfile;
	
	@FXML
	Button menu_btn;
	@FXML
	TextField search_txt;
	@FXML
	Text search_error;
	@FXML
	ListView<Patient> patient_list;
	@FXML
	AnchorPane patient_profile;
	@FXML
	Text patientInfo_txt;



	@Override
	public void setScreenParent(ScreensController screenParent) {
		myController = screenParent;	
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		List<Patient> patientStringList = Patient.patients;
		ObservableList<Patient> patients = FXCollections.observableList(patientStringList);
		patient_list.setItems(patients);
		patient_list.setOnMouseClicked(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent event) {
	        		displayPatientProfile(patient_list.getSelectionModel().getSelectedItem());
	        		currentPatientProfile = patient_list.getSelectionModel().getSelectedItem();
	        }
	    });
		patient_list.setCellFactory(lv -> new ListCell<Patient>() {
		    @Override
		    public void updateItem(Patient pat, boolean empty) {
		        super.updateItem(pat, empty);
		        if (empty) {
		            setText(null);
		        } else {
		            String text =  pat.getFullName(); // get text from item
		            setText(text);
		        }
		    }
		});
		
        }
        
	
	public void goToMenu() {
		myController.setScreen(ApplicationDemo.HomescreenID);
	}
	
	public void PatientSearch() {
		String name = search_txt.getText().toUpperCase();
		Patient patientSearched = null;
		for (Patient patient: patient_list.getItems()) {
				if (patient.getFullName().equals(name)) {
					patientSearched = patient;
				}
			}
		if (patientSearched != null) {
			displayPatientProfile(patientSearched);
			currentPatientProfile = patientSearched;
			search_txt.setText("");
			search_error.setVisible(false);
		}
		else {
			search_error.setText("No patient with name: "+name);
			search_error.setVisible(true);
			}
	}
	
	public void displayPatientProfile(Patient patient) {
		patientInfo_txt.setText(patient.toString());
		//here we will add all the needed information about the patient
		patient_profile.setVisible(true);
		search_error.setVisible(false);
	}
	
	public void displayZoneMap() {
		myController.getMapViewController().newZone(currentPatientProfile);
		myController.setScreen(ApplicationDemo.MapViewLayoutID);
	}
	
	public void closePatientProfile() {
		patient_profile.setVisible(false);
	}

}
        
