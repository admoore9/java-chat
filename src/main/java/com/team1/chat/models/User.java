package com.team1.chat.models;

import com.mysql.fabric.Server;
import com.team1.chat.interfaces.UserInterface;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class User implements UserInterface
{
	private String uid;
	private String username;
	private String password;
    private String publicName;
    private String currentChannel;
    private ArrayList<Channel> invitedChannels;
    private ArrayList<Channel> privateChannels;
    private ArrayList<Channel> publicChannels;
    private ArrayList<User> friends;
    private ArrayList<User> blocked;

	/**
	 * Constructor
	 */
	public User()
	{
		this.uid=null;
        this.currentChannel = null;
        this.invitedChannels = new ArrayList<Channel>();
        this.privateChannels = new ArrayList<Channel>();
        this.friends = new ArrayList<User>();
        this.blocked = new ArrayList<User>();
        this.publicName = "";
	}
    /**
     * Constructor
     * @param id user id
     * @param username user username
     * @param password user password
     */
	public User(String id, String username, String password){
		this.username=username;
		this.password=password;
		this.uid=id;
        this.currentChannel = null;
        this.invitedChannels = new ArrayList<Channel>();
        this.privateChannels = new ArrayList<Channel>();
        this.friends = new ArrayList<User>();
        this.blocked = new ArrayList<User>();
        this.publicName = "";		
	}
    /**
     * Method to create a user using a username and password
     * @param username user username
     * @param password user password
     * @return true
     */
    public boolean createUser(String username, String password)
    {
        this.username = username;
        this.password = password;
        return true;
    }

    /**
     * Returns the User's id.
     */
    public String getId()
    {
    	return uid;
    }
    
    /**
     * Returns the private channels that this User has a pending invite to.
     * @return
     */
    public ArrayList<Channel> getInvitedChannels(){
    	return this.invitedChannels;
    }
    /**
     * Returns the private channels that this User has accepted an invite for.
     * @return
     */
    public ArrayList<Channel> getPrivateChannels(){
    	return this.privateChannels;
    }
    
    public boolean sendMessage(UserInterface u, String msgText)
    {
        return false;
    }

    /**
     * Sets the username of this User object.
     * @param uid users id
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
     * @param uid user id
     * @param newPassword new password for user
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

    /**
     *  Set the currentChannel of a User
     */
    public boolean setCurrentChannel(String uid, String cname){
        if(this.uid!=null && this.getId().equals(uid)) {
            this.currentChannel = cname;
            return true;
        }
        else
            return false;
    }

    /**
     * Get a User's current channel
     * If no channel set, set to default and return.
     */
    public String getCurrentChannel(String uid){
        if(this.uid!=null && this.getId().equals(uid) ){
            if(this.currentChannel != null) {
                return this.currentChannel;
            }
            else{
                // Shouldn't happen, should be set to default on login.
                System.out.println("Logged-in user does not have default channel set.");
                return null;
            }
        }
        else {
            return null;
        }
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

        if (currentChannel != null && currentChannel.equals(deletedChannel))
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

    /**
     * Method that gets a list of public channels
     *
     * @return a list of public channels
     */
    public ArrayList<Channel> viewPublicChannels()
    {
        return publicChannels;
    }

    // Iteration 3
    /**
     * Method that adds a user to the user's friends list
     *
     * @param f friend to add
     * @return true on success
     */
    public boolean addFriend(User f)
    {
        int i;

        for (i = 0; i < friends.size(); i++)
        {
            if (friends.get(i).getUsername().equals(f.getUsername()))
            {
                return true;
            }
        }

        friends.add(f);
        return true;
    }

    /**
     * Method that removes a user from the user's friends list
     *
     * @param f friend to remove
     * @return true on success
     */
    public boolean removeFriend(User f)
    {
        int i;

        for (i = 0; i < friends.size(); i++)
        {
            if (friends.get(i).getUsername().equals(f.getUsername()))
            {
                friends.remove(i);
                return true;
            }
        }

        return true;
    }

    /**
     * Returns the user's list of friends
     */
    public ArrayList<User> getFriends()
    {
        return friends;
    }

    /**
     * Method that adds a user to the user's blocked list
     *
     * @param f user to add
     * @return true on success
     */
    public boolean blockUser(User f)
    {
        int i;

        for (i = 0; i < blocked.size(); i++)
        {
            if (blocked.get(i).getUsername().equals(f.getUsername()))
            {
                return true;
            }
        }

        blocked.add(f);
        return true;
    }

    /**
     * Method that removes a user from the user's blocked list
     *
     * @param f user to remove
     * @return true on success
     */
    public boolean removeBlockedUser(User f)
    {
        int i;

        for (i = 0; i < blocked.size(); i++)
        {
            if (blocked.get(i).getUsername().equals(f.getUsername()))
            {
                blocked.remove(i);
                return true;
            }
        }

        return true;
    }

    /**
     * Returns the user's list of blocked users
     */
    public ArrayList<User> getBlockedUsers()
    {
        return blocked;
    }

    /**
     * Method that changes the public name of the user
     *
     * @param n name to set as public name
     * @return true on success
     */
    public boolean setPublicName(String n)
    {
        this.publicName = n;
        return true;
    }

    public String getPublicName()
    {
        return publicName;
    }

    /**
     * Method that handles accepting a channel invite
     *
     * @param c channel
     * @return true on success
     */
    public boolean acceptChannelInvite(Channel c)
    {
        int i;

        for (i = 0; i < invitedChannels.size(); i++)
        {
            if (invitedChannels.get(i).getName().equals(c.getName()))
            {
                invitedChannels.remove(i);

                if (!c.isPublic())
                {
                    privateChannels.add(c);
                }

                return true;
            }
        }

        return false;
    }

    /**
     * Method that handles declining a channel invite
     *
     * @param c channel
     * @return true on success
     */
    public boolean declineChannelInvite(Channel c)
    {
        int i;

        for (i = 0; i < invitedChannels.size(); i++)
        {
            if (invitedChannels.get(i).getName().equals(c.getName()))
            {
                invitedChannels.remove(i);
                return true;
            }
        }

        return false;
    }
}
