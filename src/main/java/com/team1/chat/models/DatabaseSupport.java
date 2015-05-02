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
	    //Get default channel.
    	Channel defaultChannel = getChannelByName("testCH1");
    	//MySQL statement to get all userids from User table.
		String statement = "SELECT uid " + "FROM User u";
		//Execute query.
		ArrayList<String> result = getData(statement);
		//Parse the result of query.
		for (int i = 0; i < result.size();i++){
			Scanner scanForColumnValues = new Scanner(result.get(i));
			// Get id.
    		String id = scanForColumnValues.nextLine();
    		// Get corresponding user.
    		User u = getUserById(id);
    		// Attempt to add each user to default channel's whitelist.
    		// System.out.println("userid: "+id);
    		if(defaultChannel.whiteListUser("33", u)){
    		//		System.out.println("User successfully added to whitelist.");
    		}
    		else {
    		//	System.out.println("User unsuccessfully added to whitelist.");
    		}
			scanForColumnValues.close();
		}
		putChannel(defaultChannel);
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
    	String statement;
    	String fList = userListToString(u.getFriends());
    	String bList = userListToString(u.getBlockedUsers());
    	
    	User thisUser = getUserById(u.getId());
    	
//    	if (thisUser!=null){
//    		statement = "UPDATE User "+
//    						"SET username='"+u.getUsername()+"',"+
//    							"password='"+u.getPassword()+"'"+
//    						"WHERE uid='"+u.getId()+"'";
//    	}
//    	else {statement = "INSERT INTO User " +
//    					   "VALUES(DEFAULT,'" + u.getUsername()+"','"+u.getPassword()+"')";
//    	}
    	if (thisUser!=null){
    		statement = "UPDATE User "+
    						"SET username='"+u.getUsername()+"',"+
    							"password='"+u.getPassword()+"',"+
    							"friendlist='"+fList+"',"+
    							"blocklist='"+bList+"' "+
    						"WHERE uid='"+u.getId()+"'";
    	}
    	else {statement = "INSERT INTO User " +
    					   "VALUES(DEFAULT,'" + u.getUsername()+"','"+u.getPassword()+"','"+fList+"','"+bList+"')";
    	}
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
//    	if (result.size()==1)
//    	{
//    		Scanner scanForColumnValues = new Scanner(result.get(0));
//    		
//    		//First column: uid
//    		String uid = scanForColumnValues.nextLine();
//    		
//    		//Second column: username
//    		String uname = scanForColumnValues.nextLine();
//    		
//    		//Third column: password
//    		String pw = scanForColumnValues.nextLine();
//    		
//    		User u = new User(uid,uname,pw);
//    		
//            //System.out.println("User was successfully retrieved from database.");
//    		scanForColumnValues.close();
//    		return u;
//    	}
//    	else return null;
		return parseUserData(result);
    }

    /**
     * Returns a User from the database by user id.
     */

	public User getUserById(String uid) {
		if (uid==null){
			return null;
		}
		String statement = "SELECT * " + "FROM User u " + "WHERE u.uid ='"+uid+"'";
		ArrayList<String> result = getData(statement);
		
//    	if (result.size()==1)
//    	{
//    		Scanner scanForColumnValues = new Scanner(result.get(0));
//    		//First column: uid
//    		String userid = scanForColumnValues.nextLine();
//
//    		
//    		//Second column: username
//    		String uname = scanForColumnValues.nextLine();
//
//    		
//    		//Third column: password
//    		String pw = scanForColumnValues.nextLine();
//
//    		User u = new User(userid,uname,pw);
//    		
//    		scanForColumnValues.close();
//    		return u;
//    	}
//    	else return null;
		return parseUserData(result);
	}
	
	private User parseUserData(ArrayList<String> result)
	{    	
		if (result.size()==1){
			Scanner scanForColumnValues = new Scanner(result.get(0));
			
			//First column: uid
			String uid = scanForColumnValues.nextLine();
			
			//Second column: username
			String uname = scanForColumnValues.nextLine();
			
			//Third column: password
			String pw = scanForColumnValues.nextLine();
			
			User u = new User(uid,uname,pw);
	        
	        // Fourth Column: friendlist
	        while (scanForColumnValues.hasNextLine())
	        {
	        	String id = scanForColumnValues.nextLine();
	        	if (!id.equals("0"))
	        	{
	            	User friend = getUserById(id);
	            	if (friend==null){
	            		//System.out.println("parseUserData(): User["+id+"] could not be found.");
	            		continue;
	            	}
	                if (u.addFriend(friend)){
	                	//System.out.println("parseUserData(): Successfully added user["+id+"] to friend list.");
	                }
	                else {
	                	//System.out.println("parseUserData(): Failed to add user["+id+"] to friend list.");
	                }
	        	}
	        	else{
	        		//System.out.println("At end of friendlist column.");
	        		break;
	        	}
	        }
	
	        // Fifth Column: blocklist
	        while (scanForColumnValues.hasNextLine())
	        {
	        	String id = scanForColumnValues.nextLine();
	        	if (id.isEmpty()){
	        		continue;
	        	}
	        	if (!id.equals("0"))
	        	{
	            	User blocked = getUserById(id);
	            	if (blocked==null){
	            		//System.out.println("parseUserData(): User["+id+"] could not be found.");
	            		continue;
	            	}
	                if (u.blockUser(blocked)){
	                	//System.out.println("parseUserData(): Successfully added user["+id+"] to blocklist.");
	                }
	                else {
	                	//System.out.println("parseUserData(): Failed to add user["+id+"] to blocklist.");
	                }
	        	}
	        	else {
	        		//System.out.println("At end of blocklist column.");
	        		break;
	        	}
	        }
	        //System.out.println("User was successfully retrieved from database.");
			scanForColumnValues.close();
			return u;
		}
		return null;
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
            
            // Instantiate channel so that it can modified 
            
            Channel c = new Channel(channelName, admin);
            
            // Fourth Column: whitelist
            // 	This could've been implemented better. Should prob change the format for lists on the database to
            // 	json or something, instead of how I have it now.
            
            while (scanForColumnValues.hasNextLine())
            {
            	String id = scanForColumnValues.nextLine();
            	if (!id.equals("0"))
            	{
	            	User u = getUserById(id);
	            	if (u==null){
	            		//System.out.println("getChannelByName(): User["+id+"] could not be found.");
	            		continue;
	            	}
	                if (c.whiteListUser(admin, u)){
	                	//System.out.println("getChannelByName(): Successfully added user["+id+"] to whitelist.");
	                }
	                else {
	                	//System.out.println("getChannelByName(): Failed to add user["+id+"] to whitelist.");
	                }
            	}
            	else{
            		//System.out.println("At end of whitelist column.");
            		break;
            	}
            }

            // Fifth Column: currentlist
            while (scanForColumnValues.hasNextLine())
            {
            	String id = scanForColumnValues.nextLine();
            	if (id.isEmpty()){
            		continue;
            	}
            	if (!id.equals("0"))
            	{
	            	User u = getUserById(id);
	            	if (u==null){
	            		//System.out.println("getChannelByName(): User["+id+"] could not be found.");
	            		continue;
	            	}
	                if (c.addChannelUser(u)){
	                	//System.out.println("getChannelByName(): Successfully added user["+id+"] to currentlist.");
	                }
	                else {
	                	//System.out.println("getChannelByName(): Failed to add user["+id+"] to currentlist.");
	                }
            	}
            	else {
            		//System.out.println("At end of currentlist column.");
            		break;
            	}
            }
            scanForColumnValues.close();
            //System.out.println("Channel was successfully retrieved from database.");
            return c;
        } else
            return null;
    }

    public boolean putChannel(Channel c)
    {
    	
    	String statement;
//    	String wList = "";
//    	ArrayList<User> tempList = c.getWhiteList();
//    	int i;
//    	for (i = 0; i < tempList.size();i++)
//    	{
//    		wList = wList + tempList.get(i).getId() + "\n";
//    	}
//    	wList = wList + "0\n";
    	String wList = userListToString(c.getWhiteList());
    	
//    	String cList="";
//    	tempList = c.getCurrentUsers();
//    	for (i=0; i < tempList.size();i++)
//    	{
//    		cList = cList + tempList.get(i).getId()+"\n";
//    	}
//    	cList = cList + "0\n";
    	String cList = userListToString(c.getCurrentUsers());
    	
    	int val = c.isPublic() ? 0 : 1;
    	String isPublic = String.valueOf(val);
    	Channel thisChannel = getChannelByName(c.getName());
    	if (thisChannel!=null){
    		statement = "UPDATE Channel "+
    						"SET name='"+c.getName()+"',"+
    							"ispublic='"+isPublic+"',"+
    							"admin='"+c.getAdminId()+"',"+
    							"whitelist='"+wList+"',"+
    							"currentlist='"+cList+"'"+
    						"WHERE name='"+c.getName()+"'";
    	}
    	else {statement = "INSERT INTO Channel " +
	   			"VALUES('"+c.getName()+"','"+isPublic+"','"+c.getAdminId()+"','"+wList+"+','"+cList+"')";
    	}
        return setData(statement);
    }

    public User getUserByName(String uname)
    {
    	String statement = "SELECT * " +
				   "FROM User u " +
				   "WHERE u.username = '"+uname+"'";
		ArrayList<String> result = getData(statement);
		if (result.size() == 1) {
			Scanner scanForColumnValues = new Scanner(result.get(0));

			// First column: uid
			String uid = scanForColumnValues.nextLine();

			// Second column: username
			String username = scanForColumnValues.nextLine();

			// Third column: password
			String pw = scanForColumnValues.nextLine();

			User u = new User(uid, username, pw);

			scanForColumnValues.close();
			return u;
		} else
			return null;
    }

    public boolean deleteChannel(String name)
    {
        String statement = "DELETE FROM Channel WHERE name = '"+name+"'";
        return (setData(statement));
    }
    /*
     * Helper method for converting a list of Users into a parse-able string to be stored in database. 
     */
    String userListToString(ArrayList<User> list)
    {	
    	String listString="";
    	ArrayList<User>tempList = list;
    	for (int i=0; i < tempList.size();i++)
    	{
    		listString = listString + tempList.get(i).getId()+"\n";
    	}
    	listString = listString + "0\n";
    	return listString;
    }
    /*
     * Helper method for converting a parse-able string taken from database into a list of users.
     */
    ArrayList<User> userStringToList(String str)
    {
    	ArrayList<User> userList = new ArrayList<User>();
    	Scanner scanString = new Scanner(str);
        while (scanString.hasNextLine())
        {
        	userList.add(getUserById(scanString.nextLine()));
        }
        scanString.close();
    	return userList;
    }
    /*
     * Helper method for converting a list of Channels into a parse-able string to be stored in database. 
     */
    String channelListToString(ArrayList<Channel> list)
    {
    	String cListStr = "";
    	for (int i = 0; i < list.size(); i++)
    	{
    		cListStr = cListStr + list.get(i).getName() + "\n";
    	}
    	return cListStr;
    }
    /*
     * Helper method for converting a parse-able string taken from database into a list of channels.
     */
    ArrayList<Channel> channelStringToList(String str)
    {
    	ArrayList<Channel> channelList = new ArrayList<Channel>();
    	Scanner scanString = new Scanner(str);
        while (scanString.hasNextLine())
        {
        	channelList.add(getChannelByName(scanString.nextLine()));
        }
        scanString.close();
    	return channelList;    	
    }
}
