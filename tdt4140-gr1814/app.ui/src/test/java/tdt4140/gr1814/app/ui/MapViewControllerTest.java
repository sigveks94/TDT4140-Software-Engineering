package tdt4140.gr1814.app.ui;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

public class MapViewControllerTest extends ApplicationTest{
	
	@BeforeClass
	public static void headless() {
		if (Boolean.valueOf(System.getProperty("gitlab-ci", "false"))) {
			GitlabCISupport.headless();
		}
	}
	
	@Before
	public void setUp() throws Exception {
		/* Retrieving the tested widgets from the GUI, using fx:id */
		
	}
	
	@Override
	public void start(Stage stage) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("mapView.fxml"));
		
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
}
