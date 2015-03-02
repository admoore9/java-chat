package com.team1.chat.models;

import com.team1.chat.interfaces.UserInterface;

public class User implements UserInterface
{
	private String uid;
	private String uname;
	private String password;
	
	
	public User(){
		
	}
	
	public User(String uid, String username, String password)
	{
		this.uid = uid;
		uname = username;
		this.password = password;
	}
	
    public boolean createUser(String username, String password)
    {
    	//TODO Need a helper methods to check if username and password
    	//     are of a valid format. 
        uname=username;
        this.password=password;
        uid = "";
        return true;
        
    }

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

    public String getUsername()
    {
        return uname;
    }

    public boolean setPassword(String uid, String newPassword)
    {
        return false;
    }
    
    public String getPassword()
    {
    	return password;
    }
}
