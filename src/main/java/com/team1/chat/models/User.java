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
    private String currentChannel;
    private ArrayList<Channel> invitedChannels;
    private ArrayList<Channel> privateChannels;

	/**
	 * Default constructor.
	 */
	public User(){
		this.uid = "-1";
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
        this.currentChannel = null;
        this.invitedChannels = new ArrayList<Channel>();
        this.privateChannels = new ArrayList<Channel>();
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

    // Iteration 2
    /**
     * Method that adds a channel to the users ArrayList of private channels
     * this is called when a user creates a new channel
     *
     * @param c Channel to add
     * @return true on success
     */
    public boolean addChannel(Channel c)
    {
        privateChannels.add(c);
        return true;
    }

    /**
     * Method that takes care of removing a deleted channels information
     * from a user
     *
     * @param c channel that has been deleted
     * @return true on success
     */
    public boolean deleteChannel(Channel c)
    {
        int i;
        String deletedChannel = c.getName();

        if (currentChannel.equals(deletedChannel))
        {
            currentChannel = null;
        }

        for (i = 0; i < invitedChannels.size(); i++)
        {
            if (invitedChannels.get(i).getName().equals(deletedChannel))
            {
                invitedChannels.remove(i);
            }
        }

        for (i = 0; i < privateChannels.size(); i++)
        {
            if (privateChannels.get(i).getName().equals(deletedChannel))
            {
                privateChannels.remove(i);
            }
        }

        return true;
    }

    /**
     * Method that updates a user's list of invited channels
     *
     * @param c channel user has been invited to
     * @return true on success
     */
    public boolean addChannelInvite(Channel c)
    {
        int i;

        for (i = 0; i < invitedChannels.size(); i++)
        {
            if (invitedChannels.get(i).getName().equals(c.getName()))
            {
                return false;
            }
        }

        for (i = 0; i < privateChannels.size(); i++)
        {
            if (privateChannels.get(i).getName().equals(c.getName()))
            {
                return false;
            }
        }

        invitedChannels.add(c);

        return true;
    }

    /**
     * Method that removes a channel from a user's list of channels
     *
     * @param c channel user has been remove from
     * @return true on success
     */
    public boolean removeFromChannel(Channel c)
    {
        int i;

        if (currentChannel.equals(c.getName()))
        {
            currentChannel = null;
        }

        for (i = 0; i < invitedChannels.size(); i++)
        {
            if (invitedChannels.get(i).getName().equals(c.getName()))
            {
                invitedChannels.remove(i);
                return true;
            }
        }

        for (i = 0; i < privateChannels.size(); i++)
        {
            if (privateChannels.get(i).getName().equals(c.getName()))
            {
                privateChannels.remove(i);
                return true;
            }
        }

        return false;
    }

    /**
     * Method that gets a list of the user's invited channels
     *
     * @return a list of the user's invited channels
     */
    public ArrayList<Channel> viewInvitedChannels()
    {
        return invitedChannels;
    }

    /**
     * Method that gets a list of the user's private channels
     *
     * @return a list of the user's private channels
     */
    public ArrayList<Channel> viewPrivateChannels()
    {
        return privateChannels;
    }
}
