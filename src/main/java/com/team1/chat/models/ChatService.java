package com.team1.chat.models;

import com.team1.chat.interfaces.ChatServiceInterface;

import java.util.ArrayList;

public class ChatService implements ChatServiceInterface
{
    // class-level instantiation of DatabaseSupport object.
    private DatabaseSupport dbs = null;

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

    /**
     * Fetch this uid associateed object from database and update username.
     * @param uid
     * @param newUsername
     * @return true on successful update, false otherwise.
     */
    public boolean setUsername(int uid, String newUsername)
    {
        // get User object from database.
        User u = this.getDatabaseSupportInstance().getUser(uid);

       // if(u == null)
            //throw error here.

        // use setUsername method in User object to place new username.
        u.setUsername(uid, newUsername);

        // return User object to database, if returns false, we have an error.
        if( dbs.putUser(u)) {
            System.out.println("Database put error");
            return false;
        }
        return true;
    }

    /**
     * Fetch this uid associateed object from database and update username.
     * @param uid
     * @param newPassword
     * @return  true on successful update, false otherwise.
     */
    public boolean setPassword(int uid, String newPassword)
    {
        // get User object from database.
        User u = getDatabaseSupportInstance().getUser(uid);

        // if(u == null)
             //throw error here.

        // use setUsername method in User object to place new username.
        u.setPassword(uid, newPassword);

        // return User object to database, if returns false, we have an error.
        if(!dbs.putUser(u)) {
            System.out.println("Database put error");
            return false;
        }
        return true;
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


    /**
     * @return a new instance of DatabaseSupport class
     */
    private DatabaseSupport getDatabaseSupportInstance(){
        if(dbs == null)
            dbs = new DatabaseSupport();
        return dbs;
    }
}
