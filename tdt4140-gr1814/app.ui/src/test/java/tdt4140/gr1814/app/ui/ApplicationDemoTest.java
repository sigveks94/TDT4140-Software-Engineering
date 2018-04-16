package tdt4140.gr1814.app.ui;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import tdt4140.gr1814.app.core.participants.Patient;
import tdt4140.gr1814.app.core.zones.Point;



public class ApplicationDemoTest extends ApplicationTest {
	
//	@Rule
//	public final ExpectedException exception = ExpectedException.none();
	
	private Patient patient, patient2, patient3;
	
    @BeforeClass
	public static void headless() throws Exception {
		if (Boolean.valueOf(System.getProperty("gitlab-ci", "false"))) {
			GitlabCISupport.headless();
		}
		ApplicationTest.launch(ApplicationDemo.class);
	}
    
	@Before
	public void setUp() throws Exception {
		String firstname = "Ola";
		String surname = "Nordmann";
		String surname2 = "Hansen";
		char gender = 'M';
		char gender2 = 'F';
		Long SSN = 12345678910L;
		Long SSN2 = 98765432190L;
		int phone = 92252233;
		int phone2 = 87263782;
		String email = "test@email.com";
		String email2 = "example@gmail.com";
		String dev1 = "H822";
		String dev2 = "A289";
		
		
		patient = Patient.newPatient(firstname,surname,gender,SSN,phone,email,dev1, true,true);
		patient2 = Patient.newPatient(firstname,surname2,gender2,SSN2,phone2,email2,dev2, true,true);
		patient3 = Patient.newPatient(firstname,"tulleberta",'D',88855544433L,phone2,email2,dev2, true,true);
	}
	
    @Override
    public void start(Stage stage) throws Exception {
    	stage.show();
    }
	
	@After
	public void teardown() throws TimeoutException {
		FxToolkit.cleanupStages();
		FxToolkit.hideStage();
		release(new KeyCode[] {});
		release(new MouseButton[] {});
	}
	
	@Test
	public void testLoadScreens() {
		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) {
		}
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				ApplicationDemo.loadScreens();
				ApplicationDemo.ScreensContainer.loadScreen(ApplicationDemo.LoginID, ApplicationDemo.LoginFile);
				assertNotNull(ApplicationDemo.ScreensContainer.getScreen("LoginScreen"));
				
			}
		});
	}
	
	@Test
	public void testGetters() {
		assertNotNull(ApplicationDemo.ScreensContainer.getMapViewController());
		assertNotNull(ApplicationDemo.ScreensContainer.getOverviewController());
	}
	
	@Test
	public void testOnPatientAlarm() {
		ApplicationDemo.ScreensContainer.OnPatientAlarm(null);
	}
	
	@Test
	public void testMapViewController() {
		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) {
		}
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				ApplicationDemo.ScreensContainer.loadScreen(ApplicationDemo.MapViewLayoutID, ApplicationDemo.MapViewLayoutFile);
				assertNotNull(ApplicationDemo.ScreensContainer.getScreen("MapViewLayout"));
				MapViewController mvc = ApplicationDemo.ScreensContainer.getMapViewController();
				List<Patient> patients = new ArrayList<>();
				patients.add(patient);
				patients.add(patient2);
				mvc.addAllViewables(patients);
				//mvc.onLocationChanged("H822", new Point("H822", 10, 10));
				mvc.goToHome(new ActionEvent());
				mvc.goToOverview();
				mvc.showAlarm(patient);
				//mvc.removePatientFromMap(patient);
			}
		});
	}
	
//	@Test
//	public void testZoneView() {
//		try {
//			Thread.sleep(1000L);
//		} catch (InterruptedException e) {
//		}
//		Platform.runLater(new Runnable() {
//			
//			@Override
//			public void run() {
//				ApplicationDemo.ScreensContainer.loadScreen(ApplicationDemo.MapViewLayoutID, ApplicationDemo.MapViewLayoutFile);
//				assertNotNull(ApplicationDemo.ScreensContainer.getScreen("MapViewLayout"));
//				MapViewController mvc = ApplicationDemo.ScreensContainer.getMapViewController();
//				// Need some help on testing these methods 
//				// mvc.zoneView(patient);
//				// mvc.saveZone()
//				// Both throws exception in async thread
//			}
//		});
//	}
}