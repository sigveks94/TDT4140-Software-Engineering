package tdt4140.gr1814.webserver.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tdt4140.gr1814.webserver.ConnectionHandler;
import tdt4140.gr1814.webserver.DatabaseHandler;

public class LoginServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	
	/*
	 * POST REQUEST for login
	 * Expects username and password as parameter
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			DatabaseHandler databaseHandler;
		
			try {
				databaseHandler = new DatabaseHandler();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
				resp.setStatus(500); //Internal DB ERROR
				return;
			}
			
			//The request expects the two given parameters
			String username = req.getParameter("username");
			String password = req.getParameter("password");
		
			//If either one of the expected parameters are missing, the bad request response code is returned
			if(username == null || password == null) {
				resp.setStatus(400); //Bad Request - Code
				return;
			}
			
			ResultSet result; //Resultset that gathers the result from the query
			try {
				result = databaseHandler.query("SELECT * FROM Caretaker WHERE Username ='"+username+"'");
				if(result == null) {
					resp.setStatus(400);
					return;
				}
			} catch (SQLException e) {
				e.printStackTrace();
				resp.setStatus(500); //Internal DB ERROR
				return;
			}
			try {
				result.next(); //Targets the first row in the resultset
				
				String pw = result.getString("password");
				if(!password.contentEquals(pw)) {
					resp.setStatus(400);
					return;
				}
				
				
			} catch (SQLException e) {
				e.printStackTrace();
				resp.setStatus(500);
				return;
			}
			
			//If the request has made it this far through the request, the caretaker is known, and the password is correct.
			
			try {
				String returnString = "{\"username\":\"" + result.getString("Username") + "\", \"address\": \"" + result.getString("Address") + 
						"\", \"firstName \":\"" + result.getString("Firstname") + "\", \"lastName \":\"" + result.getString("Lastname") + "\" }";
				resp.setStatus(200); //OK
				resp.getWriter().print(returnString);
			} catch (SQLException e) {
				e.printStackTrace();
				resp.setStatus(500);
			}
			
	}
	
	

}
