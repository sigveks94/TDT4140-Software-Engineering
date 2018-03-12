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
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import tdt4140.gr1814.app.core.Database;
import tdt4140.gr1814.app.core.Patient;

public class PatientOverviewController implements Initializable, ControlledScreen{
	
	private ScreensController myController;
	private Patient currentPatientProfile;
	
	@FXML
	private Button menu_btn;
	@FXML
	private TextField search_txt;
	@FXML
	private Text search_error;
	@FXML
	private ListView<Patient> patient_list;
	@FXML
	private AnchorPane patient_profile;
	@FXML
	private Text patientInfo_txt;
	@FXML
	private Button alarm_btn;





	@Override
	public void setScreenParent(ScreensController screenParent) {
		myController = screenParent;	
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		updatePatientList();
		patient_list.setOnMouseClicked(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent event) {
	        		displayPatientProfile(patient_list.getSelectionModel().getSelectedItem());
	        		currentPatientProfile = patient_list.getSelectionModel().getSelectedItem();
	        }
	    });
        }
	
	public void updatePatientList() {
		List<Patient> patientStringList = Patient.patients;
		ObservableList<Patient> patients = FXCollections.observableList(patientStringList);
		patient_list.setItems(patients);
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
			patient_profile.setVisible(false);
			}
	}
	
	public void displayPatientProfile(Patient patient) {
		patientInfo_txt.setText(patient.toString());
		//here we will add all the needed information about the patient
		patient_profile.setVisible(true);
		search_error.setVisible(false);
	}
	
	public void displayZoneMap() {
		myController.getMapViewController().zoneView(currentPatientProfile);
		myController.setScreen(ApplicationDemo.MapViewLayoutID);
	}
	
	public void closePatientProfile() {
		patient_profile.setVisible(false);
	}
	
	public void changeAlarmSetting() {
		if (alarm_btn.getText().equals("ON")) {
			alarm_btn.setText("OFF");
			alarm_btn.setStyle("-fx-background-color: #f3f4f7; -fx-border-color: white; -fx-text-fill: red;");
		}
		else {
			alarm_btn.setText("ON");
			alarm_btn.setStyle("-fx-background-color: #f3f4f7; -fx-border-color: white; -fx-text-fill: #30c39e;");
		}
	}
	public void alarmDarken() {
		ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.1);
        alarm_btn.setEffect(colorAdjust);
	}
	public void alarmBrighten() {
		ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(0.0);
        alarm_btn.setEffect(colorAdjust);
	}
	public void delete_patient() {
		Long patientSSN = currentPatientProfile.getSSN();
		if(Patient.deletePatient(patientSSN)) { //if this patient exists in the static list in Patient.java, this will be deleted, and we will delete the person from the database as well
			updatePatientList();
			Database db = new Database();
			db.connect();
			db.update("DELETE FROM Patient WHERE SSN = "+String.valueOf(patientSSN)+";");
			System.out.println("Deleted patient with SSN: "+String.valueOf(patientSSN));
			patientInfo_txt.setText("Deleted patient with SSN: \n"+String.valueOf(patientSSN));
			try {Thread.sleep(500);} 
			catch (InterruptedException e) {e.printStackTrace();}
			patient_profile.setVisible(false);	
		}else {//if we do not find a patient with this SSN in the static list in Patient.java
			System.out.println("No person with SSN: "+String.valueOf(patientSSN));
		}
	}


}
        
