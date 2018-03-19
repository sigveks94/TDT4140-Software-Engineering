package tdt4140.gr1814.webserver.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tdt4140.gr1814.webserver.ConnectionHandler;

public class LoginServlet extends HttpServlet{

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
	 * POST REQUEST for login
	 * Expects username and password as parameter
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
			if(!this.establishConnection(resp)) {
				resp.setStatus(500); //Internal DB Error
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
		
			try {
				ArrayList<ArrayList<String>> caretaker = databaseConnection.query("SELECT * FROM Caretaker WHERE Username ='"+username+"'");
				//If the credentials received has no matching care taker the 401 code is passed
				if(caretaker.isEmpty()) {
					resp.setStatus(401); // Unauthorized - Code
					return;
				}
				
				//If the request has made it this far the log in is succesful. The outputstream then prints back the username and sets the response code to OK
				String pw = caretaker.get(0).get(1);
				if(password.equals(pw)) {
					//If the request has made it this far the log in is succesful. The outputstream then prints back the username and sets the response code to OK
					PrintWriter writer = resp.getWriter();
					writer.print("{\"username\":\"" + caretaker.get(0).get(0) + "\", \"address\": \"" + caretaker.get(0).get(4) + "\"}");
					resp.setStatus(200); //OK - Code
					return;
				}
				else {
					resp.setStatus(401); //Unauthorized 
					return;
				}
				
			} catch (SQLException e) {
				resp.setStatus(500); //Internal DB error code 500
				e.printStackTrace();
			}
			
	}
	
	

}