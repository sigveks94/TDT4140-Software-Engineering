package tdt4140.gr1814.app.ui;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.junit.After;
import org.junit.BeforeClass;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

public class ScreensControllerTest extends ApplicationTest {
	
	private ScreensController controller;
	
	@BeforeClass
	public static void headless() {
		if (Boolean.valueOf(System.getProperty("gitlab-ci", "false"))) {
			GitlabCISupport.headless();
		}
	}
	
	@Override
	public void start(Stage stage) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("CreateNewPatient.fxml"));
		Parent root = loader.load();
		this.controller = loader.getController();
		Scene scene = new Scene(root);
		
		stage.setTitle("ScreensControllerTest");
		stage.setScene(scene);
		stage.show();
		stage.toFront();
	}
	
	@After
	public void teardown() throws TimeoutException {
		FxToolkit.hideStage();
		release(new KeyCode[] {});
		release(new MouseButton[] {});
	}
	
	public <T extends Node> T find(final String query) {
		return lookup(query).query();
	}
	
	
	// this method makes it possible to switch controllers
	public void run() {
		FXMLLoader loader =(FXMLLoader) this.controller.getChildren().get(0).getUserData();
		ControlledScreen controller = loader.getController();
		controller.showAlarm();
	}
}
