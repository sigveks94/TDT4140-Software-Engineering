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
        Parent root = FXMLLoader.load(getClass().getResource("alarmScreen.fxml"));
        Scene scene = new Scene(root,500,500);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws SQLException {
        launch(args); 
    }  
}

