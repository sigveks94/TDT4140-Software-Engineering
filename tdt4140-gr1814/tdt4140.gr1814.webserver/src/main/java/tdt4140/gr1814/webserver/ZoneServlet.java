package tdt4140.gr1814.webserver;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse; 
//This is the sevlet supposed to handle all fetching and updating of zones in the database
public class ZoneServlet extends HttpServlet{

	//Serial Version, if the servlet ever changes something that will have inpact on the http request,
	//the serial version should also be updated
	private static final long serialVersionUID = 1L;
	
	//The connection helper class
	ConnectionHandler databaseConnection;
	
	//Private method for establishing connection with the database
	private void establishConnection(HttpServletResponse resp) {
		databaseConnection = new ConnectionHandler();
		try {
			databaseConnection.connect();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			resp.setStatus(500); //Internal DB error
			return;
		} 
	}
	
	/*
	 * Get requests can handle following inputs and outputs:
	 * - patientId : returns the zone associated with the patient given
	 * - caretaker_id : returns all zones associated with all patients associated with the given caretaker
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		//Establishes connection with the database
		this.establishConnection(resp);
		
		//Fetches the response input stream in order to echo answer back to the requester
		PrintWriter echoWriter = resp.getWriter();
		
		//If a SSN is passed, the response should be the zone associated with this patient
		if(req.getParameter("ssn") != null) {
			Long ssn = null;
			try { //Tries to parse to long, if parse fails the argument is considered invalid
				ssn = Long.valueOf(req.getParameter("ssn"));
				//Even though the parse works it does not prove this is a valid SSN, this checks that the given SSN is in fact 11 digits long
				if(req.getParameter("ssn").length() != 11) {
					resp.setStatus(400); //Bad Request Code
					return;
				}
			}
			catch(NumberFormatException exception) {
				exception.printStackTrace();
				resp.setStatus(400); //Bad Request Code
				return;
			}
			
			//If all of the above works, the query is executed, the result is coded into a JSON serialization and the result is echoed back to the client that performed the HTTPRequest
			echoWriter.print(toJson(getZone(ssn)));
			echoWriter.flush();
			echoWriter.close();
		}
		//If the given argument is a caretaker username
		else if(req.getParameter("caretaker_id") != null){
			//If the given caretakerId does not corrospond to any zones this request will simply return a empty list and therefor does not need any further validation unlike the alternativ above
			echoWriter.print(toJson(getAllZones(req.getParameter("caretaker_id"))));
			echoWriter.flush();
			echoWriter.close();
		}
		else { //If none of the above options was satisified the input was on invalid form
			resp.setStatus(400); //Bad Request code
			return;
		}
		
	}
	
	/*
	 * The POST Request handles requests for inserting new zones and updating current ones
	 * Expected parameters: ssn, lat, long, point_order
	 */
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		this.establishConnection(resp);
		
		String ssn = req.getParameter("ssn");
		String lat = req.getParameter("lat");
		String longt = req.getParameter("long");
		String point_order = req.getParameter("point_order");
		System.out.println(ssn + " " + lat + " " + longt + " " + point_order);
		
		String zoneId = null;
		
		String query = "SELECT MAX(ZoneID) AS zoneID FROM ZonePoint";
		try {
			zoneId = databaseConnection.query(query).get(0).get(0);
		} catch (SQLException e) {
			e.printStackTrace();
			resp.setStatus(500); //Internal DB Error
			return;
		}
		if(zoneId == null) {
			resp.setStatus(500); //Internal DB Error
			return;
		}
		try {
			zoneId = String.valueOf((Integer.parseInt(zoneId) + 1));
		}
		catch(NumberFormatException e) {
			e.printStackTrace();
			resp.setStatus(500); //Internal DB Error
			return;
		}
		
		query = "INSERT INTO Zone (ZoneID, PatientSSN) VALUES (" + zoneId + "," + ssn + ")";
		try {
			databaseConnection.update(query);
		} catch (SQLException e) {
			e.printStackTrace();
			resp.setStatus(500); //Internal DB Error
			return;
		}
		
		query = "INSERT INTO ZonePoint (Lat, Longt, PointOrder, ZoneID, DeviceID) VALUES (" + lat +"," + longt + "," + point_order + "," + zoneId + "id99)";
		try {
			databaseConnection.update(query);
		} catch (SQLException e) {
			e.printStackTrace();
			resp.setStatus(500); //Internal DB Error
			return;
		}
		resp.setStatus(200);
	}
	
	//Private method for getting zones associated with a single patient given the SSN of the patient
	private ArrayList<ArrayList<String>> getZone(long ssn){
		try {
			return databaseConnection.query("SELECT * FROM ZonePoint INNER JOIN Zone ON Zone.ZoneID = ZonePoint.ZoneID WHERE Zone.PatientSSN LIKE '" + ssn + "'");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//Private method that fetches all zones associated with all the patients assoiated with a given care taker given by the care taker username
	private ArrayList<ArrayList<String>> getAllZones(String caretakerId){
		try {
			return databaseConnection.query("SELECT Zone.patientSSN, ZonePoint.*, PatientCaretaker.CaretakerUsername FROM ZonePoint NATURAL JOIN Zone "
					+ "INNER JOIN PatientCaretaker ON Zone.PatientSSN = PatientCaretaker.PatSSN WHERE PatientCaretaker.CaretakerUsername = '" 
					+ caretakerId + "' ORDER BY ZonePoint.ZoneID ASC, ZonePoint.PointOrder ASC");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//Takes an ArrayList holding an ArrayList of strings as paramter. This should essentially cosist of a list of zonecursors from the SQL Query.
	//This method parses the list into a JSON serialized String on the format: {{"zone_id":1,"ssn":12345678901,"lat":63.41949000273954,"long":10.397296119049088,"point_order": 5},{ --==--}}
	private String toJson(ArrayList<ArrayList<String>> result) {
		
		if(result.size() < 1) {
			return "[]";
		}
		
		String json = "[";
		for(ArrayList<String> lst: result) {
			json += "{\"zone_id\":" + lst.get(5) + ",\"ssn\":" + lst.get(0) + ",\"lat\":" + lst.get(2) + ",\"long\":" + lst.get(3) + ",\"point_order\":" + lst.get(4) + "},";
		}
		
		return json.substring(0, json.length()-1) + "]";
	}
}
