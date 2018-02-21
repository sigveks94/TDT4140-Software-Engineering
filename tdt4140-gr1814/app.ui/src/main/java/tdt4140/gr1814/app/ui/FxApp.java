package tdt4140.gr1814.app.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FxApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("MapViewLayout.fxml"));
    	
    	
    	Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
