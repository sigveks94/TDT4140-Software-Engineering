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
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class createNewPatientControllerTest extends ApplicationTest {
	
	private CreateNewPatientController controller;
	
	/* The widgets of the GUI used for the test */
	private TextField patient_name, patient_surname, patient_SSN, 
			NoK_phone, NoK_email, deviceID;
	private CheckBox genderM, genderF, accept_checkbox;
	private Hyperlink TermsOfUse;
	private Button cancel_button, add_button;
	private Text surnameError, firstnameError, ssnError, cellphoneError,
			emailError;
	
	@BeforeClass
	public static void headless() {
		if (Boolean.valueOf(System.getProperty("gitlab-ci", "false"))) {
			GitlabCISupport.headless();
		}
	}
	
	@Before
	public void setUp() throws Exception {
		/* Retrieving the tested widgets from the GUI, using fx:id */
		patient_name = find("#patient_name");
		patient_surname = find("#patient_surname");
		patient_SSN = find("#patient_SSN");
		NoK_phone = find("#NoK_phone");
		NoK_email = find("#NoK_email");
		genderM = find("#genderM");
		genderF = find("#genderF");
		accept_checkbox = find("#accept_checkbox");
		TermsOfUse = find("#TermsOfUse");
		cancel_button = find("#cancel_button");
		// add_button = find("#add_button");
		surnameError = find("#surnameError");
		firstnameError = find("#firstnameError");
		ssnError = find("#ssnError");
		cellphoneError = find("#cellphoneError");
		emailError = find("#emailError");
	}
	
	@Override
	public void start(Stage stage) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("CreateNewPatient.fxml"));
		Parent root = loader.load();
		this.controller = loader.getController();
		Scene scene = new Scene(root);
		
		stage.setTitle("CreateNewPatientControllerTest");
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
		assertNotNull(errMsg, patient_name);
		assertNotNull(errMsg, patient_surname);
		assertNotNull(errMsg, patient_SSN);
		assertNotNull(errMsg, NoK_phone);
		assertNotNull(errMsg, NoK_email);
		assertNotNull(errMsg, genderM);
		assertNotNull(errMsg, genderF);
		assertNotNull(errMsg, accept_checkbox);
		assertNotNull(errMsg, TermsOfUse);
		assertNotNull(errMsg, cancel_button);
		//assertNotNull(errMsg, add_button);
		assertNotNull(errMsg, firstnameError);
		assertNotNull(errMsg, surnameError);
		assertNotNull(errMsg, ssnError);
		assertNotNull(errMsg, cellphoneError);
		assertNotNull(errMsg, emailError);
	}
	
	@Test
	public void testPatientName() {
		patient_name.setText("Olebob");
		verifyThat("#patient_name", NodeMatchers.hasText("Olebob"));
	}
	/*
	@Test
	public void testPatientSurname() {
		patient_surname.setText("fanny");
		verifyThat("#patient_surname", NodeMatchers.hasText("fanny"));
	}
	*/
	@Test
	public void testPatientSSN() {
		patient_SSN.setText("12345678910");
		verifyThat("#patient_SSN", NodeMatchers.hasText("12345678910"));
	}
	
	@Test
	public void testNoK_Phone() {
		NoK_phone.setText("12345678");
		verifyThat("#NoK_phone", NodeMatchers.hasText("12345678"));
	}
	
	@Test
	public void testNoK_email() {
		NoK_email.setText("alibaba@email.com");
		verifyThat("#NoK_email", NodeMatchers.hasText("alibaba@email.com"));
	}
	
	@Test
	public void testGenderM_checkbox() {
		assertFalse("Gender M checkbox was checked when it should not "
				+ "have been checked.", genderM.isSelected());
		genderM.setSelected(true);
		assertTrue("Gender M checkbox was not checked when it should "
				+ "have been checked", genderM.isSelected());
	}
	
	@Test
	public void testGenderF_checkbox() {
		assertFalse("Gender F checkbox was checked when it should not "
				+ "have been checked.", genderF.isSelected());
		genderF.setSelected(true);
		assertTrue("Gender F checkbox was not checked when it should "
				+ "have been checked", genderF.isSelected());
	}
	
	@Test
	public void testAccept_checkbox() {
		assertFalse("The accept checkbox was checked when it should not"
				+ " have been checked.", accept_checkbox.isSelected());
		accept_checkbox.setSelected(true);
		assertTrue("The accept checkbox was not checked when it should"
				+ " have been checked", accept_checkbox.isSelected());
	}
	
	@Test
	public void testTermsOfuse() {
		// da fuq ??
		
	}
	
	@Test
	public void testCancel_button() {
		
	}
	
	@Test
	public void testAdd_Button() {
		
	}
	
	@Test
	public void testGoToHomeScreen() {
		
	}
	
	@Test
	public void testResetScene() {
		controller.resetScene();
		verifyThat(patient_name, NodeMatchers.hasText(""));
		verifyThat(patient_surname, NodeMatchers.hasText(""));
		verifyThat(patient_SSN, NodeMatchers.hasText(""));
		verifyThat(NoK_email, NodeMatchers.hasText(""));
		verifyThat(NoK_phone, NodeMatchers.hasText(""));
		assertFalse(genderM.isSelected());
		assertFalse(genderF.isSelected());
		assertFalse(accept_checkbox.isSelected());
	}
}
