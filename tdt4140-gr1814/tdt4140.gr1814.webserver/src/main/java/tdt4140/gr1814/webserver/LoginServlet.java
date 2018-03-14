package tdt4140.gr1814.webserver;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tdt4140.gr1814.app.core.Caretaker;

public class LoginServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	ConnectionHandler databaseConnection;
	
	public LoginServlet() {
		databaseConnection = new ConnectionHandler();
		databaseConnection.connect();
	}
	
	/*
	 * POST REQUEST for login
	 * Expects username and password as parameter
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		
		
			String username = req.getParameter("username");
			String password = req.getParameter("password");
			
			System.out.println("POST:" + username + ", pw:" + password);
			
			if(username == null || password == null) {
				return; //Return errorcode here
			}
		
			try {
				ArrayList<ArrayList<String>> caretaker = databaseConnection.query("SELECT * FROM Caretaker WHERE Username ='"+username+"'");
				if(caretaker.isEmpty()) {
					return; // Return invalid login here
				}
				String pw = caretaker.get(0).get(1);
				if(password.equals(password)) {
					System.out.println("success");
					PrintWriter writer = resp.getWriter();
					writer.print("{\"username\":\"" + caretaker.get(0).get(0) + "\"}");
					return; // Return success code
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	}
	
	

}
