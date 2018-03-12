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
	
	
	//connecting to the db
	public void connect() {
		try {
			myConn = DriverManager.getConnection("jdbc:mysql://mysql.stud.ntnu.no:3306/hara_database?autoReconnect=true&useSSL=false","hara_db","gruppe14");
		}catch (Exception ex){
				ex.printStackTrace();
			}
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
		ArrayList<Patient> returnList = new ArrayList(); //we never use this list..
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
	            Patient patient  = Patient.newPatient(innerList.get(1), innerList.get(2), innerList.get(3).charAt(0), Long.parseLong(innerList.get(0)),  Integer.parseInt(innerList.get(4)),innerList.get(5), innerList.get(6));
	            returnList.add(patient);
	        }
		}
		return returnList;
	}
	
	//inserts a patient into the db
	public void insertPatient(Patient patient) {
		String firstName = patient.getFirstName();
		String surname = patient.getSurname();
		String SSN = Long.toString(patient.getSSN());
		String phoneNumber=Integer.toString(patient.getNoK_cellphone());
		String email = patient.getNoK_email();
		String gender = patient.getGender();
		String deviceID = patient.getID();
		
		update("INSERT INTO Patient(SSN, FirstName, LastName, Gender, PhoneNumber, Email, DeviceID) "
            		+ "VALUES ('"+SSN+"','"+firstName+"','"+surname+"','"+gender+"',"+phoneNumber+",'"+email+"', '"+deviceID+"');");
    }
	
	public void deletePatient(Patient patient) {
		String SSN = Long.toString(patient.getSSN());
		update("DELETE FROM Patient WHERE SSN = +"+SSN+"");
	}
	
	//a general method for updating the db, used as a help method.
	public void update(String query) {
		try {
            myStmt = myConn.createStatement();
            myStmt.executeUpdate(query);
        } catch (Exception e) {
            System.out.println("The update query failed. Check your sql syntax.");
            e.printStackTrace();
        }
	}
	
	//a general method for querying the db, used as a help method
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
	
	// getters for testing
	public Connection getConnection() {
		return myConn;
	}
	
	public Statement getStatement() {
		return myStmt;
	}
	
	public ResultSet getResultSet() {
		return myRs;
	}
	
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
	
	//returns an array with all the patients a caretaker is connected to
	//TODO: Maybe make this return patient objects
	public ArrayList<Patient> retrieveCaretakersPatients(Caretaker caretaker) throws SQLException{
		String username = caretaker.getUsername();
		String queryString = "SELECT Patient.SSN, Patient.FirstName, Patient.LastName, Patient.Gender, Patient.PhoneNumber, Patient.Email, Patient.DeviceID FROM PatientCaretaker "
				+ "JOIN Patient ON PatientCaretaker.PatSSN=Patient.SSN WHERE PatientCaretaker.CaretakerUsername='"+username+"'";
		ArrayList<ArrayList<String>> patients =  query(queryString);
		ArrayList<Patient> result = new ArrayList();
		for(int i=0; i<patients.size();i++) {
			Patient p = Patient.newPatient(patients.get(i).get(1), patients.get(i).get(2), patients.get(i).get(3).charAt(0), Long.parseLong(patients.get(i).get(0)), Integer.parseInt(patients.get(i).get(4)), patients.get(i).get(5), patients.get(i).get(6));
			result.add(p);
		}
		return result;
	}
	
	//returns an array with all the caretakers that is connected to a patient.
	//TODO: Make this return cartaker objects
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
	
	//A method that inserts a zone. All zones must be connected to a person already in the db
	public void insertZone(Patient patient, ZoneTailored zoneTailored) throws SQLException {
		String SSN = Long.toString(patient.getSSN());
		ArrayList<ArrayList<Double>> zonePoints = zoneTailored.getPointsToDatabaseFormat();
		int zoneID = generateZoneKey();
		update("INSERT INTO Zone(ZoneID, PatientSSN) VALUES("+zoneID+",'"+SSN+"')");
		for (int i=0;i<zonePoints.size(); i++) {
			update("INSERT INTO ZonePoint(ZonePointID, Lat, Longt, PointOrder, ZoneID) VALUES("+generateZonePointKey()+","+zonePoints.get(i).get(0)+","+zonePoints.get(i).get(1)+", "+i+","+zoneID+")");
		}
	}
	
	//deletes ALL zones a patient is connected to
	public void deleteZone(Patient patient) {
		String SSN = Long.toString(patient.getSSN());
		update("DELETE FROM Zone WHERE PatientSSN='"+SSN+"'");
	}
	
	//returns the Zone a patient is connected to. this includes all the points in the zone in the right order
	public ArrayList<ArrayList<Double>> retrieveZone(Patient patient) throws SQLException {
		String patientSSN=Long.toString(patient.getSSN());
		ArrayList<ArrayList<String>> zones = query("SELECT ZonePoint.Lat, ZonePoint.Longt FROM ZonePoint WHERE ZonePoint.ZoneID IN(SELECT ZoneID FROM Zone WHERE PatientSSN='"+patientSSN+"')");
		ArrayList<ArrayList<Double>> result = new ArrayList();
		for(int i=0; i<zones.size();i++) {
			ArrayList<Double> latLongs = new ArrayList();
			latLongs.add(Double.parseDouble(zones.get(i).get(0)));
			latLongs.add(Double.parseDouble(zones.get(i).get(1)));
			result.add(latLongs);
			}
		return result;
	}
	
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
	
	public Caretaker checkPassword(String username, String inputPassword) throws SQLException {
		ArrayList<ArrayList<String>> caretaker = query("SELECT * FROM Caretaker WHERE Username ='"+username+"'");
		if(caretaker.isEmpty()) {
			return null;
		}
		String password=caretaker.get(0).get(1);
		if(password.equals(inputPassword)) {
			return new Caretaker(caretaker.get(0).get(0),password,caretaker.get(0).get(2));
		}
		return null;
	}
	
	public static void main(String[] args) throws SQLException, FileNotFoundException {
		Patient p1 = Patient.newPatient("Harald", "Bach", 'M', 12345678919l, 90887878, "harald@gmail.com","id1");
		Caretaker c1 = new Caretaker("motherofthree","Saga123@1","Jordmorjordet 1");
		Caretaker c2 = new Caretaker("iceroadtruckerfan","beef&Burger3","Rallarveien 3");
		Database db = new Database();
		db.connect();
		
		System.out.println(db.insertCareTaker(c2));
		
	}
}


