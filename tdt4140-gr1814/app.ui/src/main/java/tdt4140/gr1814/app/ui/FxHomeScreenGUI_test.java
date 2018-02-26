package tdt4140.gr1814.app.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FxHomeScreenGUI_test extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		Scene home = new Scene(FXMLLoader.load(getClass().getResource("HomeScreenGUI.fxml")));
		stage.setTitle("Homescreen");
		stage.setScene(home);
		stage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
