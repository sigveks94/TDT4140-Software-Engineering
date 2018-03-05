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
	
	public void initialize(URL location, ResourceBundle resources) {
		delete_button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					delete_patient();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}});
        }
	
	//I use the update method and not the delete method because the delete method requires 
	//a patient object as input, but we only get the SSN from the GUI.
	//This method deletes a patient both from the DB and the static list in the Patient class
	public void delete_patient() throws SQLException {
		String patientSSN = SSN.getText();
		Database db = new Database();
		db.connect();
		db.update("DELETE FROM Patient WHERE SSN = "+patientSSN+";");
		Patient.deletePatient(Long.parseLong(patientSSN));
		System.out.println("Deleted person with SSN: "+patientSSN);
		SSN.setText("");
	}
	
	

	@Override
	public void setScreenParent(ScreensController screenParent) {
		myController = screenParent;

	}
	
	public void goToHome() {
		myController.setScreen(ApplicationDemo.HomescreenID);
	}
	
		
	}
