package tdt4140.gr1814.app.core;

import java.util.ArrayList;
import java.util.Properties;
import java.sql.*;

public class Database {
	
	private Connection myConn;
	private Statement myStmt;
	private ResultSet myRs=null;
	
	//TODO: Make the class take a person object as input and write it to the DB.
	
	//connecting to db
	public void connect() {
		try {
			myConn = DriverManager.getConnection("jdbc:mysql://mysql.stud.ntnu.no:3306/hara_database","hara_db","gruppe14");
		
		}catch (Exception ex){
				ex.printStackTrace();
			}
		}
	
	//Retrieve data from DB using queries
	public ArrayList<Patient> retrievePatients() throws SQLException {
		try {
			myStmt = myConn.createStatement();
			myStmt.executeQuery("SELECT * FROM Patient");
			myRs = myStmt.getResultSet();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		ArrayList<Patient> returnList = new ArrayList();
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
		return returnList;
	}
	
	
	public void insert(Patient patient) {
		String firstName = patient.getFirstName();
		String surname = patient.getSurname();
		String SSN = Long.toString(patient.getSSN());
		String phoneNumber=Integer.toString(patient.getNoK_cellphone());
		String email = patient.getNoK_email();
		String gender = patient.getGender();
		String deviceID = patient.getID();
		
        try {
            myStmt = myConn.createStatement();
            myStmt.executeUpdate("INSERT INTO Patient(SSN, FirstName, LastName, Gender, PhoneNumber, Email, DeviceID) "
            		+ "VALUES ('"+SSN+"','"+firstName+"','"+surname+"','"+gender+"',"+phoneNumber+",'"+email+"', '"+deviceID+"');");
            System.out.println("Success.");
        } catch (Exception e) {
            System.out.println("The insert query failed. Check your sql syntax.");
        }
    }
	
	public void delete(Patient patient) {
		String SSN = Long.toString(patient.getSSN());
		try {
            myStmt = myConn.createStatement();
            myStmt.executeUpdate("DELETE FROM Patient WHERE SSN = +"+SSN+"");
            System.out.println("Success.");
        } catch (Exception e) {
            System.out.println("The delete query failed. Check your sql syntax.");
        }
	}
	
	public void update(String query){
		// example input: "UPDATE Patient SET FirstName='Fjotolf' WHERE SSN=123123"
		
		try {
            myStmt = myConn.createStatement();
            myStmt.executeUpdate(query);
            System.out.println("Success.");
        } catch (Exception e) {
            System.out.println("The query failed. Check your sql syntax.");
        }
		
	}
	
	
	
	public static void main(String[] args) throws SQLException {
		
		Patient p1 = Patient.newPatient("Harald", "Bach", 'M', 12345678919l, 90887878, "harald@gmail.com","id1");
		Patient p2 = Patient.newPatient("Hennie", "Sørensen", 'F', 99345678910l, 34534534, "hennie@gmail.com","id2");
		
		
		
		Database db = new Database();
		db.connect();
		db.insert(p1);
		System.out.println(db.retrievePatients());
		
		
	}
}

