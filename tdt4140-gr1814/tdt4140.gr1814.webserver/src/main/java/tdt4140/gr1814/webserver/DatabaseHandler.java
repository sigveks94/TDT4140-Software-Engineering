package tdt4140.gr1814.webserver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*
 * This class will handle all traffic between the web server and the database
 * All methods throws SQLException, if a method does not throw an exception the query is interpreted as successfull
 * The methods throws exceptions because the method calling knows better how to handle the exception
 */
public class DatabaseHandler {
	
	//The connection to the database is set up on instantiation
	private Connection connection;
	
	public DatabaseHandler() throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
		connection = DriverManager.getConnection("jdbc:mysql://mysql.stud.ntnu.no:3306/hara_database?autoReconnect=true&useSSL=false","hara_db","gruppe14");
	}
	
	//Method for handling queries that will return a result
	public ResultSet query(String query) throws SQLException {
		Statement statement = this.connection.createStatement();
		statement.executeQuery(query);
		return statement.getResultSet();
	}
	
	//Method for handling queries that will do update on a table, no result returned
	public void update(String query) throws SQLException{
		Statement statement = this.connection.createStatement();
		statement.executeUpdate(query);
	}

}
