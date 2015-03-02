package com.team1.chat.models;

import com.team1.chat.interfaces.UserInterface;
import java.sql.*;


public class User implements UserInterface
{
    private int uid;
    private String username;
    private String password;

    public User(int uid, String username, String password)
    {
        this.uid = uid;
        this.username = username;
        this.password = password;
    }


    public boolean createUser(String username, String password)
    {
        return false;
    }

    public int getId()
    {
        return 0;
    }

    // TODO what is toInactive() ?

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
    public boolean setUsername(int uid, String newUsername)
    {
        // make sure we have correct User
        if(this.getId() == uid){
            this.username = newUsername;
            return true;
        }
        else
            return false;
    }

    public String getUsername()
    {
        return null;
    }

    /**
     * Sets the password of this User object.
     * @param uid
     * @param newPassword
     * @return true if uid match and password set, false otherwise
     */
    public boolean setPassword(int uid, String newPassword)
    {
        // make sure we have correct User
        if(this.getId() == uid){
            this.password = newPassword;
            return true;
        }
        else
            return false;
    }
}
