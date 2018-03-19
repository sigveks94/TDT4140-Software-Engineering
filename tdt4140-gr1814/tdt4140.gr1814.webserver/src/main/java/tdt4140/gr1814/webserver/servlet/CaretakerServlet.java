package tdt4140.gr1814.webserver.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tdt4140.gr1814.webserver.ConnectionHandler;

public class CaretakerServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	
	ConnectionHandler databaseConnection = null;
	
	//Private method for establishing connection with the database
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
	 *	POST REQUEST can handle the follow:
	 *	- Change of password given username and new password
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		//Sets up the connection to the database
		if(!this.establishConnection(resp)) {
			resp.setStatus(500); //Internal DB Error
			return;
		}
		
		//This is the expected paramaters for a password change
		if(req.getParameter("username") != null && req.getParameter("password") != null) {
			if(this.changePassword(req.getParameter("username"), req.getParameter("password"))) {
				resp.setStatus(200);
				return;
			}
			else {
				resp.setStatus(500); //Internal DB Error
				return;
			}
		}
		
		
	}
	
	//Private method for handling a change of password
	private boolean changePassword(String caretakerUsername, String newPassword) {
		
		String query = "SELECT Username FROM Caretaker WHERE Username LIKE \"" + caretakerUsername + "\"";
		ArrayList<ArrayList<String>> resultList = null;
		try {
			resultList = databaseConnection.query(query);
			if(resultList.size() != 1)
				return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		query = "UPDATE Caretaker SET Password = \"" + newPassword + "\"";
		try {
			databaseConnection.update(query);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		
	}
	
}
