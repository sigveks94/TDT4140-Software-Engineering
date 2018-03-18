package tdt4140.gr1814.webserver;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import tdt4140.gr1814.app.core.Patient;

/*
 * 	This is the HttpServlet supposed to handle database queries concerning the patient objects
 * 	PS: This class uses the static method Patient.newPatient just like the client application. Even though the server and the client is running on the same computer in the demos, they
 * 		does not use the same static pool since they are started as separate application in the OS.
 */

public class PatientServlet extends HttpServlet{

	//Serial Version, if the servlet ever changes something that will have inpact on the http request,
	//the serial version should also be updated
	private static final long serialVersionUID = 1L;
	
	ConnectionHandler databaseConnection;
	
	//Privat method for establishing connection with the database
		private boolean establishConnection(HttpServletResponse resp) {
			databaseConnection = new ConnectionHandler();
			
			try {
				databaseConnection.connect();
				return true;
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
				resp.setStatus(500); //Internal DB error
				return false;
			} 
		}
	
	/*
	 * GET requests can handle following inputs and outputs:
	 * - caretaker_id: returns all the patients associated with this care taker
	 */
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		if(!this.establishConnection(resp)) {
			resp.setStatus(500); //Internal DB Error
			return;
		}
		
		//Fetches the response input stream in order to echo answer back to the requester
		PrintWriter echoWriter = resp.getWriter();
		
		//If a caretaker id is passed as a argument, the response should be a list of all the patients associated with this caretaker
		if(req.getParameter("caretaker_id") != null) {
			echoWriter.print(toJson(getMultiplePatients(req.getParameter("caretaker_id"))));
			echoWriter.flush();
			echoWriter.close();
			return;
		}
		else {
			resp.setStatus(400); //If none of the expected parameters are recieved the 400 Bad Request status is set
			return;
		}
		
		
	}

	/*
	 * POST Request can be called for inserting a patient object into the database
	 * This request expects the follow parameters : firstname, surname, SSN, phone number, email, gender and deviceID
	 * If any of these parameters are missing the insertion will not go through and an appropriate status code should be sent back to the requester
	 */
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {	
		
		this.establishConnection(resp);
		String firstName = req.getParameter("firstname");
		String surname = req.getParameter("surname");
		String SSN = req.getParameter("ssn");
		String phoneNumber = req.getParameter("phone");
		String email = req.getParameter("email");
		String gender = req.getParameter("gender");
		String deviceID = req.getParameter("id");
		
		if(req.getParameter("delete") != null && req.getParameter("delete").contentEquals("yes")) {
			if(req.getParameter("ssn") == null || req.getParameter("ssn").length() != 11) {
				resp.setStatus(400); //Bad Request
				return;
			}
			if(this.deletePatient(req.getParameter("ssn"))) {
				resp.setStatus(200);
				return;
			}
			else {
				resp.setStatus(500); //Internal DB Error
				return;
			}
		}
		
		if(firstName == null || surname == null || SSN == null || phoneNumber == null || email == null || gender == null || deviceID == null) {
			resp.setStatus(400); //Bad Request status
			return;
		}
		try {
			Long.parseLong(SSN);
			if(SSN.length() != 11) {
				resp.setStatus(400); //Bad Request status
				return;
			}
		}
		catch(NumberFormatException numberException) {
			numberException.printStackTrace();
			resp.setStatus(400); //Bad Request status
			return;
		}
		try {
			Integer.parseInt(phoneNumber);
		}
		catch(NumberFormatException numberException) {
			numberException.printStackTrace();
			resp.setStatus(400); //Bad Request Status
			return;
		}
		
		try {
			databaseConnection.update("INSERT INTO Patient(SSN, FirstName, LastName, Gender, PhoneNumber, Email, DeviceID, alarmActivated) "
					+ "VALUES ('"+SSN+"','"+firstName+"','"+surname+"','"+gender+"',"+phoneNumber+",'"+email+"', '"+deviceID+"','1')");
		} catch (SQLException e) {
			e.printStackTrace();
			resp.setStatus(500); //Internal DB Error
			return;
		}
		resp.setStatus(200); //OK

	}
	
	
	//Removes a patient from the database
	private boolean deletePatient(String ssn) {
		String query = "DELETE FROM Patient WHERE SSN LIKE " + ssn + ";";
		try {
			this.databaseConnection.update(query);
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	//Returns an array of all patients associated with the given caretaker
	private ArrayList<Patient> getMultiplePatients(String caretakerUsername) {
		
		//Query for retrieving all patients associated with given caretaker
		String queryString = "SELECT Patient.* FROM PatientCaretaker "
				+ "JOIN Patient ON PatientCaretaker.PatSSN=Patient.SSN WHERE PatientCaretaker.CaretakerUsername='" + caretakerUsername + "'";
		
		//The list which stores query result
		ArrayList<ArrayList<String>> result = null;
		
		//Executes the query, result is stored in a double arraylist
		try {
			result = databaseConnection.query(queryString);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		//List to store actual patient objects in
		ArrayList<Patient> patients = new ArrayList<>();
		
		for(ArrayList<String> p: result) {
			long SSN = Long.valueOf(p.get(0));
			String firstName = p.get(1);
			String surName = p.get(2);
			char gender = p.get(3).charAt(0);
			int cellPhone = Integer.valueOf(p.get(4));
			String mail = p.get(5);
			String deviceId = p.get(6);
			patients.add(Patient.newPatient(firstName, surName, gender, SSN, cellPhone, mail, deviceId));
		}
		
		return patients;
	}
	
	//Takes any kind of object and parses it into a string on the JSON pattern
	//Is used to take a list of patient objects and json serialize it
	private String toJson(Object o) {
		Gson jsonParser = new Gson();
		return jsonParser.toJson(o);
	}
	
	
}


