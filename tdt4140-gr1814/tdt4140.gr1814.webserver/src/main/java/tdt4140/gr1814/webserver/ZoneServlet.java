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

public class ZoneServlet extends HttpServlet{

	//Serial Version, if the servlet ever changes something that will have inpact on the http request,
	//the serial version should also be updated
	private static final long serialVersionUID = 1L;
	
	ConnectionHandler databaseConnection;
	
	public ZoneServlet() {
		databaseConnection = new ConnectionHandler();
		databaseConnection.connect();
	}
	
	/*
	 * Get requests can handle following inputs and outputs:
	 * - patientId : returns the zone associated with the patient given
	 * - caretaker_id : returns all zones associated with all patients associated with the given caretaker
	 */
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		//Fetches the response input stream in order to echo answer back to the requester
		PrintWriter echoWriter = resp.getWriter();
		
		//If a SSN is passed, the response should be the zone associated with this patient
		if(req.getParameter("ssn") != null) {
			Long ssn = null;
			try {
				ssn = Long.valueOf(req.getParameter("ssn"));
			}
			catch(NumberFormatException exception) {
				exception.printStackTrace();
				return;
			}
			
			echoWriter.print(toJson(getZone(ssn)));
			echoWriter.flush();
			echoWriter.close();
		}
		//If the given argument is a caretaker username
		else if(req.getParameter("caretaker_id") != null){;
			echoWriter.print(toJson(getAllZones(req.getParameter("caretaker_id"))));
			echoWriter.flush();
			echoWriter.close();
		}
		
	}
	
	private ArrayList<ArrayList<String>> getZone(long ssn){
		try {
			return databaseConnection.query("SELECT * FROM ZonePoint INNER JOIN Zone ON Zone.ZoneID = ZonePoint.ZoneID WHERE Zone.PatientSSN LIKE '" + ssn + "'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private ArrayList<ArrayList<String>> getAllZones(String caretakerId){
		try {
			return databaseConnection.query("SELECT Zone.patientSSN, ZonePoint.*, PatientCaretaker.CaretakerUsername FROM ZonePoint NATURAL JOIN Zone "
					+ "INNER JOIN PatientCaretaker ON Zone.PatientSSN = PatientCaretaker.PatSSN WHERE PatientCaretaker.CaretakerUsername = '" + caretakerId + "'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private String toJson(Object o) {
		Gson jsonParser = new Gson();
		return jsonParser.toJson(o);
	}
	
	
}
