package tdt4140.gr1814.webserver.servlet;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tdt4140.gr1814.webserver.Authenticator;
import tdt4140.gr1814.webserver.DatabaseHandler;

/*
 * 	This is the HttpServlet supposed to handle database queries concerning the patient objects
 */

/*
 * ================== RESPONSE CODES ===================
 * 			200 - OK
 * 			400 - Bad Arguments
 * 			500 - Internal Database Error, not users fault
 */

public class PatientServlet extends HttpServlet{

	//Serial Version, if the servlet ever changes something that will have inpact on the http request,
	//the serial version should also be updated
	private static final long serialVersionUID = 1L;
	
	/*
	 * GET requests can handle following inputs and outputs:
	 * - caretaker_id: returns all the patients associated with this care taker
	 */
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		

		String hash = req.getParameter("hash");
		Long timestamp = Long.parseLong(req.getParameter("timestamp"));
		if(hash == null || timestamp == null) {
			resp.setStatus(401);
			return;
		}
		
		if(!Authenticator.verifyHash(timestamp, hash)) {
			resp.setStatus(401);
			return;
		}
		
		String caretakerId = req.getParameter("caretaker_id");
		if(caretakerId == null) {
			resp.setStatus(400);
			return;
		}
		
		resp.getWriter().print(this.getPatients(caretakerId, resp));
		
	}

	/*
	 *	POST Request can handle the follow:
	 *	Insering a patient expects: firstname, surname, ssn, phonenumber, email, gender, deviceID
	 * 	Deleting a patient expects: ssn and delete = yes
	 * 	Binding a patient and a caretaker expects caretaker_id and ssn
	 * 	Set alarm activation expects: activate = yes and ssn
	 */
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {	
		

		String hash = req.getParameter("hash");
		Long timestamp = Long.parseLong(req.getParameter("timestamp"));
		if(hash == null || timestamp == null) {
			resp.setStatus(401);
			return;
		}
		
		if(!Authenticator.verifyHash(timestamp, hash)) {
			resp.setStatus(401);
			return;
		}
		
		//Handles deletation 
		if(req.getParameter("delete") != null && req.getParameter("delete").contentEquals("yes")) {
			if(req.getParameter("ssn") == null || req.getParameter("ssn").length() != 11) {
				resp.setStatus(400);
				return;
			}
			this.deletePatient(req.getParameter("ssn"), resp);
			return;
		}
		
		//Handles request for binding caretaker and patients together
		if(req.getParameter("caretaker_id") != null && req.getParameter("ssn") != null) {
			this.bindPatientCaretaker(req.getParameter("ssn"), req.getParameter("caretaker_id"), resp);
			return;
		}
		
		//Handles request for alarm activation set
		if(req.getParameter("activate") != null && req.getParameter("ssn")!= null){
			this.setAlarmActivation(req.getParameter("ssn"), Boolean.parseBoolean(req.getParameter("activate")), resp);
			return;
		}
		
		String firstName = req.getParameter("firstname");
		String surname = req.getParameter("surname");
		String SSN = req.getParameter("ssn");
		String phoneNumber = req.getParameter("phone");
		String email = req.getParameter("email");
		String gender = req.getParameter("gender");
		String deviceID = req.getParameter("id");
		
		if(firstName == null || surname == null || SSN == null || phoneNumber == null || email == null || gender == null || deviceID == null) {
			resp.setStatus(400);
			return;
		}
		
		this.insertPatient(firstName, surname, SSN, phoneNumber, email, gender, deviceID, resp);

	}
	
	
	//Removes a patient from the database
	private void deletePatient(String ssn, HttpServletResponse resp) {
		
		DatabaseHandler databaseHandler;
		try {
			databaseHandler = new DatabaseHandler();
		} catch (ClassNotFoundException | SQLException e1) {
			e1.printStackTrace();
			resp.setStatus(500);
			return;
		}
		
		String query = "DELETE FROM Patient WHERE SSN LIKE " + ssn + ";";
		
		try {
			databaseHandler.update(query);
			resp.setStatus(200);;
			return;
		} catch (SQLException e) {
			e.printStackTrace();
			resp.setStatus(500);
			return;
		}
	}
	
	//Binds a patient to a caretaker
	private void bindPatientCaretaker(String ssn, String caretakerId, HttpServletResponse resp) {
		DatabaseHandler databaseHandler;
		try {
			databaseHandler = new DatabaseHandler();
		} catch (ClassNotFoundException | SQLException e1) {
			e1.printStackTrace();
			resp.setStatus(500);
			return;
		}
		
		
		String query = "INSERT INTO PatientCaretaker (PatSSN, CaretakerUsername) VALUES (" + ssn + ", \"" +  caretakerId + "\");";
		try {
			databaseHandler.update(query);
			resp.setStatus(200);
			return;
		} catch (SQLException e) {
			e.printStackTrace();
			resp.setStatus(500);
			return;
		}
	}
	
	private void setAlarmActivation(String ssn, boolean activate, HttpServletResponse resp) {
		
		DatabaseHandler databaseHandler;
		try {
			databaseHandler = new DatabaseHandler();
		} catch (ClassNotFoundException | SQLException e1) {
			e1.printStackTrace();
			resp.setStatus(500);
			return;
		}
		
		if(ssn.length() != 11) {
			resp.setStatus(400);
			return;
		}
		
		int active = 0;
		if(activate) {
			active = 1;
		}
		String query = "UPDATE Patient SET alarmActivated = '" + active + "' WHERE SSN = " + ssn + ";";
		try {
			databaseHandler.update(query);
			resp.setStatus(200);
			return;
		} catch (SQLException e) {
			e.printStackTrace();
			resp.setStatus(500);
			return;
		}
	}
	
	private void insertPatient(String firstName, String surName, String ssn, String phoneNumber, String email, String gender, String deviceId, HttpServletResponse resp) {
		
		DatabaseHandler databaseHandler;
		
		try {
			databaseHandler = new DatabaseHandler();
		} catch (ClassNotFoundException | SQLException e1) {
			e1.printStackTrace();
			resp.setStatus(500);
			return;
		}
		
		try {
			Long.parseLong(ssn);
			Integer.parseInt(phoneNumber);
			if(ssn.length() != 11) {
				resp.setStatus(400);
				return;
			}
		}
		catch(NumberFormatException numberException) {
			numberException.printStackTrace();
			resp.setStatus(400);
			return;
		}
		
		try {
			databaseHandler.update("INSERT INTO Patient(SSN, FirstName, LastName, Gender, PhoneNumber, Email, DeviceID, alarmActivated) "
					+ "VALUES ('"+ssn+"','"+firstName+"','"+surName+"','"+gender+"',"+phoneNumber+",'"+email+"', '"+deviceId+"','1')");
		} catch (SQLException e) {
			e.printStackTrace();
			resp.setStatus(500);
			return;
		}
		resp.setStatus(200);
	}
	
	private String getPatients(String caretakerUsername, HttpServletResponse resp ){
		
		DatabaseHandler databaseHandler; //Database connection handler
		
		try {
			databaseHandler = new DatabaseHandler();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			resp.setStatus(500);
			return null;
		}
		
		//The query to be handled by the database handler
		String queryString = "SELECT Patient.* FROM PatientCaretaker "
				+ "JOIN Patient ON PatientCaretaker.PatSSN=Patient.SSN WHERE PatientCaretaker.CaretakerUsername='" + caretakerUsername + "'";
		
		ResultSet result; //Where to store the dataset returned by the query
		
		try {
			result = databaseHandler.query(queryString);
		} catch (SQLException e) {
			e.printStackTrace();
			resp.setStatus(500);
			return null;
		}
		
		//Builds the json string to be returned
		String json = "[";
		try {
			while(result.next()) {
				json += "{\"FirstName\":\"" + result.getString("FirstName") + "\", \"Surname\":\"" + result.getString("LastName") + "\",\"Gender\":\"" + result.getString("Gender") + "\","
						+ "\"SSN\":\"" + result.getString("SSN") + "\", \"NoK_cellphone\":\"" + result.getString("PhoneNumber") + "\", \"NoK_email\":\"" + 
						result.getString("Email") + "\", \"DeviceID\":\"" + result.getString("DeviceID") + "\", \"alarmActivated\":\"" + result.getString("alarmActivated") + "\"},";
			}
		} catch (SQLException e) {
			e.printStackTrace();
			resp.setStatus(500);
			return null;
		}
		if(json.length() > 1) //If the list is empty there will be no comma to remove either
			json = json.substring(0, json.length() -1); //The last character will be a comma that is not supposed to be there
		return json += "]";
		
	}
	
}


