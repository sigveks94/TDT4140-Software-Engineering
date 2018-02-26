package tdt4140.gr1814.app.core;

import java.util.ArrayList;
import java.util.Properties;
import java.sql.*;

public class Database {
	
	private Connection myConn;
	private Statement myStmt;
	private ResultSet myRs=null;
	
	//TODO: Make the class take a person object as input and write it to the DB.
	
	//Get a connection to database
	public void connect() {
		try {
			myConn = DriverManager.getConnection("jdbc:mysql://mysql.stud.ntnu.no:3306/hara_database","hara_db","gruppe14");
		
		}catch (Exception ex){
				ex.printStackTrace();
			}
		}
	
	//Retrieve data from DB using queries
	public ArrayList<ArrayList<String>> retrieve(String query) throws SQLException {
		try {
			myStmt = myConn.createStatement();
			myStmt.executeQuery(query);
			myRs = myStmt.getResultSet();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		ArrayList<ArrayList<String>> returnList = new ArrayList();
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
            returnList.add(innerList);
        }
        System.out.println(returnList);
		return returnList;
	}
	
	//the insert, delete and update methods are all similar now. Once I know more about the Pasient class I
	//think it would be clever to execute the sql query inside the method.
	
	
	public void insert(Patient patient) {
		//example input: "INSERT INTO Patient(SSN, FirstName, LastName) VALUES (999999,'Mathias','Kroken');"
		String firstName = patient.getFirstName();
		String surname = patient.getSurname();
		String SSN = Long.toString(patient.getSSN());
		String phoneNumber=Integer.toString(patient.getNoK_cellphone());
		String email = patient.getNoK_email();
		String gender = patient.getGender();
		System.out.println("INSERT INTO Patient(SSN, FirstName, LastName, Gender, PhoneNumber, Email) "
				+ "VALUES ("+SSN+",'"+firstName+"','"+surname+"','"+gender+"',"+phoneNumber+",'"+email+"');");
		
        try {
            myStmt = myConn.createStatement();
            myStmt.executeUpdate("INSERT INTO Patient(SSN, FirstName, LastName, Gender, PhoneNumber, Email) "
            		+ "VALUES ("+SSN+",'"+firstName+"','"+surname+"','"+gender+"',"+phoneNumber+",'"+email+"');");
            System.out.println("Success.");
        } catch (Exception e) {
            System.out.println("The query failed. Check your sql syntax.");
        }
    }
	
	public void delete(Patient patient) {
	// example input: "DELETE FROM Patient WHERE SSN = 999999;"
		String SSN = Long.toString(patient.getSSN());
		try {
            myStmt = myConn.createStatement();
            myStmt.executeUpdate("DELETE FROM Patient WHERE SSN = +"+SSN+"");
            System.out.println("Success.");
        } catch (Exception e) {
            System.out.println("The query failed. Check your sql syntax.");
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
		
		Patient p1 = new Patient("Harald", "Bach", 'M', 1234567891l, 90887878, "heihei@gmail.com");
		
		
		
		Database db = new Database();
		db.connect();
		db.insert(p1);
		db.retrieve("select * from Patient");
		db.delete(p1);
		db.retrieve("select * from Patient");
		
	}
}

