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
	
	public PatientServlet() {
		databaseConnection = new ConnectionHandler();
		databaseConnection.connect();
	}
	
	/*
	 * Get requests can handle following inputs and outputs:
	 * - caretaker_id: returns all the patients associated with this care taker
	 */
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		test();
		
		//Fetches the response input stream in order to echo answer back to the requester
		PrintWriter echoWriter = resp.getWriter();
		
		//If a care taker id is passed as a argument, the response should be a list of all the patients associated with
		//this care taker
		if(req.getParameter("caretaker_id") != null) {
			echoWriter.print(toJson(getMultiplePatients(req.getParameter("caretaker_id"))));
			echoWriter.flush();
			echoWriter.close();
			return;
		}
		
		
	}
	
	public void test() {
		System.out.println("A:" + Patient.getAllPatients().size());
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
	private String toJson(Object o) {
		Gson jsonParser = new Gson();
		return jsonParser.toJson(o);
	}
	
	
}


