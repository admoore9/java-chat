package com.team1.chat.models;

import com.team1.chat.interfaces.UserInterface;

public class User implements UserInterface
{
	private String uid;
	private String uname;
	private String password;
	
	/**
	 * Default constructor.
	 */
	public User(){
		
	}
	
	/**
	 * Overloaded Constructor
	 * @param uid user id
	 * @param username
	 * @param password
	 */
	public User(String uid, String username, String password)
	{
		this.uid = uid;
		uname = username;
		this.password = password;
	}
	
	/**
	 * Creates User with an un-set user id.
	 */
    public boolean createUser(String username, String password)
    {
    	//TODO Need a helper methods to check if username and password
    	//     are of a valid format. 
        uname=username;
        this.password=password;
        uid = "";
        return true;
        
    }

    /**
     * Returns the User's id.
     */
    public String getId()
    {
    	return uid;
    }
    
    public boolean sendMessage(UserInterface u, String msgText)
    {
        return false;
    }

    public boolean setUsername(String uid, String newUsername)
    {
        return false;
    }

    /**
     * Returns the User's username.
     */
    public String getUsername()
    {
        return uname;
    }

    public boolean setPassword(String uid, String newPassword)
    {
        return false;
    }
    
    /**
     * Returns the User's password.
     */
    public String getPassword()
    {
    	return password;
    }
}
