package tdt4140.gr1814.app.core;

import java.util.ArrayList;
import java.util.Properties;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;

public class Database {
	
	private Connection myConn;
	private Statement myStmt;
	private ResultSet myRs=null;
	
	//THIS CLASS DOES NOT PROVIDE THE NECESSARY SECURITY FOR DATABASE MANIPULATION, THIS WILL BE IMPLEMENTED IN LATER SPRINTS
	
	//******************************************************CONNECTING******************************************************
	
	public void connect() {
		try {
			myConn = DriverManager.getConnection("jdbc:mysql://mysql.stud.ntnu.no:3306/hara_database?autoReconnect=true&useSSL=false","hara_db","gruppe14");
		}catch (Exception ex){
				ex.printStackTrace();
			}
		}
	
	
	
	
	
	//******************************************************GETTERS******************************************************
	
		public Connection getConnection() {
			return myConn;
		}
		
		public Statement getStatement() {
			return myStmt;
		}
		
		public ResultSet getResultSet() {
			return myRs;
		}
		
		
		
		
	
	//******************************************************GENERAL SQL HELP METHODS******************************************************
	
		//a general method for changing the db(delete, insert, update), used as a help method.
		public void update(String query) {
			try {
	            myStmt = myConn.createStatement();
	            myStmt.executeUpdate(query);
	        } catch (Exception e) {
	            System.out.println("The update query failed. Check your sql syntax.");
	            e.printStackTrace();
	        }
		}
		
		//a general method for querying the db(select-queries), used as a help method
		public ArrayList<ArrayList<String>> query(String query) throws SQLException {
			try {
				if (myConn != null) {
					myStmt = myConn.createStatement();
					myStmt.executeQuery(query);
					myRs = myStmt.getResultSet();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			ArrayList<ArrayList<String>> returnList = new ArrayList<ArrayList<String>>();
			if (myRs != null) {
				while (myRs.next()) {
		            int index = 1;
		            ArrayList<String> innerList = new ArrayList<String>();
		            while (true) {
		                try {
		                    String temp = myRs.getString(index);
		                    innerList.add(temp);
		                    index++;
		                }catch (Exception e) {
		                    break;
		                }
		            }
		            returnList.add(innerList);
		        }
			}
			return returnList;
		}
		
		
		
		
	
	//******************************************************PATIENT******************************************************
	
	//inserts a patient into the db
	public void insertPatient(Patient patient) {
		String firstName = patient.getFirstName();
		String surname = patient.getSurname();
		String SSN = Long.toString(patient.getSSN());
		String phoneNumber=Integer.toString(patient.getNoK_cellphone());
		String email = patient.getNoK_email();
		String gender = patient.getGender();
		String deviceID = patient.getID();
		int alarmActivated = 1;
		
		update("INSERT INTO Patient(SSN, FirstName, LastName, Gender, PhoneNumber, Email, DeviceID, alarmActivated) "
            		+ "VALUES ('"+SSN+"','"+firstName+"','"+surname+"','"+gender+"',"+phoneNumber+",'"+email+"', '"+deviceID+"', "+alarmActivated+");");
    }
		
	//Retrieves patients from DB using queries
	public ArrayList<Patient> retrievePatients() throws SQLException {
		try {
			if (myConn != null) {
				myStmt = myConn.createStatement();
				myStmt.executeQuery("SELECT * FROM Patient");
				myRs = myStmt.getResultSet();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ArrayList<Patient> returnList = new ArrayList();
		if (myRs != null) {
			while (myRs.next()) {
	            int index = 1;
	            ArrayList<String> innerList = new ArrayList();
	            while (true) {
	                try {
	                    String temp = myRs.getString(index);
	                    innerList.add(temp);
	                    index++;
	                }catch (Exception e) {
	                    break;
	                }
	            }
	            Patient patient  = Patient.newPatient(innerList.get(1), innerList.get(2), innerList.get(3).charAt(0), Long.parseLong(innerList.get(0)),  
	            		Integer.parseInt(innerList.get(4)),innerList.get(5), innerList.get(6), convertFromIntToboolean(innerList.get(7)));
	            
	            returnList.add(patient);
	        }
		}
		
		for(Patient p : returnList) {
			Zone z = retrieveZone(p);
			p.addZone(z);
		}
		return returnList;
	}
	
	public void deletePatient(Patient patient) {
		String SSN = Long.toString(patient.getSSN());
		update("DELETE FROM Patient WHERE SSN = +"+SSN+"");
	}
	
	
	
	
	
	//******************************************************CARETAKER******************************************************
	
	//inserts caretaker into the db
	public boolean insertCareTaker(Caretaker caretaker) {
		String username = caretaker.getUsername();
		String password = caretaker.getPassword();
		String address = caretaker.getAddress();
		
		if(username == null || password == null) {
			System.out.println("Error from database class. Password invalid");
			return false;
		}
		update("INSERT INTO Caretaker(Username, Password, Address) "
        		+ "VALUES ('"+username+"', '"+password+"','"+address+"');");
		return true;
	}
	
	//deletes caretaker from db
	public void deleteCaretaker(Caretaker caretaker) {
		String username = caretaker.getUsername();
		update("DELETE FROM Caretaker WHERE Username = '"+username+"';");
	}
	
	public Caretaker retrieveCaretaker(Caretaker c) throws SQLException {
		ArrayList<ArrayList<String>> caretaker = query("SELECT * FROM Caretaker WHERE Username ='"+c.getUsername()+"'");
		if(caretaker.isEmpty()) {
			return null;
		}
		return new Caretaker(caretaker.get(0).get(0),caretaker.get(0).get(1),caretaker.get(0).get(2));
	}
	
	
	
	
	
	//******************************************************PATIENT-CARETAKER METHODS******************************************************
	
	public static boolean convertFromIntToboolean(String i) {
		if (i.equals("0")) {
			return false;
		}
		else {
			return true;
		}
	}
	
	//returns an array with all the patients a caretaker is connected to
	public ArrayList<Patient> retrieveCaretakersPatients(Caretaker caretaker) throws SQLException{
		String username = caretaker.getUsername();
		String queryString = "SELECT Patient.SSN, Patient.FirstName, Patient.LastName, Patient.Gender, Patient.PhoneNumber, Patient.Email, "
				+ "Patient.DeviceID, Patient.alarmActivated FROM PatientCaretaker "
				+ "JOIN Patient ON PatientCaretaker.PatSSN=Patient.SSN WHERE PatientCaretaker.CaretakerUsername='"+username+"'";
		ArrayList<ArrayList<String>> patients =  query(queryString);
		ArrayList<Patient> result = new ArrayList();
		for(int i=0; i<patients.size();i++) {
			Patient p = Patient.newPatient(patients.get(i).get(1), patients.get(i).get(2), patients.get(i).get(3).charAt(0), 
					Long.parseLong(patients.get(i).get(0)), Integer.parseInt(patients.get(i).get(4)), patients.get(i).get(5), patients.get(i).get(6), convertFromIntToboolean(patients.get(i).get(7)));
			result.add(p);
		}
		
		for(Patient p : result) {
			Zone z = retrieveZone(p);
			p.addZone(z);
		}
		return result;
	}
	
	//returns an array with all the caretakers that is connected to a patient.
	public ArrayList<Caretaker> retrievePatientsCaretakers(Patient patient) throws SQLException{
		String patientSSN = Long.toString(patient.getSSN());
		ArrayList<ArrayList<String>> caretakers =  query("SELECT Caretaker.Username, Caretaker.Password, Caretaker.Address FROM PatientCaretaker "
				+ "JOIN Caretaker ON PatientCaretaker.CaretakerUsername=Caretaker.Username WHERE PatientCaretaker.PatSSN='"+patientSSN+"'");
		ArrayList<Caretaker> result = new ArrayList();
		for(int i=0; i<caretakers.size();i++) {
			Caretaker c = new Caretaker(caretakers.get(i).get(0), caretakers.get(i).get(1), caretakers.get(i).get(2));
			result.add(c);
		}
		return result;
	}
	
	//assigns patients to caretakers
	public void assignPatientToCaretaker(Caretaker caretaker, Patient patient) {
		String patientSSN = Long.toString(patient.getSSN());
		String username = caretaker.getUsername();
		update("INSERT INTO PatientCaretaker(PatSSN, DepUsername) VALUES('"+patientSSN+"','"+username+"');");
	}
	
	//removes patient from the assigned caretaker
	public void deletePatientCaretaker(Patient patient, Caretaker caretaker) {
		String patientSSN = Long.toString(patient.getSSN());
		String caretakerUsername = caretaker.getUsername();
		update("DELETE FROM PatientCaretaker WHERE PatSSN = '"+patientSSN+"' AND CaretakerUsername = '"+caretakerUsername+"'");
	}
	
	
	
	
	
	//******************************************************ZONE******************************************************
	
	//A method that inserts a zone. All zones must be connected to a person already in the db
	public void insertZone(Patient patient, ZoneTailored zoneTailored) throws SQLException {
		String SSN = Long.toString(patient.getSSN());
		//ArrayList<ArrayList<Double>> zonePoints = zoneTailored.getPointsToDatabaseFormat();
		ArrayList<Point> points = zoneTailored.getPoints();
		int zoneID = generateZoneKey();
		update("INSERT INTO Zone(ZoneID, PatientSSN) VALUES("+zoneID+",'"+SSN+"')");
		for (int i=0;i<points.size(); i++) {
			update("INSERT INTO ZonePoint(ZonePointID, Lat, Longt, PointOrder, ZoneID, DeviceID) VALUES("+generateZonePointKey()+","
					+ ""+points.get(i).getLat()+","+points.get(i).getLongt()+", "+i+","+zoneID+", '"+points.get(i).getDeviceId()+"')");
		}
	}
	
	//deletes ALL zones a patient is connected to
	public void deleteZone(Patient patient) {
		String SSN = Long.toString(patient.getSSN());
		update("DELETE FROM Zone WHERE PatientSSN='"+SSN+"'");
	}
	
	//returns the Zone a patient is connected to. this includes all the points in the zone in the right order
	public Zone retrieveZone(Patient patient) throws SQLException {
		String patientSSN=Long.toString(patient.getSSN());
		ArrayList<ArrayList<String>> zones = query("SELECT ZonePoint.Lat, ZonePoint.Longt, ZonePoint.DeviceID FROM ZonePoint WHERE ZonePoint.ZoneID "
				+ "IN(SELECT ZoneID FROM Zone WHERE PatientSSN='"+patientSSN+"')");
		ArrayList<Point> points = new ArrayList();
		
		if(zones.size()==0) {
			return null;
		}
		
		for(int i=0; i<zones.size();i++) {
			Point p = new Point(zones.get(i).get(2), Double.parseDouble(zones.get(i).get(0)), Double.parseDouble(zones.get(i).get(1)));
			points.add(p);
			}
		Zone zone = new ZoneTailored(points);
		return zone;
	}
	
	//finds the maximum id number in the zone table.
	private int findMaxIDZone() throws SQLException {
		ArrayList<ArrayList<String>> maxID = query("SELECT MAX(ZoneID) FROM Zone");
		String id = maxID.get(0).get(0);
		if(id==null) {
			return 0;
		}
		return Integer.parseInt(id);
	}
	
	//finds the maximum id number in the zonePoint table.
	private int findMaxIDZonePoint() throws SQLException {
		ArrayList<ArrayList<String>> maxID = query("SELECT MAX(ZonePointID) FROM ZonePoint");
		String id = maxID.get(0).get(0);
		if(id==null) {
			return 0;
		}
		return Integer.parseInt(id);
	}
	
	//generates a key for a ZonePoint based on the maxId already in the db
	private int generateZonePointKey() throws SQLException {
		int maxKey=findMaxIDZonePoint();
		return maxKey+=1;
	}
	
	//generates a key for a Zone based on the maxId already in the db
	private int generateZoneKey() throws SQLException {
		int maxKey=findMaxIDZone();
		return maxKey+=1;
	}
	
	
	
	
	
	//******************************************************ALARM******************************************************
	
	//activates the alarm for a patient
	public void activateAlarmActivated(Patient patient) {
		String SSN = Long.toString(patient.getSSN());
		update("UPDATE Patient SET alarmActivated = 1 WHERE SSN ='"+SSN+"'");
	}
	
	//deactivates the alarm for a patient
	public void deactivateAlarmActivated(Patient patient) {
		String SSN = Long.toString(patient.getSSN());
		update("UPDATE Patient SET alarmActivated = 0 WHERE SSN ='"+SSN+"'");
	}
	
		
		
		
	
	//******************************************************FILE******************************************************
	
	//this method inserts txt-files in the db
	public void insertFile(int fileKey, String filename) throws FileNotFoundException {
		FileHandler fh = new FileHandler();
		String output = fh.read(filename);
		update("INSERT INTO File(fileKey, text) VALUES("+fileKey+",'"+output+"')");
	}
	
	//retrieves txt-files from the db, returns a string
	public String retrieveFile(int fileKey) throws SQLException {
		return query("SELECT text FROM File WHERE fileKey="+fileKey).get(0).get(0);
	}
	
	
	
	
	
	//******************************************************LOGIN******************************************************
	
	//checks if the password for the username is correct. If it is, the method returns the username 
	//If the username don't exist or the password is wrong, the method returns null.
	public String checkPassword(String username, String inputPassword) throws SQLException {
		ArrayList<ArrayList<String>> caretaker = query("SELECT * FROM Caretaker WHERE Username ='"+username+"'");
		if(caretaker.isEmpty()) {
			return null;
		}
		String password=caretaker.get(0).get(1);
		if(password.equals(inputPassword)) {
			return username;
		}
		return null;
	}
	
	
	
	
	//main
	public static void main(String[] args) throws SQLException, FileNotFoundException {
		Patient p1 = Patient.newPatient("Harald", "Bach", 'M', 12345678919l, 90887878, "harald@gmail.com","id1", true);
		//Caretaker c1 = new Caretaker("motherofthree","Saga123@1","Jordmorjordet 1");
		//Caretaker c2 = new Caretaker("iceroadtruckerfan","beef&Burger3","Rallarveien 3");
		
		Point point1 = new Point("deviceID3",225.56,347.12345678911234567891);
		Point point2 = new Point("deviceID3",223.56,323.89999);
		Point point3 = new Point("deviceID3",227.56,389.89999);
		Point point4 = new Point("deviceID3",221.56,312.89999);
		ArrayList<Point> arr = new ArrayList();
		arr.add(point1);
		arr.add(point2);
		arr.add(point3);
		arr.add(point4);
		ZoneTailored zone = new ZoneTailored(arr);
		
		Database db = new Database();
		db.connect();
		System.out.println(db.retrievePatients());
		
		
	}
}


