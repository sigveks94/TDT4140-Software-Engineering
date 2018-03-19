package tdt4140.gr1814.app.ui;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import javafx.fxml.FXMLLoader;

public class applicationIT {
	
	ApplicationDemo demo = new ApplicationDemo();
	
	
	
	@Before
	public void setUp() {
		demo.main(null);
	}
	
	@Test
	public void testLoginScreen() {
		assertEquals(demo.ScreensContainer.getChildren().get(0),
		demo.ScreensContainer.getScreen(demo.LoginID));
		
	}
	
	@Test
	public void loginTest() {
		FXMLLoader loader = (FXMLLoader) demo.ScreensContainer.getChildren().get(0).getUserData();
		ControlledScreen controller = loader.getController();
		
	}
	
}
