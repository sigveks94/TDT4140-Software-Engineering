package tdt4140.gr1814.app.ui;

import java.sql.SQLException;
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

    public static void main(String[] args) throws SQLException {
    	
    	//Temporary Simulation
    	Database database = new Database();
    	database.connect();
    	ArrayList<Patient> Patients =  database.retrievePatients();
    Patient p1 = Patients.get(0);
    Patient p2 = Patients.get(1);
    System.out.println(p1);
    
    	
    	Task task = new Task<Void>() {
            @Override
            public Void call() {
                while (true) {
                p1.changeLocation(new Point(p1.getID(), p1.getCurrentLocation().getLat() - 0.0002, p1.getCurrentLocation().getLongt()));
                p2.changeLocation(new Point(p2.getID(), p2.getCurrentLocation().getLat() - 0.0001, p2.getCurrentLocation().getLongt() + 0.0001));
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

