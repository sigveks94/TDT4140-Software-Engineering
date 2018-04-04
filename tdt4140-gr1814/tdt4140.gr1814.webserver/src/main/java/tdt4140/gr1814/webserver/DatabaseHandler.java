package tdt4140.gr1814.webserver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

//The database handler handles all traffic between the web server and the database
public class DatabaseHandler {
	
	private Connection connection;
	
	public DatabaseHandler() throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
		connection = DriverManager.getConnection("jdbc:mysql://mysql.stud.ntnu.no:3306/hara_database?autoReconnect=true&useSSL=false","hara_db","gruppe14");
	}
	
	public ResultSet query(String query) throws SQLException {
		Statement statement = this.connection.createStatement();
		statement.executeQuery(query);
		return statement.getResultSet();
	}
	
	public void update(String query) throws SQLException{
		Statement statement = this.connection.createStatement();
		statement.executeUpdate(query);
	}

}
