package tdt4140.gr1814.app.ui;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import tdt4140.gr1814.app.core.Caretaker;
import tdt4140.gr1814.app.core.Database;
import tdt4140.gr1814.app.core.InputController;
import tdt4140.gr1814.app.core.Patient;

public class LoginScreenController implements Initializable, ControlledScreen{

	ScreensController myController;
	
	@FXML
	private AnchorPane anchorPane;
	@FXML
	private ImageView background;
    @FXML
    private TextField username;
    @FXML
    private PasswordField passwd;
    @FXML
    private ImageView sigve;
    @FXML
    private AnchorPane login_pane;
    @FXML
    private Text loginError;


    //Run
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    		//background.fitWidthProperty().bind(anchorPane.widthProperty());
    		//background.fitHeightProperty().bind(anchorPane.heightProperty());

    		showLogin();
    		passwd.setOnMouseClicked(new EventHandler<MouseEvent>(){
    			@Override
                public void handle(MouseEvent event) {
    				sigve.setVisible(true);
    				}
    			});
    		passwd.setOnKeyPressed((event) -> { if(event.getCode() == KeyCode.ENTER) { try {
				goToHome();
			} catch (InterruptedException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} } });
    }
    
    public void showLogin() {
	    	FadeTransition ft = new FadeTransition(Duration.millis(500), login_pane);
	    	ft.setFromValue(0.0);
	    	ft.setToValue(1.0);
	    	ft.setCycleCount(1);
	    	ft.setDelay(Duration.millis(700));
		ft.play();

    }
	
	@FXML
	public void goToHome() throws InterruptedException, IOException {
		if((username.getText().length() > 0) && (passwd.getText().length() > 0)) {
			Database database = new Database();
			database.connect();
			Caretaker systemUser = null;
			try {
			systemUser = database.checkPassword(username.getText(), passwd.getText());
			}catch(SQLException e){e.printStackTrace();}
			if (systemUser != null) {
				username.clear();
				passwd.clear();
				ApplicationDemo.applicationUser = systemUser;
				Patient.getPatient("id1").addListeners(systemUser); //test: adds the user as a caretaker for person with deviceID = id1
				myController.setScreen(ApplicationDemo.HomescreenID);
				InputController.metamorphise(); //tracking when logged in, this is running on a seperate thread
			}else {loginError.setVisible(true);}
		}else {loginError.setVisible(true);}
	}
	
	@Override
	public void setScreenParent(ScreensController screenPage) {
		myController = screenPage;
		
	}
	@Override
	public void showAlarm(Patient patient) {
		System.out.println("Alarm! In login scene, so did not show");
	}  
    

}

