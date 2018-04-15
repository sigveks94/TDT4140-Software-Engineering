package tdt4140.gr1814.app.ui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.concurrent.Task;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import tdt4140.gr1814.app.core.InputController;
import tdt4140.gr1814.app.core.datasaving.DataFetchController;
import tdt4140.gr1814.app.core.participants.Caretaker;
import tdt4140.gr1814.app.core.participants.Patient;
import tdt4140.gr1814.app.core.tcp.TCPClient;

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
    		showLogin();
    		passwd.setOnKeyPressed(new EventHandler<KeyEvent>(){
    	        @Override
    	        public void handle(KeyEvent ke){
    	            if (ke.getCode().equals(KeyCode.ENTER)){
    	                try { goToHome();} 
    	                catch (InterruptedException e) { e.printStackTrace();}
    	            }
    	        }
    	    });
    }
    
    public void showLogin() {
	    	FadeTransition ft = new FadeTransition(Duration.millis(500), login_pane);
	    	ft.setFromValue(0.0);
	    	ft.setToValue(1.0);
	    	ft.setCycleCount(1);
	    	ft.setDelay(Duration.millis(1000));
		ft.play();

    }
	
	@FXML
	public void goToHome() throws InterruptedException {
		if((username.getText().length() > 0) && (passwd.getText().length() > 0)) {
			Caretaker systemUser = null;
			DataFetchController datafetcher = new DataFetchController();
			systemUser = datafetcher.logIn(username.getText(), passwd.getText());
			System.out.println(systemUser);//---
			if (systemUser != null) {
				username.clear();
				passwd.clear();
				ApplicationDemo.applicationUser = systemUser;
				datafetcher.fetchPatients(systemUser);
				datafetcher.getPatientsZones(systemUser);
				ApplicationDemo.loadScreens();
			    try {
			    	Task<Void> task = new Task<Void>() {

						@Override
						protected Void call() throws Exception {
							TCPClient client = new TCPClient();
						    client.initiate();
						    
						    return null;
						}
			    	};
			    	
			    	Thread simuThread = new Thread(task);
			    	simuThread.setDaemon(true);
			    	simuThread.start();
			    	
				}catch (Exception e) {e.printStackTrace();} 
			    
			}else {loginError.setVisible(true);}
		}else {loginError.setVisible(true);}
	}
	
	@Override
	public void setScreenParent(ScreensController screenPage) {
		myController = screenPage;
		
	}
	@Override
	public void showAlarm(Patient patient) {
		System.out.println("Error: Alarm went off in Login Screen. This should not happen when not yet logged in");
		
	}
    

}
