package com.team1.chat.models;

import com.mysql.fabric.Server;
import com.team1.chat.interfaces.UserInterface;

import java.sql.*;
import java.util.ArrayList;

public class User implements UserInterface
{
	private String uid;
	private String username;
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
		this.username = username;
		this.password = password;
	}
	

    public boolean createUser(String username, String password)
    {
    	//TODO Need a helper methods to check if username and password
    	//     are of a valid format. 
        this.username = username;
        this.password = password;
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

    /**
     * Sets the username of this User object.
     * @param uid
     * @param newUsername the new username for this user
     * @return true if uid match and username set, false otherwise
     */
    public boolean setUsername(String uid, String newUsername)
    {
        // make sure we have correct User
        if(this.getId().equals(uid)){
            this.username = newUsername;
            return true;
        }
        else
            return false;
    }

    /**
     * Returns the User's username.
     */
    public String getUsername()
    {
        return username;
    }

    /**
     * Sets the password of this User object.
     * @param uid
     * @param newPassword
     * @return true if uid match and password set, false otherwise
     */
    public boolean setPassword(String uid, String newPassword)
    {
        // make sure we have correct User

        if(this.getId().equals(uid)){
            this.password = newPassword;
            return true;
        }
        else
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
