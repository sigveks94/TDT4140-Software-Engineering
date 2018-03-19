package tdt4140.gr1814.app.core.datasaving;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import tdt4140.gr1814.app.core.participants.Caretaker;
import tdt4140.gr1814.app.core.participants.Patient;
import tdt4140.gr1814.app.core.zones.Point;
import tdt4140.gr1814.app.core.zones.Zone;
import tdt4140.gr1814.app.core.zones.ZoneTailored;

/*
 * Class for establishing connection with the webserver and fetching data as well as passing data to the DB
 */


public class DataFetchController {
	
	//The port the server is listening for http requests on
	private final int serverPort = 8080;
	
	public DataFetchController() {
		
	}
	
	
	public Caretaker logIn(String username, String password) {
		
		//Opens a connection to the server
			HttpURLConnection connection = this.connect("login");
			
			if(connection == null) {
				System.out.println("Connection trouble...");
				return null;
			}
			
			//Sets requestMethod to POST and enables the outputStream
			try {
				connection.setRequestMethod("POST");
				connection.setDoOutput(true);
			} catch (ProtocolException e) {
				e.printStackTrace();
			}
			
			//Insert Request String
				String params = "username=" + username + "&password=" + password;
				
			//Pass the arguments through the outputstream
			try {
		      DataOutputStream wr = new DataOutputStream (
		                  connection.getOutputStream ());
		      wr.writeBytes (params);
		      wr.flush ();
		      wr.close ();
			}
			catch(IOException e) {
				e.printStackTrace();
			}
			
			//Retrieves the inputstream (webservers outputstream) For som reason this needs to be called in order for to execute the POSTRequest
			try {
				InputStream connectionInputStream = connection.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(connectionInputStream));
				String content = "";
				String line = "";
				while((line = reader.readLine())!= null) {
					content += line;
				}
				Gson gson = new Gson();
				JsonObject o = gson.fromJson(content, JsonObject.class);
				Caretaker caretaker = new Caretaker(o.get("username").getAsString(), "password", "Firstname", "lastname", o.get("address").getAsString());
				return caretaker;
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		return null;
	}
	
	public void updatePassword(Caretaker caretaker, String newPassword) {
		//Opens a connection to the server
				HttpURLConnection connection = this.connect("caretaker");
				
				if(connection == null) {
					System.out.println("Connection trouble...");
					return;
				}
				
				//Sets requestMethod to POST and enables the outputStream
				try {
					connection.setRequestMethod("POST");
					connection.setDoOutput(true);
				} catch (ProtocolException e) {
					e.printStackTrace();
				}
				
				//Insert Request String
				String sendStr = "username=" + caretaker.getUsername() + "&password=" + newPassword;
					
					
				//Pass the arguments through the outputstream
				try {
			      DataOutputStream wr = new DataOutputStream (
			                  connection.getOutputStream ());
			      wr.writeBytes (sendStr);
			      wr.flush ();
			      wr.close ();
				}
				catch(IOException e) {
					e.printStackTrace();
				}
				
				//Retrieves the inputstream (webservers outputstream) For som reason this needs to be called in order for to execute the POSTRequest
				try {
					InputStream connectionInputStream = connection.getInputStream();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
	
	
	public void fetchPatients(Caretaker systemUser) {
	
		HttpURLConnection connection = this.connect("patient?caretaker_id=" + systemUser);
		
		if(connection == null) {
			System.out.println("Connection trouble...");
			return;
		}
		
		try {
			connection.setRequestMethod("GET");
		} catch (ProtocolException e) {
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
		
		Gson gson = new Gson();
		
		JsonParser jsonParser = new JsonParser();
		JsonArray jsonArray = (JsonArray) jsonParser.parse(content);
		for(JsonElement j: jsonArray) {
			try {
				JsonObject o = gson.fromJson(j, JsonObject.class);
				Patient.newPatient(o.get("FirstName").getAsString(), o.get("Surname").getAsString(), o.get("Gender").getAsString().charAt(0),o.get("SSN").getAsLong() , o.get("NoK_cellphone").getAsInt(), o.get("NoK_email").getAsString(), o.get("DeviceID").getAsString(), o.get("AlarmActivated").getAsBoolean());
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void getPatientsZones(Caretaker caretaker) {
		HttpURLConnection connection = this.connect("zone?caretaker_id=" + caretaker.getUsername());
		
		if(connection == null) {
			System.out.println("Connection trouble...");
			return;
		}
		
		try {
			connection.setRequestMethod("GET");
		} catch (ProtocolException e) {
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
		Gson gson = new Gson();
		JsonParser jsonParser = new JsonParser();
		JsonArray jsonArray = (JsonArray) jsonParser.parse(content);
		int prevZoneID = -1;
		int nr = 0;
		ArrayList<Point> points = new ArrayList<>();
		for(JsonElement j: jsonArray) {
			try {
				nr++;
				JsonObject o = gson.fromJson(j, JsonObject.class);
				if (prevZoneID == -1) {
					prevZoneID = o.get("zone_id").getAsInt();
					points.add(new Point(Patient.getPatient(o.get("ssn").getAsLong()).getID(),
							o.get("lat").getAsDouble(),o.get("long").getAsDouble()));
				} else if ((nr == jsonArray.size())) {
					points.add(new Point(Patient.getPatient(o.get("ssn").getAsLong()).getID(),
							o.get("lat").getAsDouble(),o.get("long").getAsDouble()));
					Zone zone = new ZoneTailored(points);
					Patient.getPatient(points.get(0).getDeviceId()).addZone(zone);
				} else if (prevZoneID == o.get("zone_id").getAsInt()) {
					points.add(new Point(Patient.getPatient(o.get("ssn").getAsLong()).getID(),
							o.get("lat").getAsDouble(),o.get("long").getAsDouble()));
				} else if (prevZoneID < o.get("zone_id").getAsInt()) {
					Zone zone = new ZoneTailored(points);
					Patient.getPatient(points.get(0).getDeviceId()).addZone(zone);
					prevZoneID = o.get("zone_id").getAsInt();
					points = new ArrayList<>();
					points.add(new Point(Patient.getPatient(o.get("ssn").getAsLong()).getID(),
							o.get("lat").getAsDouble(),o.get("long").getAsDouble()));
				} else {
					throw new Exception("Something went wrong with creating a new zone");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} return;
		
		
	}
	
	public void insertNewPatient(Patient patient) {
		
		//Opens a connection to the server
		HttpURLConnection connection = this.connect("patient?");
		
		if(connection == null) {
			System.out.println("Connection trouble...");
			return;
		}
		
		//Sets requestMethod to POST and enables the outputStream
		try {
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
		} catch (ProtocolException e) {
			e.printStackTrace();
		}
		
		//Insert Request String
			String params = "firstname=" + patient.getFirstName() +"&surname=" + patient.getSurname() + "&ssn=" + patient.getSSN() + "&phone=" + patient.getNoK_cellphone() +
					"&email=" + patient.getNoK_email() + "&gender=" + patient.getGender() + "&id=" + patient.getID();
			
		//Pass the arguments through the outputstream
		try {
	      DataOutputStream wr = new DataOutputStream (
	                  connection.getOutputStream ());
	      wr.writeBytes (params);
	      wr.flush ();
	      wr.close ();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
		//Retrieves the inputstream (webservers outputstream) For som reason this needs to be called in order for to execute the POSTRequest
		try {
			InputStream connectionInputStream = connection.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void insertZone(Patient patient) {
		//Opens a connection to the server
		HttpURLConnection connection = this.connect("zone");
		
		if(connection == null) {
			System.out.println("Connection trouble...");
			return;
		}
		
		//Sets requestMethod to POST and enables the outputStream
		try {
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
		} catch (ProtocolException e) {
			e.printStackTrace();
		}
		
		//Insert Request String
			Zone zone = patient.getZone();
			String sendStr = "ssn=" + patient.getSSN() + "&zone=[";
			int order = 0;
			for (Point poi : zone.getPoints()) {
				sendStr += "{" + order + "," + poi.getLat() + "," + poi.getLongt() + "}";
				order++;
				if (order < zone.getPoints().size()) {
					sendStr += ",";
				}
				
			} sendStr += "]";
			
			
		//Pass the arguments through the outputstream
		try {
	      DataOutputStream wr = new DataOutputStream (
	                  connection.getOutputStream ());
	      wr.writeBytes (sendStr);
	      wr.flush ();
	      wr.close ();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
		//Retrieves the inputstream (webservers outputstream) For som reason this needs to be called in order for to execute the POSTRequest
		try {
			InputStream connectionInputStream = connection.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
