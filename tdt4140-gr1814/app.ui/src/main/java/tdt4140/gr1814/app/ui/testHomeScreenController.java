package tdt4140.gr1814.app.ui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;


public class testHomeScreenController implements Initializable{
	
	@FXML
	private Button addPatient;
	@FXML
	private Button showMap;
	@FXML
	private Button searchPatient;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		addPatient.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Stage stage = (Stage) addPatient.getScene().getWindow();
				try {
				stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("CreateNewPatient.fxml")),500,500));
				}catch (Exception e) {
					System.out.println();
				}
				}
			});
		showMap.setOnAction(new EventHandler<ActionEvent>(){
		    @Override
		    public void handle(ActionEvent event) {
		        Hyperlink_Browser.browse("https://www.google.no/maps/@63.4199203,10.4172,15z");
		    }
		});
	}

}
