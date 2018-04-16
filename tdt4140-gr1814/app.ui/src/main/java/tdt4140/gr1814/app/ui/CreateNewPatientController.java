
package tdt4140.gr1814.app.ui;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import tdt4140.gr1814.app.core.Hyperlink_Browser;
import tdt4140.gr1814.app.core.datasaving.DataFetchController;
import tdt4140.gr1814.app.core.participants.Patient;


// This is a simple controller for the 'CreateNewPatient.fxml' UI, validating and creating a Patient-object. 
public class CreateNewPatientController implements Initializable, ControlledScreen{
	
	private ScreensController myController;
	
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
    @FXML
    private TextField deviceID;
    

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//Making shure you can only check one gender checkbox (this does not work when using 'space' to check the boxes)
		genderM.setOnMouseClicked(new EventHandler<MouseEvent>(){
			@Override
            public void handle(MouseEvent event) {
				genderF.setSelected(false);
				}
			});
		genderF.setOnMouseClicked(new EventHandler<MouseEvent>(){
			@Override
            public void handle(MouseEvent event) {
				genderM.setSelected(false);
				}
			});
	}
	
	
	@FXML
	public void Create_Patient_Profile(){
		boolean termsaccepted = this.CheckTermsofUse();
		//Validating input:
		String firstname = this.ValidateText(patient_name, firstnameError);
		String surname = this.ValidateText(patient_surname, surnameError);
		char gender = this.checkGender();
		Long SSN = this.ValidateSSN();//using Long because of small max-value of integers
		int NoK_mobile = this.ValidateMobile();
		String email = this.ValidateEmail();
		String deviceId = deviceID.getText();
		
		//If all input values are valid. Create new patient-object.
		if(firstname != null && surname != null && SSN != null && NoK_mobile != 0 && email != null && termsaccepted) {
			Patient patient = Patient.newPatient(firstname, surname, gender, SSN, NoK_mobile, email, deviceId, true, true);
			patient.addAlarmListener(myController); //makes the Screencontroller a listener to recieve alarm-screen when outside zone. 
			myController.getMapViewController().addViewables(patient); //addind new patient to map-tracking
			myController.getMapViewController().addViewablesPolygon(patient);//adding patients zone to map
			myController.getOverviewController().updatePatientList();//updating patient overview list
			//Saving patient to database. (should check if this works before adding to static list (Patient.patients) )
			DataFetchController dataInsert = new DataFetchController();
			dataInsert.insertNewPatient(patient);
			dataInsert.caretakerForPatient(ApplicationDemo.applicationUser, patient);
			System.out.println(patient);
			//Adding and saving patient completed. change scene to homescreen:
			this.goToHomescreen(null);
		}
	}
	
	// VALIDATION OF ALL INPUT-FIELDS (considering moving the validation process to setters inside the patient-class)
	
	//Validate that textFields contains only letters
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
		if(genderM.isSelected()){return 'M';}
		else if(genderF.isSelected()){return 'F';} 
		else {return 'U';}//uncertain
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
	@FXML
	public void browseTerms() {
		Hyperlink_Browser.browse("https://www.datatilsynet.no/rettigheter-og-plikter/overvaking-og-sporing/lokalisering/");
	}

	@Override
	public void setScreenParent(ScreensController screenParent) {
		myController = screenParent;
		
	}
	//Change screen to homescreen, and clear all input-fields
	@FXML
	public void goToHomescreen(ActionEvent event) {
		this.resetScene();
		myController.setScreen(ApplicationDemo.HomescreenID);
	}
	
	public void resetScene() {
		patient_SSN.setText("");
		NoK_phone.setText("");
		NoK_email.setText("");
		patient_name.setText("");
		patient_surname.setText("");	
		deviceID.setText("");
		accept_checkbox.setSelected(false);
		genderM.setSelected(false);
		genderF.setSelected(false);
	}
	

	@Override
	public void showAlarm(Patient patient) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "\t\tPatient is currently outside zone.\n\t\tShow in map?", ButtonType.CLOSE,ButtonType.OK);
		alert.setTitle("");
		alert.setHeaderText("\t\t\t     ALARM!");
		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.setStyle("-fx-background-color: #f3f4f7;");
		Image image = new Image(ApplicationDemo.class.getResourceAsStream("pictures/mapWarning.png"));
		ImageView imageView = new ImageView(image);
		alert.setGraphic(imageView);
		alert.showAndWait();
		if (alert.getResult() == ButtonType.CLOSE) {alert.close();}
		if (alert.getResult() == ButtonType.OK) {		
			this.resetScene();
			myController.getMapViewController().patientView();
			myController.setScreen(ApplicationDemo.MapViewLayoutID);
			}
		
	}
	
	
}


