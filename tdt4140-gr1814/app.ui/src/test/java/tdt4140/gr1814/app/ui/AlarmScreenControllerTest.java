package tdt4140.gr1814.app.ui;

import static org.junit.Assert.assertNotNull;
import static org.testfx.api.FxAssert.verifyThat;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

public class AlarmScreenControllerTest extends ApplicationTest {
	
	private alarmScreenController controller;
	
	private Button Dismiss_btn, showMap_btn;
	
	@BeforeClass
	public static void headless() {
		if (Boolean.valueOf(System.getProperty("gitlab-ci", "false"))) {
			GitlabCISupport.headless();
		}
	}
	
	@Before
	public void setUp() throws Exception {
		/* Retrieving the tested widgets from the GUI, using fx:id */
		Dismiss_btn = find("#Dismiss_btn");
		showMap_btn = find("#showMap_btn");
	}
	
	@Override
	public void start(Stage stage) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("alarmScreen.fxml"));
		Parent root = loader.load();
		this.controller = loader.getController();
		Scene scene = new Scene(root);
		
		stage.setTitle("AlarmScreenControllerTest");
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
		assertNotNull(errMsg, Dismiss_btn);
		assertNotNull(errMsg, showMap_btn);
	}
	
	@Test
	public void testDismiss_btn() {
		verifyThat(Dismiss_btn, NodeMatchers.hasText("Dismiss"));
		
	}
	
	@Test
	public void testShowMap_btn() {
		verifyThat(showMap_btn, NodeMatchers.hasText("Show in map"));
	}
}
