package tdt4140.gr1814.app.ui;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

public class HomeScreenGUIControllerTest extends ApplicationTest {
	
	private HomeScreenGUIController controller;
	
	private Button newProfile_btn, MyPatients_btn, ViewMap_btn,
			Settings_btn, user_btn;
	private ColorPicker picker;
	
	@BeforeClass
	public static void headless() {
		if (Boolean.valueOf(System.getProperty("gitlab-ci", "false"))) {
			GitlabCISupport.headless();
		}
	}
	
	@Before
	public void setUp() throws Exception {
		/* Retrieving the tested widgets from the GUI, using fx:id */
		newProfile_btn = find("#newProfile_btn");
		MyPatients_btn = find("#MyPatients_btn");
		ViewMap_btn = find("#ViewMap_btn");
		picker = find("#picker");
		// This button has no function yet
		// Settings_btn = find("#Settings_btn");
	}
	
	@Override
	public void start(Stage stage) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().
				getResource("HomeScreenGUI.fxml"));
		Parent root = loader.load();
		this.controller = loader.getController();
		Scene scene = new Scene(root);
		
		stage.setTitle("HomeScreenGUIControllerTest");
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
	
	@Test
	public void testWidgetsExists() {
		final String errMsg = "One of the widgets could not be retrieved";
		assertNotNull(errMsg, newProfile_btn);
		assertNotNull(errMsg, MyPatients_btn);
		assertNotNull(errMsg, ViewMap_btn);
	}
}
