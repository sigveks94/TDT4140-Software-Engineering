package tdt4140.gr1814.app.ui;


import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import tdt4140.gr1814.app.core.Patient;

public class HomeScreenGUIController implements Initializable {
	
	
    @FXML
    private Button newProfile_btn;

    @FXML
    private Button MyPatients_btn;

    @FXML
    private Button ViewMap_btn;

    @FXML
    private Button Settings_btn;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		newProfile_btn.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				Stage stage = (Stage) newProfile_btn.getScene().getWindow();
				try {
					stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("CreateNewPatient.fxml"))));
				} catch(Exception e) {
					System.out.println("Could not access new profile fxml.");
				}
				
			}
			
		});
		
		MyPatients_btn.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				Stage stage = (Stage) MyPatients_btn.getScene().getWindow();
				try {
					stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("CreateNewPatient.fxml"))));
				} catch(Exception e) {
					System.out.println("Could not access your patients fxml.");
				}
				
			}
			
		});
		
		ViewMap_btn.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				Stage stage = (Stage) ViewMap_btn.getScene().getWindow();
				FXMLLoader loader = new FXMLLoader(getClass().getResource("MapViewLayout.fxml"));
				
				MapViewController controller = new MapViewController();
				controller.addAllViewables(Patient.getAllPatients());
				
				loader.setController(controller);
				
				try {
					stage.setScene(new Scene(loader.load()));
				} catch(Exception e) {
					System.out.println("Could not access map fxml.");
				}
				
			}
			
		});
		
		Settings_btn.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				Stage stage = (Stage) Settings_btn.getScene().getWindow();
				try {
					stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("CreateNewPatient.fxml"))));
				} catch(Exception e) {
					System.out.println("Could not access settings fxml.");
				}
				
			}
			
		});
		
		
		
	}
    
    

}
