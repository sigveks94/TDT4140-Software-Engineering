package tdt4140.gr1814.app.ui;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.testfx.api.FxAssert.verifyThat;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tdt4140.gr1814.app.core.Caretaker;

public class HomeScreenGUIControllerTest extends ApplicationTest {
	
	//private HomeScreenGUIController controller;
	
	private Button newProfile_btn, MyPatients_btn, ViewMap_btn,
			Settings_btn, user_btn;
	private AnchorPane profile_pane;
	private Text username_txt;
	private Hyperlink userAdr_txt;
	
	private Caretaker applicationUser;
	
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
		user_btn = find("#user_btn");
		profile_pane = find("#profile_pane");
		username_txt = find("#username_txt");
		userAdr_txt = find("#userAdr_txt");
		applicationUser = new Caretaker("finn", "abcdefg", "lolman");
	}
	/*
	@Override
	public void start(Stage stage) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("HomeScreenGUI.fxml"));
		Parent root = loader.load();
		// loader.getController() returns null, why? The correct controller is assosiated with the FXML-file.
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
	public void testWidgetsExists() {
		final String errMsg = "One of the widgets could not be retrieved!";
		assertNotNull(errMsg, newProfile_btn);
		assertNotNull(errMsg, MyPatients_btn);
		assertNotNull(errMsg, ViewMap_btn);
		assertNotNull(errMsg, Settings_btn);
		assertNotNull(errMsg, user_btn);
		assertNotNull(errMsg, profile_pane);
		//assertNotNull(errMsg, username_txt);
		//assertNotNull(errMsg, userAdr_txt);
	}
	
//	@Test
//	public void testInitialize() {
//		verifyThat(username_txt, NodeMatchers.hasText(ApplicationDemo.username));
//		verifyThat(userAdr_txt, NodeMatchers.hasText(ApplicationDemo.adress));
//	}
	
	@Test
	public void gotToNewPatientTest() {
		assertFalse(profile_pane.isVisible());
		profile_pane.setVisible(true);
		assertTrue(profile_pane.isVisible());
		
	}
	
	@Test
	public void goToMapTest() {
		profile_pane.setVisible(true);
	}
	
	@Test
	public void goToDeletePatientTest() {
		profile_pane.setVisible(true);
		try {
			controller.goToDeletePatient();
		} catch (NullPointerException e) {
			
		}
		assertFalse(profile_pane.isVisible());
	}
	
	@Test
	public void toggleProfileTest() {
		profile_pane.setVisible(true);
		controller.toggleProfile();
		assertFalse(profile_pane.isVisible());
		controller.toggleProfile();
		assertTrue(profile_pane.isVisible());
		
	}
	
	@Test
	public void logoutTest() {
		
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
