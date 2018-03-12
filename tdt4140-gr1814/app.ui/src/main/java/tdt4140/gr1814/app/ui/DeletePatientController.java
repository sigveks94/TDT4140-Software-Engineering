package tdt4140.gr1814.app.ui;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.input.MouseEvent;
import tdt4140.gr1814.app.core.Database;
import tdt4140.gr1814.app.core.Patient;

public class DeletePatientController implements Initializable, ControlledScreen{
	
	private ScreensController myController;
	
	@FXML
	private Button delete_button;
	@FXML
	private Button back_button;
	@FXML
	private TextField SSN;
	@FXML
	private Text input_error;
	@FXML
	private Text ssn_error;
	@FXML
	private Text success_msg;
	
	public void initialize(URL location, ResourceBundle resources) {}
	
	//I use the update method and not the delete method because the delete method requires 
	//a patient object as input, but we only get the SSN from the GUI.
	//This method deletes a patient both from the DB and the static list in the Patient class
	public void delete_patient() throws SQLException {
		Long patientSSN = ValidateSSN();
		if (patientSSN != null) {
		if(Patient.deletePatient(patientSSN)) { //if this patient exists in the static list in Patient.java, this will be deleted, and we will delete the person from the database as well
			Database db = new Database();
			db.connect();
			db.update("DELETE FROM Patient WHERE SSN = "+String.valueOf(patientSSN)+";");
			System.out.println("Deleted patient with SSN: "+String.valueOf(patientSSN));
			SSN.setText("");
			input_error.setVisible(false);
			success_msg.setText("Success: Deleted patient with SSN "+String.valueOf(patientSSN));
			success_msg.setVisible(true);
		}else {//if we do not find a patient with this SSN in the static list in Patient.java
			System.out.println("No person with SSN: "+String.valueOf(patientSSN));
			input_error.setVisible(true);
		}
		}
	}
	
	//Validation of SSN. should be an 11-digit number.
	private Long ValidateSSN() {
		try{
			Long patientSSN = Long.parseLong(SSN.getText());
			if(String.valueOf(patientSSN).length() != 11) {throw new NumberFormatException();}
			ssn_error.setVisible(false);
			SSN.setStyle(null);
			return patientSSN;
		}catch(NumberFormatException e){
			//e.printStackTrace();
			ssn_error.setVisible(true);
			SSN.setStyle("-fx-border-color: red ; -fx-border-width: 1px ;"); //direct feedback in UI (red border)
			return null;
	}}
	
	
	@Override
	public void setScreenParent(ScreensController screenParent) {
		myController = screenParent;

	}
	
	public void goToHome() {
		myController.setScreen(ApplicationDemo.HomescreenID);
		SSN.setText("");
	}
	
		
	}
