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
 * This servlet has the follow functions:
 * 		- Fetching all zones associated with a caretaker
 * 		- Inserting a zone to a database connecting it to the corresponding ssn
 * 		- Deleting a zone and remove the link to a patient
 */

/*
 * ================== RESPONSE CODES ===================
 * 			200 - OK
 * 			400 - Bad Arguments
 * 			500 - Internal Database Error, not users fault
 */

public class ZoneServlet extends HttpServlet{

	//Serial Version, if the servlet ever changes something that will have inpact on the http request,
	//the serial version should also be updated
	private static final long serialVersionUID = 1L;
	
	/*
	 * Get requests can handle following inputs and outputs:
	 * - caretaker_id : returns all zones associated with all patients associated with the given caretaker
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
		
		resp.getWriter().print(this.getAllZones(caretakerId, resp));
		
	}
	
	/*
	 * The POST Request handles requests for inserting new zones and deleting current zones
	 * Expected parameters for inserting: ssn and a list of {lat, long, point_order}
	 * Expected parameters for deleting: ssn and a delete = yes
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
		
		//If the delete paramater is present then the delete method is called
		if(req.getParameter("delete") != null && req.getParameter("delete").contentEquals("yes")){
			if(req.getParameter("ssn") == null || req.getParameter("ssn").length() != 11) {
				resp.setStatus(400);
				return;
			}
			
			this.deleteZone(req.getParameter("ssn"), resp);
			return;
		}
		
		String ssn = req.getParameter("ssn");
		String zone = req.getParameter("zone");
		if(ssn == null || ssn.length() != 11 || zone == null) {
			resp.setStatus(400);
			return;
		}
		
		insertZone(ssn, zone, resp);
		
	}
	
	//Inserts the zone given
	private void insertZone(String ssn, String zone, HttpServletResponse resp) {
		
		DatabaseHandler databaseHandler;
		
		try {
			databaseHandler = new DatabaseHandler();
		} catch (ClassNotFoundException | SQLException e1) {
			e1.printStackTrace();
			resp.setStatus(500);
			return;
		}
		
		String zoneId = null;
		
		String query = "SELECT MAX(ZoneID) AS zoneID FROM Zone";
		try {
			ResultSet result = databaseHandler.query(query);
			result.next();
			zoneId = result.getString("zoneID");
		} catch (SQLException e) {
			e.printStackTrace();
			resp.setStatus(500);
			return;
		}
		if(zoneId == null) {
			resp.setStatus(500);
			return;
		}
		try {
			zoneId = String.valueOf((Integer.parseInt(zoneId) + 1));
		}
		catch(NumberFormatException e) {
			e.printStackTrace();
			resp.setStatus(500);
			return;
		}
		
		query = "INSERT INTO Zone (ZoneID, PatientSSN) VALUES (" + zoneId + "," + ssn + ")";
		try {
			databaseHandler.update(query);
		} catch (SQLException e) {
			e.printStackTrace();
			resp.setStatus(500);
			return;
		}
		
		
		zone = zone.substring(1);
		while(zone.length() > 0) {
			ZonePointWrapper zp = new ZonePointWrapper(zone.substring(0, zone.indexOf('}') + 1));
			query = "INSERT INTO ZonePoint (Lat, Longt, PointOrder, ZoneId, DeviceID) VALUES (" + zp.lat + ", " + zp.longt + ", " + zp.order + ", " + zoneId + ", \"??\");";
			try {
				databaseHandler.update(query);
			} catch (SQLException e) {
				e.printStackTrace();
				resp.setStatus(500);
				return;
			}
			zone = zone.substring(zone.indexOf('}') + 2);
		}
		resp.setStatus(200);
	}
	
	//Deletes the zone given by the ssn
	private void deleteZone(String ssn, HttpServletResponse resp) {
		
		DatabaseHandler databaseHandler;
		try {
			databaseHandler = new DatabaseHandler();
		} catch (ClassNotFoundException | SQLException e1) {
			e1.printStackTrace();
			resp.setStatus(500);
			return;
		}
		
		String query = "DELETE FROM Zone WHERE PatientSSN LIKE " + ssn + ";";
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
	
	private String getAllZones(String caretakerId, HttpServletResponse resp ){
		DatabaseHandler databaseHandler;
		
		try {
			databaseHandler = new DatabaseHandler();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			resp.setStatus(500);
			return "";
		}
		
		ResultSet result;
		String query = "SELECT Zone.patientSSN, ZonePoint.*, PatientCaretaker.CaretakerUsername FROM ZonePoint NATURAL JOIN Zone "
						+ "INNER JOIN PatientCaretaker ON Zone.PatientSSN = PatientCaretaker.PatSSN WHERE PatientCaretaker.CaretakerUsername = '" 
						+ caretakerId + "' ORDER BY ZonePoint.ZoneID ASC, ZonePoint.PointOrder ASC";
		
		try {
			result = databaseHandler.query(query);
		} catch (SQLException e) {
			e.printStackTrace();
			resp.setStatus(500);
			return "";
		}
		
		String json = "[";
		try {
			while(result.next()) {
				json += "{\"zone_id\":" + result.getInt("ZoneID") + ",\"ssn\":" + result.getLong("PatientSSN") + ",\"lat\":" + result.getDouble("Lat") 
				+ ",\"long\":" + result.getDouble("Longt") + ",\"point_order\":" + result.getInt("PointOrder") + "},";
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(json.length() > 1)
			json = json.substring(0, json.length() -1);
		return json + "]";
	}
	
	//Wrapper class for extracting the information from the request parameter
	private class ZonePointWrapper{
		String lat;
		String longt;
		int order;
		
		//Constructor expects a string with format: {lat, long, order}
		ZonePointWrapper(String param){
			param = param.substring(1);
			order = Integer.parseInt(param.substring(0, param.indexOf(',')));
			param = param.substring(param.indexOf(',') + 1);
			lat = param.substring(0, param.indexOf(','));
			param = param.substring(param.indexOf(',') + 1);
			longt = param.substring(0, param.indexOf('}'));
		}
	}
	
}



