package com.team1.chat.models;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

import com.team1.chat.interfaces.DatabaseSupportInterface;

public class DatabaseSupport implements DatabaseSupportInterface
{	
	Connection conn;
	public DatabaseSupport()
	{
		try {   
	         // Load the driver (registers itself)
	         Class.forName ("com.mysql.jdbc.Driver");
	         } 
	    catch (Exception E) {
	            System.err.println ("Unable to load driver.");
	            E.printStackTrace ();
	    }
	    String dbUrl = "jdbc:mysql://104.236.206.121:3306/chat";
	    String user = "root";
	    String password = "362team1";
	    try {
			conn = DriverManager.getConnection (dbUrl, user, password);
	    } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	    }
	    System.out.println ("*** Connected to the database ***"); 
	}

	private boolean setData(String statement)
	{
		//Simply submit statement to the MySQL server.
		try {
			PreparedStatement stmt = conn.prepareStatement(statement);
			stmt.executeUpdate();
			stmt.close();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	//Each index of the string list represents a row. 
	//Each line in a string represents a column.
	private ArrayList<String> getData(String statement){
		Statement stmt;
		ResultSet rs;
		ResultSetMetaData rsmd;
		ArrayList<String> data = new ArrayList<String>();
		try {
			//Create Statement
			stmt = conn.createStatement();
			//Execute Query and get ResultSet
			rs = stmt.executeQuery(statement);
			//Get number of columns in a row.
			rsmd = rs.getMetaData();
			int numColumns = rsmd.getColumnCount();
			
			for (int i = 0;rs.next();i++) {
				String row = "";
				for (int j = 0; j < numColumns;j++)
				{
					row = row + rs.getString(i)+"\n";
				}
				data.add(row);
			}
			stmt.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}
    public boolean putUser(User u)
    {
    	String statement = "INSERT INTO User " +
    					   "VALUES(DEFAULT,"+u.getUsername()+","+u.getPassword()+")";
        return setData(statement);
    }

    public User getUser(String username, String password)
    {
    	String statement = "SELECT * " +
    					   "FROM User u " +
    					   "WHERE u.username = "+username+ 
    					   "AND u.password = "+ password;
    	ArrayList<String> result = getData(statement);
    	if (!(result.size()==3))
    	{
    		//First column: uid
    		String uid = result.get(0);
    	
    		//Second column: username
    		String uname = result.get(1);
    		
    		//Third column: password
    		String pw = result.get(2);
    		
    		User u = new User(uid,uname,pw);
    		
    		return u;
    	}
    	else return null;
		
    }

    public User getUser(String uid)
    {
        return null;
    }

    public boolean nameAvailable(String newUsername)
    {
        return false;
    }

    public Channel getChannel(String cid)
    {
        return null;
    }

    public boolean putChannel(Channel c)
    {
        return false;
    }
}
