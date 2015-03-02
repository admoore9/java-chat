package com.team1.chat.models;

import com.team1.chat.interfaces.ChatServiceControllerInterface;

import java.util.ArrayList;
import java.sql.*;



public class ChatServiceController implements ChatServiceControllerInterface
{
    private ChatService cs = null;

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
        boolean success = false;

        success = getChatServiceInstance().setUsername(uid, newUsername);

        return success;
    }

    public boolean setPassword(String uid, String newPassword)
    {
        boolean success = false;

        success = getChatServiceInstance().setPassword(uid, newPassword);

        return success;
    }

    public boolean leaveChannel(String cid, String uid)
    {
        return this.getChatServiceInstance().leaveChannel(cid, uid);
    }

    public boolean joinChannel(String cid, String uid)
    {
        return this.getChatServiceInstance().joinChannel(cid, uid);
    }

    public ArrayList<User> listChannelUsers(String cid, String uid)
    {
        return this.getChatServiceInstance().listChannelUsers(cid, uid);
    }

    private ChatService getChatServiceInstance()
    {
        if (cs == null)
        {
            cs = new ChatService();
        }
        return cs;
    }

    private ChatService getChatServiceInstance()
    {
        if (cs == null)
            cs = new ChatService();
        return cs;
    }

}
