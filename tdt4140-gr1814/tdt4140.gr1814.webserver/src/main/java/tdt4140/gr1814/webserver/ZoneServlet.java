package tdt4140.gr1814.webserver;

import javax.servlet.http.HttpServlet;

public class ZoneServlet extends HttpServlet{

	//Serial Version, if the servlet ever changes something that will have inpact on the http request,
	//the serial version should also be updated
	private static final long serialVersionUID = 1L;
	
	ConnectionHandler databaseConnection;
	
	public ZoneServlet() {
		databaseConnection = new ConnectionHandler();
		databaseConnection.connect();
	}
	
	
	
}
