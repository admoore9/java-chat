package com.team1.chat.models;

import com.team1.chat.interfaces.ChatServiceControllerInterface;

import java.util.ArrayList;

//Tyler Notes: I'm thinking that each instance of ChatServiceController
//will need to open a Socket, and pass information to ChatService.

public class ChatServiceController implements ChatServiceControllerInterface
{
	ChatService cs;
    public boolean createAccount(String username, String password)
    {
    	
    	return cs.createAccount(username, password);
    }

    public String login(String username, String password)
    {
        return cs.login(username, password);
    }

    public boolean logout(String uid)
    {
        return cs.logout(uid);
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
