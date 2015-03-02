package com.team1.chat.models;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

import com.team1.chat.interfaces.DatabaseSupportInterface;

public class DatabaseSupport implements DatabaseSupportInterface
{	Connection conn;
	public DatabaseSupport(){
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
        return false;
    }

    public User getUser(String username, String password)
    {
        return null;
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
