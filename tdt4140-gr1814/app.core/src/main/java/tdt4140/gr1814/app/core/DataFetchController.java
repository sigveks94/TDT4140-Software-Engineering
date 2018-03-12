package tdt4140.gr1814.app.core;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/*
 * Class for establishing connection with the webserver and fetching data as well as passing data to the DB
 */

public class DataFetchController {
	
	//The port the server is listening for http requests on
	private final int serverPort = 8080;
	
	public DataFetchController() {
		
	}
	
	private void establishConnection(String postfix) {
		
	}
	
	public void fetchPatients(String caretakerId) {
		
		HttpURLConnection connection = null;
		
		try {
			URL address = new URL("localhost:" + serverPort + "/patient");
			connection = (HttpURLConnection) address.openConnection();
			connection.setRequestMethod("GET");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

}
