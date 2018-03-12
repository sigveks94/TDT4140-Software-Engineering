package tdt4140.gr1814.app.ui;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class alarmScreenController implements Initializable, ControlledScreen{
	
	private ScreensController myController;

	@FXML
	private Button Dismiss_btn;
	@FXML
	private Button showMap_btn;


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	
	public void dismiss() {
		Stage stage = (Stage) Dismiss_btn.getScene().getWindow();
		stage.close();
	}
	
	public void goToMap() {
		myController.getMapViewController().patientView();
		myController.setScreen(ApplicationDemo.MapViewLayoutID);
		Stage stage = (Stage) Dismiss_btn.getScene().getWindow();
		stage.close();
	}
	
	@Override
	public void setScreenParent(ScreensController screenParent) {
		myController = screenParent;	
}

}
