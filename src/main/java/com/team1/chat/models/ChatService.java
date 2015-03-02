package com.team1.chat.models;

import com.team1.chat.interfaces.ChatServiceInterface;

import java.util.ArrayList;

public class ChatService implements ChatServiceInterface
{
    public boolean createAccount(String username, String password)
    {
        return false;
    }

    public String login(String username, String password)
    {
        return null;
    }

    public boolean logout(String uid)
    {
        return false;
    }

    public boolean setUsername(String uid, String newUsername)
    {
        return false;
    }

    public boolean setPassword(String uid, String newPassword)
    {
        return false;
    }

    /**
     * Method that removes a user from the channel by calling the removeChannelUser() method
     *
     * @param cid channel id
     * @param uid user id
     * @return true if successful: false otherwise
     */
    public boolean leaveChannel(String cid, String uid)
    {
        Channel ch = new Channel();
        User u = new User();
        // TODO the above code will be changed when db support is implemented

        // TODO get channel from db
        // TODO get user from db

        if (ch != null && u != null && ch.removeChannelUser(u))
        {
            // TODO put updated channel in db
            return true;
        }
        return false;
    }

    /**
     * Method that adds a user to the channel by calling the addChannelUser() method
     *
     * @param cid channel id
     * @param uid user id
     * @return true if successful: false otherwise
     */
    public boolean joinChannel(String cid, String uid)
    {
        Channel ch = new Channel();
        User u = new User();
        // TODO the above code will be changed when db support is implemented

        // TODO get channel from db
        // TODO get user from db

        if (ch != null && u != null && ch.addChannelUser(u))
        {
            // TODO put updated channel in db
            return true;
        }
        return false;
    }

    /**
     * Method that returns a list of users in the channel by calling the listChannelUsers() method
     *
     * @param cid channel id
     * @param uid user id
     * @return a list of channel users if successful: null otherwise
     */
    public ArrayList<User> listChannelUsers(String cid, String uid)
    {
        Channel ch = new Channel();
        User u = new User();
        // TODO the above code will be changed when db support is implemented

        // TODO get channel from db
        // TODO get user from db

        if (ch != null && u != null && ch.isWhitelisted(u))
        {
            return ch.listChannelUsers();
        }
        return null;
    }
}
