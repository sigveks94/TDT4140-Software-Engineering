package tdt4140.gr1814.webserver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ConnectionHandler {
	
	private Connection myConn;
	private Statement myStmt;
	private ResultSet myRs=null;
	
	
	//connecting to the db
	public void connect() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		myConn = DriverManager.getConnection("jdbc:mysql://mysql.stud.ntnu.no:3306/hara_database?autoReconnect=true&useSSL=false","hara_db","gruppe14");
	}
	
	
	
	//a general method for updating the db, used as a help method.
	public boolean update(String query) {
		try {
            myStmt = myConn.createStatement();
            myStmt.executeUpdate(query);
        } catch (Exception e) {
            System.out.println("The update query failed. Check your sql syntax.");
            e.printStackTrace();
            return false;
        }
		return true;
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
}
