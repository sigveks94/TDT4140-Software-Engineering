package tdt4140.gr1814.app.ui;

import tdt4140.gr1814.app.core.Patient;

import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;


import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

//enkel test av  ui for å opprette nytt pasient-objekt. ikke tatt hensyn til kontroll av korrekt input eller lignende
public class CreateNewPatientController implements Initializable{
	
	@FXML
	private TextField patient_name;
	@FXML
	private TextField patient_surname;
	@FXML
	private CheckBox genderM;
	@FXML
	private CheckBox genderF;
	@FXML
	private TextField patient_SSN;
	@FXML
	private TextField NoK_phone;
	@FXML
	private TextField NoK_email;
	@FXML
	private CheckBox accept_checkbox;
	@FXML
	private Hyperlink TermsOfUse;
	@FXML
	private Button cancel_button;
	@FXML
	private Button add_button;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		TermsOfUse.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		        Hyperlink_Browser.browse("https://www.datatilsynet.no/rettigheter-og-plikter/overvaking-og-sporing/lokalisering/");
		    }
		});
		add_button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) { 
            	if (accept_checkbox.isSelected()) {
            		accept_checkbox.setStyle(null);
            		Create_Patient_Profile();}
            	else {
            		accept_checkbox.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
            		System.out.println("Did not accept 'Terms of use'.");
            	}
            }
        });
		cancel_button.setOnMouseClicked(new EventHandler<MouseEvent>(){
			@Override
            public void handle(MouseEvent event) {
			    Stage stage = (Stage) cancel_button.getScene().getWindow();
			    stage.close();;
			    System.out.println("New profile canceled.");
			}
		});
	}
	
	
	
	public void Create_Patient_Profile(){
		String firstname = this.ValidateText(patient_name);
		String surname = this.ValidateText(patient_surname);
		//I have not yet figured out how to only allow user to check off one of the 'male'/'female' boxes
		char Gender = 'U';//Uncertain
		if(genderM.isSelected() && !genderF.isSelected()) {Gender = 'M';}
		if(genderF.isSelected() && !genderM.isSelected()) {Gender = 'F';}
		
		//Validating input:
		Long SSN = this.ValidateSSN();//using Long because of small max-value of integers
		int NoK_mobile = this.ValidateMobile();
		String email = this.ValidateEmail();
		//If all input values are valid. Create new patient-Object.
		if(firstname != null && surname != null && SSN != null && NoK_mobile != 0 && email != null) {
			Patient patient = new Patient(firstname, surname, Gender, SSN, NoK_mobile, email);
			System.out.println(patient);
			//Adding patient completed. close the UI:
			Stage stage = (Stage) patient_name.getScene().getWindow();
		    stage.close();;	
		}
	}
	
	// VALIDATION
	
	//Validate that textFields ony contain letters
	private String ValidateText(TextField inputField) {
		String text = inputField.getText();
		if (text.matches("[a-zA-Z]+")){
			inputField.setStyle(null);
			return text;}
		else {
			inputField.setStyle("-fx-border-color: red ; -fx-border-width: 1px ;"); //direct feedback in UI (red border)
			return null;
		}
	}
	
	//validation of social security number. Required length = 11. Able to convert to Long-type
	private Long ValidateSSN() {
		try{
			Long SSN = Long.parseLong(patient_SSN.getText());
			if(String.valueOf(SSN).length() != 11) {throw new NumberFormatException();}
			patient_SSN.setStyle(null);
			return SSN;
		}catch(NumberFormatException e){
			//e.printStackTrace();
			patient_SSN.setStyle("-fx-border-color: red ; -fx-border-width: 1px ;"); //direct feedback in UI (red border)
			return null;
		}}
	
	//validation of next of kin mobile number. Required length = 8. Able to convert to integer
	private int ValidateMobile() {
		try {
			int mobile = Integer.parseInt(NoK_phone.getText());
			if(String.valueOf(mobile).length() != 8) {throw new NumberFormatException();}
			NoK_phone.setStyle(null);
			return mobile;	
		}catch (NumberFormatException e) {
			//e.printStackTrace();
			NoK_phone.setStyle("-fx-border-color: red ; -fx-border-width: 1px ;");//direct feedback in UI (red border)
			return 0;
		}}
	
	//validate email using regex
	public  String ValidateEmail() {
		String email = NoK_email.getText();
		Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
		Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(email);
        if (matcher.find()) {
        		NoK_email.setStyle(null);
        		return email;
        	}
        else {	
        		NoK_email.setStyle("-fx-border-color: red ; -fx-border-width: 1px ;");//direct feedback in UI (red border)
        		return null;
        }}
	
	}
	


