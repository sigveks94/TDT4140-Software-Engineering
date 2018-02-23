package tdt4140.gr1814.app.ui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
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
				// TODO Auto-generated method stub
				Stage stage = (Stage) addPatient.getScene().getWindow();
			    stage.close();
		        Parent root = FXMLLoader.load(getClass().getResource("CreateNewPatient.fxml"));
		        Scene scene = new Scene(root);
		        stage.setScene(scene);
		        stage.show();
				}
			});
		
	}

}
