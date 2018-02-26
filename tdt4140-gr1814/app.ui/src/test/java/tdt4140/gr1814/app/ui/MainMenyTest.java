package tdt4140.gr1814.app.ui;

import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MainMenyTest extends ApplicationTest {


	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("HomeScreenGUI.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
	}
	
	@Test
	public void accsesingNewProfilePage() {
		clickOn("#newProfile_btn");
		
	}
}
