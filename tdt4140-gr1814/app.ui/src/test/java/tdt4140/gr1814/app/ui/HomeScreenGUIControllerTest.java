package tdt4140.gr1814.app.ui;

import java.util.concurrent.TimeoutException;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

public class HomeScreenGUIControllerTest extends ApplicationTest {
	
	//private HomeScreenGUIController controller;
	
	private Button newProfile_btn, MyPatients_btn, ViewMap_btn,
			Settings_btn;
	
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
		Settings_btn = find("#Settings_btn");
	}
	/*
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
	*/
	@After
	public void teardown() throws TimeoutException {
		FxToolkit.hideStage();
		release(new KeyCode[] {});
		release(new MouseButton[] {});
	}
	
	public <T extends Node> T find(final String query) {
		return lookup(query).query();
	}
	/*
	@Test
	public void newProfile_btnTest() {
		clickOn(newProfile_btn);
	}
	
	@Test
	public void MyPatients_btnTest() {
		clickOn(MyPatients_btn);
	}
	
	@Test
	public void ViewMap_btnTest() {
		clickOn(ViewMap_btn);
	}
	
	@Test 
	public void Settings_btnTest() {
		//clickOn(Settings_btn);
	}
	*/
	
}
