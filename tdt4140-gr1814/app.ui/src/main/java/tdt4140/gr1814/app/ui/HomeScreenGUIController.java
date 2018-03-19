package tdt4140.gr1814.app.ui;


import java.net.URL;
import java.util.ResourceBundle;

import datasaving.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import participants.Caretaker;



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
    @FXML
    private Text username_txt;
    @FXML
    private Hyperlink userAdr_txt;
    @FXML
    private AnchorPane passwordPane;
    @FXML
    private PasswordField newPassword1;
    @FXML
    private PasswordField newPassword2;
    @FXML
    private Text passwordError;
    @FXML
    private Text passwordSuccessMsg;
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
	@Override
	public void setScreenParent(ScreensController screenParent) {
		myController = screenParent;	
	}
	
    @FXML//Change screen to 'add new patient'-screen
    public void goToNewPatient(ActionEvent event) {
    		profile_pane.setVisible(false);
		myController.setScreen(ApplicationDemo.NewPatientID);
    }
    
    @FXML//Change screen to 'mapView'-screen
    public void goToMap(ActionEvent event) {
    		profile_pane.setVisible(false);
    		myController.getMapViewController().patientView();
		myController.setScreen(ApplicationDemo.MapViewLayoutID);
    }
    
    @FXML
    public void goToDeletePatient() {
    		profile_pane.setVisible(false);
    		myController.setScreen(ApplicationDemo.PatientOverviewID);
    }
    
    @FXML
    public void toggleProfile() {
    		if(profile_pane.isVisible()) {
    			profile_pane.setVisible(false);
    			passwordPane.setVisible(false);
    		}
    		else {
    			if (ApplicationDemo.applicationUser.getUsername() != null) {
    				username_txt.setText(ApplicationDemo.applicationUser.getUsername());
    				}
			if (ApplicationDemo.applicationUser.getAddress() != null) {
				userAdr_txt.setText(ApplicationDemo.applicationUser.getAddress());
				}
    			profile_pane.setVisible(true);}
    }
    
    @FXML
    public void openPassword(){
    		passwordPane.setVisible(true);
    }
    
    public void closePassword() {
    		newPassword1.clear();
    		newPassword2.clear();
    		passwordError.setVisible(false);
    		passwordSuccessMsg.setVisible(false);
    		passwordPane.setVisible(false);
    }
    @FXML
    public void changePassword(){
    		if(newPassword1.getText().equals(newPassword2.getText())) {
    			if(Caretaker.checkPassword(newPassword1.getText())) {
    				Database database = new Database();
    				database.connect();
    				database.updatePassword(ApplicationDemo.applicationUser, newPassword1.getText());
    				ApplicationDemo.applicationUser.setPassword(newPassword1.getText());
        			passwordError.setVisible(false);
        			passwordSuccessMsg.setVisible(true);
        			System.out.println("changed the password to: "+newPassword1.getText()+" verify: "+newPassword2.getText());
    			}else {
    				passwordError.setText("Password syntax error");
    				passwordError.setVisible(true);
    			}
    		}else {
    			passwordError.setText("passwords do not match");
    			passwordError.setVisible(true);
    			newPassword2.setText("");
    			}
    		
    }
    
   @FXML
   public void Logout() {
	   myController.setScreen(ApplicationDemo.LoginID);
   }

	@Override
	public void showAlarm() {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "\t\tPatient is currently outside zone.\n\t\tShow in map?", ButtonType.CLOSE, ButtonType.OK);
		alert.setTitle("");
		alert.setHeaderText("\t\t\t     ALARM!");
		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.setStyle("-fx-background-color: #f3f4f7;");
		Image image = new Image(ApplicationDemo.class.getResourceAsStream("mapWarning.png"));
		ImageView imageView = new ImageView(image);
		alert.setGraphic(imageView);
		alert.showAndWait();
		if (alert.getResult() == ButtonType.CLOSE) {alert.close();}
		if (alert.getResult() == ButtonType.OK) {goToMap(null);}
	}
	
	 //live changes in button color when mouse hover over.
    public void darkenButton(Button button) {
		ColorAdjust colorAdjust = new ColorAdjust();
		if (button.equals(user_btn)) {colorAdjust.setBrightness(-0.05);}
		else {colorAdjust.setBrightness(-0.1);}
        button.setEffect(colorAdjust);
    }
    public void brightenButton(Button button) {
		ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(0.0);
        button.setEffect(colorAdjust);
    }
    
    @FXML
    public void Pdarken() {
    		darkenButton(user_btn);
    }
    @FXML
    public void Pbrighten() {
    		brightenButton(user_btn);
    }
    
    @FXML
    public void POdarken() {
    		darkenButton(MyPatients_btn);
    }
    @FXML
    public void PObrighten() {
    		brightenButton(MyPatients_btn);
    }
    @FXML
    public void PMdarken() {
    		darkenButton(ViewMap_btn);
    }
    @FXML
    public void PMbrighten() {
    		brightenButton(ViewMap_btn);
    }
    
    @FXML
    public void NPdarken() {
    		darkenButton(newProfile_btn);
    }
    @FXML
    public void NPbrighten() {
    		brightenButton(newProfile_btn);
    }
    
    @FXML
    public void UBdarken() {
    	darkenButton(Settings_btn);
    }
    @FXML
    public void UBbrighten() {
    	brightenButton(Settings_btn);
    }
}
