package com.team1.chat.models;

import com.team1.chat.interfaces.ChannelInterface;

import java.util.ArrayList;

/**
 * The channel class which contains a list of whitelisted users and current users
 */
public class Channel implements ChannelInterface
{
    private ArrayList<User> whitelist;
    private ArrayList<User> currentUsers;

    /**
     * Constructor for the Channel
     */
    public Channel()
    {
        this.whitelist = new ArrayList<User>();
        this.currentUsers = new ArrayList<User>();
    }

    /**
     * Method to check if the user is whitelisted
     *
     * @param u user
     * @return true if the whitelist contains user: false otherwise
     */
    public boolean isWhitelisted(User u)
    {
        return whitelist.contains(u);
    }

    /**
     * Method that returns a list of users currently in the channel
     *
     * @return the list of users in the channel
     */
    public ArrayList<User> listChannelUsers()
    {
        return currentUsers;
    }

    /**
     * Method to add a user to the channel
     *
     * @param u user
     * @return true if the user is successfully added: false otherwise
     */
    public boolean addChannelUser(User u)
    {
        if (!currentUsers.contains(u) && isWhitelisted(u))
        {
            currentUsers.add(u);
            return true;
        }
        return false;
    }

    /**
     * Method that removes a user from the channel
     *
     * @param u user
     * @return true if the user is successfully removed: false otherwise
     */
    public boolean removeChannelUser(User u)
    {
        if (currentUsers.remove(u))
        {
            currentUsers.trimToSize();
            return true;
        }
        return false;
    }
}
