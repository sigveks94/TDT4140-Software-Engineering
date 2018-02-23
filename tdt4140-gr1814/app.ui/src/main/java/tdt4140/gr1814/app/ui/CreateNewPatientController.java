package tdt4140.gr1814.app.ui;

import tdt4140.gr1814.app.core.Patient;

import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;


import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;


// This is a simple controller for the 'CreateNewPatient.fxml' UI, validating and creating a Patient-object. 
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
    @FXML
    private Text surnameError;
    @FXML
    private Text firstnameError;
    @FXML
    private Text ssnError;
    @FXML
    private Text cellphoneError;
    @FXML
    private Text emailError;
    

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//manage 'terms of use'-hyperlink request
		TermsOfUse.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		        Hyperlink_Browser.browse("https://www.datatilsynet.no/rettigheter-og-plikter/overvaking-og-sporing/lokalisering/");
		    }
		});
		//Try to create new profile when add-button is pressed
		add_button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) { 
            		Create_Patient_Profile();
            	}
        });
		//Cancel scene/ close UI. 
		cancel_button.setOnMouseClicked(new EventHandler<MouseEvent>(){
			@Override
            public void handle(MouseEvent event) {
			    Stage stage = (Stage) cancel_button.getScene().getWindow();
			    stage.close();//New profile canceled.
			}
		});
	}
	
	
	
	private void Create_Patient_Profile(){
		boolean termsaccepted = this.CheckTermsofUse();
		//Validating input:
		String firstname = this.ValidateText(patient_name, firstnameError);
		String surname = this.ValidateText(patient_surname, surnameError);
		char gender = this.checkGender();
		Long SSN = this.ValidateSSN();//using Long because of small max-value of integers
		int NoK_mobile = this.ValidateMobile();
		String email = this.ValidateEmail();
		
		//If all input values are valid. Create new patient-Object.
		if(firstname != null && surname != null && SSN != null && NoK_mobile != 0 && email != null && termsaccepted) {
			Patient patient = new Patient(firstname, surname, gender, SSN, NoK_mobile, email);//may be removed. Patient info saved directly to database. 
			System.out.println(patient);
			//Patient-object has to be saved to database somehow here..
			//Adding patient completed. close the UI:
			Stage stage = (Stage) patient_name.getScene().getWindow();
		    stage.close();;	
		}
	}
	
	// VALIDATION OF ALL INPUT-FIELDS (considering moving the validation process to setters inside the patient-class)
	
	//Validate that textFields ony contain letters
	private String ValidateText(TextField inputField, Text inputError) {
		String text = inputField.getText();
		if (text.matches("[a-zA-Z]+")){
			inputField.setStyle(null);
			inputError.setVisible(false);//error message hidden
			return text.toUpperCase();}
		else {
			inputError.setVisible(true);
			inputField.setStyle("-fx-border-color: red ; -fx-border-width: 1px ;"); //direct feedback in UI (red border)
			return null;
		}
	}
	
	//check gender. I have not yet implemented only allowing the user to check off one of the 'male'/'female' boxes
	private char checkGender() {
		if(genderM.isSelected() && !genderF.isSelected()) {return 'M';}
		else if(genderF.isSelected() && !genderM.isSelected()) {return 'F';}
		else {return 'U';}//Uncertain
	}
	
	//validation of social security number. Required length = 11. Able to convert to Long-type
	private Long ValidateSSN() {
		try{
			Long SSN = Long.parseLong(patient_SSN.getText());
			if(String.valueOf(SSN).length() != 11) {throw new NumberFormatException();}
			ssnError.setVisible(false);
			patient_SSN.setStyle(null);
			return SSN;
		}catch(NumberFormatException e){
			//e.printStackTrace();
			ssnError.setVisible(true);
			patient_SSN.setStyle("-fx-border-color: red ; -fx-border-width: 1px ;"); //direct feedback in UI (red border)
			return null;
	}}
	
	//validation of next of kin mobile number. Required length = 8. Able to convert to integer
	private int ValidateMobile() {
		try {
			int mobile = Integer.parseInt(NoK_phone.getText());
			if(String.valueOf(mobile).length() != 8) {throw new NumberFormatException();}
			cellphoneError.setVisible(false);
			NoK_phone.setStyle(null);
			return mobile;	
		}catch (NumberFormatException e) {
			//e.printStackTrace();
			cellphoneError.setVisible(true);
			NoK_phone.setStyle("-fx-border-color: red ; -fx-border-width: 1px ;");//direct feedback in UI (red border)
			return 0;
	}}
	
	//validate email using regex
	private String ValidateEmail() {
		String email = NoK_email.getText();
		Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
		Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(email);
        if (matcher.find()) {
        		emailError.setVisible(false);
        		NoK_email.setStyle(null);
        		return email;
        	}else {	
        		emailError.setVisible(true);
        		NoK_email.setStyle("-fx-border-color: red ; -fx-border-width: 1px ;");//direct feedback in UI (red border)
        		return null;
    }}
	
	//Check if 'terms of used'-box is checked
	private boolean CheckTermsofUse() {
		if (accept_checkbox.isSelected()) {
    			accept_checkbox.setStyle(null);
    			return true;
		}else {
    			accept_checkbox.setStyle("-fx-border-color: red ; -fx-border-width: 1px ;");
    			return false;
	}}
	
	
	
}
	


