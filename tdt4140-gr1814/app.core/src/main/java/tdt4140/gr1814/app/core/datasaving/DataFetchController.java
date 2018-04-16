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
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;

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
	
	private static final String secret = "SVEIN_ER_SJEFEN_I_GATA";
	private KeyPair keyPair = null;
	private PublicKey serverPublicKey;
	
	//The port the server is listening for http requests on
	private final int serverPort = 8080;

	public Caretaker logIn(String username, String password) {
		
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
			
			
		Long now = Date.from(Instant.now()).getTime() / 1000;
		String hash = DigestUtils.sha1Hex(now + secret);
			
		//Insert Request String
		String params = "username=" + username + "&password=" + password + "&timestamp=" 
				+ now + "&hash=" + hash;
			
		//Pass the arguments through the outputstream
		try {
	      DataOutputStream wr = new DataOutputStream (
	          connection.getOutputStream ());
	      wr.writeBytes (params);
	      wr.flush ();
	      wr.close ();
	      int statusMessage = connection.getResponseCode();
	      System.out.println(statusMessage + "");
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
			Caretaker caretaker = new Caretaker(o.get("username").getAsString(), "password", o.get("name").getAsString(), o.get("address").getAsString());
			int statusMessage = connection.getResponseCode();
			System.out.println(statusMessage + "");
			return caretaker;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	return null;
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
	
	//----------------------------- POST METHODES -----------------------------
	
	public void updatePassword(Caretaker caretaker, String newPassword) {
		String sendStr = "username=" + caretaker.getUsername() + "&password=" + newPassword;
		
		doPost(sendStr,"caretaker?");
	}
	
	public void activateAlarmActivated(Patient patient,Boolean bool) {
		String sendStr = "ssn=" + patient.getSSN() + "&activate=" + bool.toString();
		
		doPost(sendStr,"patient?");
	}
	
	public void caretakerForPatient(Caretaker caretaker,Patient patient) {
		String sendStr = "caretaker_id=" + caretaker.getUsername() + "&ssn=" + patient.getSSN();
		
		doPost(sendStr,"patient?");
	}
	
	public void deleteZone(Patient patient) {
		String sendStr = "ssn=" + patient.getSSN() + "&delete=yes";
		
		doPost(sendStr,"zone?");
	}
	
	public void deletePatient(Patient patient) {
		String sendStr = "ssn=" + patient.getSSN() + "&delete=yes";
		
		doPost(sendStr,"patient?");	
	}
	
	public void insertZone(Patient patient) {
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
		
		doPost(sendStr,"zone?");
	}
	
	public void insertNewPatient(Patient patient) {
		String sendStr = "firstname=" + patient.getFirstName() +"&surname=" + patient.getSurname() + "&ssn=" + patient.getSSN() + "&phone=" + patient.getNoK_cellphone() +
				"&email=" + patient.getNoK_email() + "&gender=" + patient.getGender() + "&id=" + patient.getID();
		
		doPost(sendStr,"patient?");
	}
	
	//------------------------------------ GET METHODES --------------------------------------------
	
	public void fetchPatients(Caretaker systemUser, boolean isTest) {
		
		String content = doGet("patient?caretaker_id=" + systemUser.getUsername());
		
		Gson gson = new Gson();
		
		JsonParser jsonParser = new JsonParser();
		JsonArray jsonArray = (JsonArray) jsonParser.parse(content);
		for(JsonElement j: jsonArray) {
			try {
				JsonObject o = gson.fromJson(j, JsonObject.class);
				boolean alarmActivation = true;
				if(o.get("alarmActivated").getAsInt() == 0) {
					alarmActivation = false;
				}
				Patient.newPatient(o.get("FirstName").getAsString(), o.get("Surname").getAsString(), o.get("Gender").getAsString().charAt(0),o.get("SSN").getAsLong() , o.get("NoK_cellphone").getAsInt(), o.get("NoK_email").getAsString(), o.get("DeviceID").getAsString(), alarmActivation, false);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public void fetchPatients(Caretaker systemUser) {
	
		String content = doGet("patient?caretaker_id=" + systemUser.getUsername());
		
		Gson gson = new Gson();
		
		JsonParser jsonParser = new JsonParser();
		JsonArray jsonArray = (JsonArray) jsonParser.parse(content);
		for(JsonElement j: jsonArray) {
			try {
				JsonObject o = gson.fromJson(j, JsonObject.class);
				boolean alarmActivation = true;
				if(o.get("alarmActivated").getAsInt() == 0) {
					alarmActivation = false;
				}
				Patient.newPatient(o.get("FirstName").getAsString(), o.get("Surname").getAsString(), o.get("Gender").getAsString().charAt(0),o.get("SSN").getAsLong() , o.get("NoK_cellphone").getAsInt(), o.get("NoK_email").getAsString(), o.get("DeviceID").getAsString(), alarmActivation, true);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void getPatientsZones(Caretaker caretaker) {
		
		String content = doGet("zone?caretaker_id=" + caretaker.getUsername());
		
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
		}
		
		
	}

	
	//------------------------------ HELPING METHODES FOR POSTING AND RECEIVING INFORMATION ---------------------------------------
	
	public void doPost(String sendStr, String loc) {
		//Opens a connection to the server
		
		Long now = Date.from(Instant.now()).getTime() / 1000;
		String hash = DigestUtils.sha1Hex(now + secret);
		sendStr += "&timestamp=" + now + "&hash=" + hash;
		
		HttpURLConnection connection = this.connect(loc);
		
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
			int statusMessage = connection.getResponseCode();
			System.out.println(statusMessage + "");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String doGet(String loc) {
		Long now = Date.from(Instant.now()).getTime() / 1000;
		String hash = DigestUtils.sha1Hex(now + secret);
		
		loc += "&timestamp=" + now + "&hash=" + hash;
		HttpURLConnection connection = this.connect(loc);
		
		if(connection == null) {
			System.out.println("Connection trouble...");
			return null;
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
			int statusMessage = connection.getResponseCode();
			System.out.println(statusMessage + "");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return content;
	}
}
