package com.team1.chat.models;

import com.team1.chat.interfaces.ChatServiceControllerInterface;

import java.util.ArrayList;
import java.sql.*;



public class ChatServiceController implements ChatServiceControllerInterface
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



    public boolean setUsername(int uid, String newUsername)
    {
        boolean success = false;

        success = ChatService.setUsername(uid, newUsername);

        return success;
    }

    public boolean setPassword(int uid, String newPassword)
    {
        boolean success = false;

        success = ChatService.setPassword(uid, newPassword);

        return success;
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
