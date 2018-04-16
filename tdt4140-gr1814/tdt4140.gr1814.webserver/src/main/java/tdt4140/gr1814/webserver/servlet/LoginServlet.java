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
 * This servlet simply handles log in requests
 * If a log in is considered succesful it will echo back information regarding the care taker that logged in
 * 
 */

/*
 * ================== RESPONSE CODES ===================
 * 			200 - OK
 * 			400 - Bad Arguments
 * 			500 - Internal Database Error, not users fault
 */

public class LoginServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	/*
	 * POST REQUEST for login
	 * Expects username and password as parameter
	 */
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
			//The request expects the two given parameters
			String username = req.getParameter("username");
			String password = req.getParameter("password");
			
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
			
		
			//If either one of the expected parameters are missing, the bad request response code is returned
			if(username == null || password == null) {
				resp.setStatus(400);
				return;
			}
			
			
			
			//Echoes back the care taker information
			resp.getWriter().print(this.requestLogin(username, password, resp));
	}
	
	private String requestLogin(String username, String password, HttpServletResponse resp) {
		DatabaseHandler databaseHandler;
		
		try {
			databaseHandler = new DatabaseHandler();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			resp.setStatus(500);
			return "";
		}
		
		ResultSet result; //Resultset that gathers the result from the query
		try {
			result = databaseHandler.query("SELECT * FROM Caretaker WHERE Username ='"+username+"'");
			if(result == null) {
				resp.setStatus(400);
				return "";
			}
		} catch (SQLException e) {
			e.printStackTrace();
			resp.setStatus(500);
			return "";
		}
		try {
			if(!result.next()) { //Targets the first row in the resultset, if the set is empty the caretaker is non existing
				resp.setStatus(400);
				return "";
			}
			String pw = result.getString("password");
			if(!password.contentEquals(pw)) {
				resp.setStatus(400);
				return "";
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			resp.setStatus(500);
			return "";
		}
		
		//If the request has made it this far through the request, the caretaker is known, and the password is correct.
		
		try {
			String returnString = "{\"username\":\"" + result.getString("Username") + "\", \"address\": \"" + result.getString("Address") + 
					"\", \"name\":\"" + result.getString("Name") + "\"}";
			resp.setStatus(200);
			return returnString;
		} catch (SQLException e) {
			e.printStackTrace();
			resp.setStatus(500);
			return "";
		}
	}

}
