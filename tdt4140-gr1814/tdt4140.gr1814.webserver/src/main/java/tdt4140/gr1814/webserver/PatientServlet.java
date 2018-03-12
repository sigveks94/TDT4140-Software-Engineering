package tdt4140.gr1814.webserver;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import tdt4140.gr1814.app.core.Patient;

/*
 * 	This is the HttpServlet supposed to handle database queries concerning the patient objects
 */

public class PatientServlet extends HttpServlet{

	//Serial Version, if the servlet ever changes something that will have inpact on the http request,
	//the serial version should also be updated
	private static final long serialVersionUID = 1L;
	
	/*
	 * Get requests can handle following inputs and outputs:
	 * - SSN : returns single patient
	 * - caretaker_id: returns all the patients associated with this care taker
	 */
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
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
	
	private void getPatient(long SSN) {
		//Dummy
	}
	
	//Returns an array of all patients associated with the given caretaker
	private Patient[] getMultiplePatients(String caretakerId) {
		
		return null;
	}
	
	//Takes a object of any type and converts it to a string on the json pattern
	private String toJson(Object o) {
		Gson jsonParser = new Gson();
		return jsonParser.toJson(o);
	}

	
	
}
