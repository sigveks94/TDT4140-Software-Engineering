package tdt4140.gr1814.app.core;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

/*
 * Class for establishing connection with the webserver and fetching data as well as passing data to the DB
 */

public class DataFetchController {
	
	//The port the server is listening for http requests on
	private final int serverPort = 8085;
	
	public DataFetchController() {
		
	}
	
	public static void main(String[] args) {
		DataFetchController controller = new DataFetchController();
		controller.fetchPatients("motherofthree");
	}
	
	//Method for establishing connection with the server. The postfix is used to determine which servlet to access
	private HttpURLConnection connect(String postfix) {
		try {
			URL address = new URL("http://localhost:" + serverPort + "/" + postfix);
			HttpURLConnection connection = (HttpURLConnection) address.openConnection();
			return connection;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	public void fetchPatients(String caretakerId) {
	
		HttpURLConnection connection = this.connect("patient?caretaker_id=" + caretakerId);
		
		if(connection == null) {
			System.out.println("Connection trouble...");
			return;
		}
		try {
			connection.setRequestMethod("GET");
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String content = "";
		try {
			InputStream input = connection.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(input));
			String line = "";
			while((line = br.readLine()) != null) {
				content += line;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//TODO
		//Convert from JSON and create patients via the Patient.newPatient interface.
		
		
	}
}
