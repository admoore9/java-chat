package com.team1.chat.models;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

import com.team1.chat.interfaces.DatabaseSupportInterface;

public class DatabaseSupport implements DatabaseSupportInterface
{	
	Connection conn;
	
	/**
	 * Loads the driver and initializes the connection to the database.
	 */
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
	
	/**
	 * Connection should be closed whenever the program exits. 
	 */
	public void close(){
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Helper method to send an update to database.
	 * @param statement
	 * @return
	 */
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
	
	/**
	 * Helper method to pull data from the database.
	 * @param statement
	 * @return Returns an ArrayList<String>. 
	 * 		   Each string denotes a row. 
	 * 		   Each line of a string denotes a column value.
	 */
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
			while (rs.next()) {
				String rowStr = "";
				for (int j = 1; j <= numColumns;j++)
				{
					//Object rowObj = rs.getObject(j);
					//if (rowObj!=null){
					//	rowStr = rowStr + (String) rowObj + "\n";
					//}
					rowStr = rowStr + rs.getString(j)+"\n";
				}
				data.add(rowStr);
			}
			stmt.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}
	/**
	 * Puts a new User into the database. 
	 */
    public boolean putUser(User u)
    {
    	String statement = "INSERT INTO User " +
    					   "VALUES(DEFAULT,'" + u.getUsername()+"','"+u.getPassword()+"')";
        return setData(statement);
    }

    /**
     * Returns a User from the database by username and password.
     */
    public User getUser(String username, String password)
    {
    	String statement = "SELECT * " +
    					   "FROM User u " +
    					   "WHERE u.username = '"+username+ 
    					   "'AND u.password = '"+ password+"'";
    	ArrayList<String> result = getData(statement);
    	if (result.size()==1)
    	{
    		Scanner scanForColumnValues = new Scanner(result.get(0));
    		
    		//First column: uid
    		String uid = scanForColumnValues.nextLine();
    		
    		//Second column: username
    		String uname = scanForColumnValues.nextLine();
    		
    		//Third column: password
    		String pw = scanForColumnValues.nextLine();
    		
    		User u = new User(uid,uname,pw);
    		
    		scanForColumnValues.close();
    		return u;
    	}
    	else return null;
		
    }

    /**
     * Returns a User from the database by user id.
     */

	public User getUserById(String uid) {
		String statement = "SELECT * " + "FROM User u " + "WHERE u.uid ="+uid;
		ArrayList<String> result = getData(statement);
		
    	if (result.size()==1)
    	{
    		Scanner scanForColumnValues = new Scanner(result.get(0));
    		//First column: uid
    		String userid = scanForColumnValues.nextLine();

    		
    		//Second column: username
    		String uname = scanForColumnValues.nextLine();

    		
    		//Third column: password
    		String pw = scanForColumnValues.nextLine();

    		User u = new User(userid,uname,pw);
    		
    		scanForColumnValues.close();
    		return u;
    	}
    	else return null;
	}

    public boolean nameAvailable(String newUsername)
    {
    	ArrayList<String> result = getData("SELECT * FROM User u WHERE u.username = '"+newUsername+"'");
        return (result.size()==0);
    }

    public Channel getChannelByName(String name)
    {
		String statement = "SELECT * " + "FROM Channel c " + "WHERE c.name ='"+name+"'";
		ArrayList<String> result = getData(statement);
		
		if (result.size() == 1)
		{
			Scanner scanForColumnValues = new Scanner(result.get(0));
			
			// First column: name
			String channelName = scanForColumnValues.nextLine();

			// Second column: ispublic
			Boolean isPublic;
			String visibility = scanForColumnValues.nextLine();
			if (visibility != "0"){
				isPublic=true;
			}
			else isPublic = false;
			
			// Third column: admin
			String admin = scanForColumnValues.nextLine();
			
			// Fourth Column: whitelist
			// 	This could've been implemented better. Should prob change the format for lists on the database to 
			// 	json or something, instead of how I have it now. 
			ArrayList<User> whitelist=new ArrayList<User>();		
			while (scanForColumnValues.hasNextLine())
			{
				// This might need some error handling. Not sure. 
				whitelist.add(getUser(scanForColumnValues.nextLine()));
			}
			scanForColumnValues.close();
			Channel c = new Channel(channelName, isPublic, admin, whitelist);
			System.out.println("Channel was successfully retrieved from database.");
			return c;
		} else
			return null;
    }

    public boolean putChannel(Channel c)
    {
    	String wList = "";
    	ArrayList<User> tempList = c.getWhiteList();
    	for (int i = 0; i < tempList.size();i++)
    	{
    		wList = wList + tempList.get(i).getId() + "\n";
    	}
    	int val = c.isPublic() ? 1 : 0;
    	String isPublic = String.valueOf(val);
    	String statement = "INSERT INTO Channel " +
				   			"VALUES('"+c.getName()+"','"+isPublic+"','"+c.getAdminId()+"','"+wList+"')";
    	System.out.println("Channel was successfully added to database.");
    	return setData(statement);
    }

    public User getUserByName(String uname)
    {
        return null;
    }

    public boolean deleteChannel(String name)
    {
        return false;
    }
}
