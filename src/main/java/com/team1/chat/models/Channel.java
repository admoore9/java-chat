package com.team1.chat.models;

import com.team1.chat.interfaces.ChannelInterface;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 * The channel class which contains a list of whiteListed users and current users
 */
public class Channel implements ChannelInterface
{

    private String name;
    private String admin;
    private boolean isPublic;
    private ArrayList<User> whiteList;
    private ArrayList<User> currentUsers;

    /**
     * Constructor for the Channel
     */
    public Channel(String name, String admin)
    {
        this.name = name;
        this.admin = admin;
        this.isPublic = false;
        this.whiteList = new ArrayList<User>();
        this.currentUsers = new ArrayList<User>();
    }

    public ArrayList<User> getWhiteList()
    {
        return whiteList;
    }

    public boolean isPublic()
    {
        return isPublic;
    }

    public String getName()
    {
        return name;
    }

    public String getAdminId()
    {
        return admin;
    }

    /**
     * Method to check if the user is WhiteListed
     *
     * @param u user
     * @return true if the white list contains user: false otherwise
     */
    public boolean isWhiteListed(User u)
    {
        int i;

        for (i = 0; i < whiteList.size(); i++)
        {
            if (whiteList.get(i).getId().equals(u.getId()))
            {
                return true;
            }
        }

        return false;
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
        int i;

        if (isWhiteListed(u))
        {
            for (i = 0; i < currentUsers.size(); i++)
            {
                if (currentUsers.get(i).getId().equals(u.getId()))
                {
                    return false;
                }
            }

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
        int i;

        for (i = 0; i < currentUsers.size(); i++)
        {
            if (currentUsers.get(i).getId().equals(u.getId()))
            {
                currentUsers.remove(i);
                return true;
            }
        }
        return false;
    }

    // Iteration 2
    /**
     * Method that gets the users in the channel to prepare for deletion
     *
     * @param aid id of user deleting the channel
     * @return a list of users in the channel
     */
    public ArrayList<User> deleteChannel(String aid)
    {
        if (admin.equals(aid))
        {
            return whiteList;
        }
        return null;
    }

    /**
     * Method that adds a user to its white list
     *
     * @param aid id of current user
     * @param u user to add
     * @return true on success
     */
    public boolean whiteListUser(String aid, User u)
    {
        int i;

        if (admin.equals(aid))
        {
            for (i = 0; i < whiteList.size(); i++)
            {
                if (whiteList.get(i).getId().equals(u.getId()))
                {
                    return false;
                }
            }
            whiteList.add(u);
            return true;
        }
        return false;
    }

    /**
     * Method that removes a user from the channel's white list
     *
     * @param aid id of current user
     * @param u user to remove
     * @return true on success
     */
    public boolean removeUser(String aid, User u)
    {
        int i;

        if (admin.equals(aid) && !aid.equals(u.getId()))
        {
            for (i = 0; i < whiteList.size(); i++)
            {
                if (whiteList.get(i).getId().equals(u.getId()))
                {
                    whiteList.remove(i);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Method that toggles is public true/false
     *
     * @param aid id of current user
     * @return true on success
     */
    public boolean toggleChannelVisibility(String aid)
    {
        if (admin.equals(aid))
        {
            isPublic = !isPublic;
            return true;
        }
        return false;
    }
}
