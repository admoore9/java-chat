package com.team1.chat.models;

import com.team1.chat.interfaces.ChatServiceInterface;
import java.util.ArrayList;
import com.team1.chat.models.DatabaseSupport;
import com.team1.chat.models.User;

public class ChatService implements ChatServiceInterface
{
	DatabaseSupport db;
	
	/**
	 * Creates a new User in the database.
	 */
    public boolean createAccount(String username, String password)
    {
        User u = new User();
        u.createUser(username, password);
        if (db.putUser(u))
        {
        	return true;
        }
        else return false;
    }

    /**
     * Gets User data from the database. 
     * User joins the default channel.
     * @return Returns the user's id.
     * 		   If the database could not retrieve a valid user, id returned is empty.
     */
    public String login(String username, String password)
    {
    	User u = db.getUser(username, password);
    	String id = u.getId();
    	if (!id.isEmpty())
    	{
    		joinChannel("0",id);
    	}
        return id;
    }

    //TODO Need to eventually discuss what actions we want to occur on logout.
    /**
     * User logs out and leaves the default channel.
     * @return true on success, false on fail.
     */
    public boolean logout(String uid)
    {
    	User u = db.getUser(uid);
    	if (u.getId() != "")
    	{
    		//TODO In next iteration, need to leave all channels.
    		 leaveChannel("0",u.getId());
    		//TODO Need to ditch toInactive() method. 
    		 return true;
    	}
    	else return false;
    }

    public boolean setUsername(String uid, String newUsername)
    {
        return false;
    }

    public boolean setPassword(String uid, String newPassword)
    {
        return false;
    }

    public boolean leaveChannel(String cid, String uid)
    {
        return false;
    }

    public boolean joinChannel(String cid, String uid)
    {
        return false;
    }

    public ArrayList<User> listChannelUsers(String cid, String uid)
    {
        return null;
    }
}
