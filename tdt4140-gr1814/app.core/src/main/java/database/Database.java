package database;

import java.util.ArrayList;
import java.util.Properties;
import java.sql.*;

public class Database {
	
	Connection myConn;
	Statement myStmt;
	ResultSet myRs=null;
	
	public void connect() {
		try {
			//1. Get a connection to database
			myConn = DriverManager.getConnection("jdbc:mysql://mysql.stud.ntnu.no:3306/DATABASE NAME","USERNAME","PASSWORD");
		
		}catch (Exception ex){
				ex.printStackTrace();
			}
		}
	
	
	public void retrieve(String query) throws SQLException {
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
                } catch (Exception e) {
                    break;
                }
            }
            returnList.add(innerList);
        }
        System.out.println(returnList);
		
	}
	
	//the insert, delete and update methods are all similar now. Once I know more about the Pasient class I
	//think it would be clever to execute the sql query inside the method.
	public void insert(String query) {
		//example input: "INSERT INTO Pasient(SSN, FirstName, LastName) VALUES (999999,'Mathias','Kroken');"
		
        try {
            myStmt = myConn.createStatement();
            myStmt.executeUpdate(query);
            System.out.println("Success.");
        } catch (Exception e) {
            System.out.println("The query failed. Check your sql syntax.");
        }
    }
	
	public void delete(String query) {
	// example input: "DELETE FROM Pasient WHERE SSN = 999999;"
		try {
            myStmt = myConn.createStatement();
            myStmt.executeUpdate(query);
            System.out.println("Success.");
        } catch (Exception e) {
            System.out.println("The query failed. Check your sql syntax.");
        }
	}
	
	public void update(String query){
		// example input: "UPDATE Pasient SET FirstName='Fjotolf' WHERE SSN=123123"
		
		try {
            myStmt = myConn.createStatement();
            myStmt.executeUpdate(query);
            System.out.println("Success.");
        } catch (Exception e) {
            System.out.println("The query failed. Check your sql syntax.");
        }
		
	}
	
	
	
	public static void main(String[] args) throws SQLException {
		
		Database db = new Database();
		db.connect();
		db.update("UPDATE Pasient SET FirstName='Mons' WHERE SSN=123123;");
		db.retrieve("select * from Pasient");
		
	}
}

