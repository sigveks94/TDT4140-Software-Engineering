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
 * ================== RESPONSE CODES ===================
 * 			200 - OK
 * 			400 - Bad Arguments
 * 			500 - Internal Database Error, not users fault
 */


public class CaretakerServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	
	/*
	 *	POST REQUEST can handle the follow:
	 *	- Change of password given username and new password
	 */
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String userName = req.getParameter("username");
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
		
		//If either the username og password arguments are missing the request is bad
		if(userName == null || password == null) {
			resp.setStatus(400);
			return;
		}
		
		this.changePassword(userName, password, resp);
	}
	
	//Private method for handling a change of password
	private void changePassword(String caretakerUsername, String newPassword, HttpServletResponse resp) {
		
		DatabaseHandler databaseHandler;
		
		try {
			databaseHandler = new DatabaseHandler();
		} catch (ClassNotFoundException | SQLException e1) {
			e1.printStackTrace();
			resp.setStatus(500);
			return;
		}
		
		String query = "SELECT Username FROM Caretaker WHERE Username LIKE \"" + caretakerUsername + "\"";
		ResultSet result;
		try {
			result = databaseHandler.query(query);
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}
		try {
			if(!result.next()) {
				resp.setStatus(400);
				return;
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
			resp.setStatus(500);
			return;
		}
		
		query = "UPDATE Caretaker SET Password = \"" + newPassword + "\" WHERE Username LIKE \"" + caretakerUsername + "\";";
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
	
}
