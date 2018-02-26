package tdt4140.gr1814.app.ui;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tdt4140.gr1814.app.core.Database;
import tdt4140.gr1814.app.core.Patient;
import tdt4140.gr1814.app.core.Point;

public class FxApp extends Application {
	
	@Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("HomeScreenGUI.fxml"));
        Scene scene = new Scene(root,500,500);
        stage.setScene(scene);
        stage.show();      
    }

    public static void main(String[] args) {
    	
    	//Temporary Simulation
    	Database database = new Database();
    	database.connect();
    	//ArrayList<Patient> Patients =  database.retrieve("SELECT * FROM Patient");
 	Patient sigg = Patient.newPatient("Sigve", "snerkerud", 'M', 12l, 47288883, "sigg@russia.ru");
    	Patient osc = Patient.newPatient("Oscar", "Vik", 'M', 31l, 49494949, "osc@hot.ru");
    	
    	Task task = new Task<Void>() {
            @Override
            public Void call() {
                while (true) {
                	//
                	sigg.changeLocation(new Point(sigg.getID(), sigg.getCurrentLocation().getLat() - 0.0002, sigg.getCurrentLocation().getLongt()));
                	osc.changeLocation(new Point(osc.getID(), osc.getCurrentLocation().getLat() - 0.0001, osc.getCurrentLocation().getLongt() + 0.0001));
                    try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }
            }
        };
        
        Thread simu_thread = new Thread(task);
        simu_thread.setDaemon(true);
        simu_thread.start();
        
        // ------------------------------------- ------------------------------------ -------------------------
        launch(args);
    }   
}

