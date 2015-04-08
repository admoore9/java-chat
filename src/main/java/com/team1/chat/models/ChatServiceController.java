package com.team1.chat.models;

import com.team1.chat.interfaces.ChatServiceControllerInterface;

import java.util.ArrayList;

//Tyler Notes: I'm thinking that each instance of ChatServiceController
//will need to open a Socket, and pass information to ChatService.

public class ChatServiceController implements ChatServiceControllerInterface
{
    private ChatService cs = null;
	
	/**
	 * Creates a new account with username and password.
	 * @return true on success, false on failure.
	 */
    public boolean createAccount(String username, String password)
    {
    	
    	return cs.createAccount(username, password);
    }

    /**
     * Logs in the User whose username/password match the input username and password.
     * @return returns the user's id.
     */
    public String login(String username, String password)
    {
        return cs.login(username, password);
    }

    /**
     * Logs out the User whose id matches the input uid. 
     * @return true on success, false on failure.
     */
    public boolean logout(String uid)
    {
        return cs.logout(uid);
    }

    public boolean setUsername(String uid, String newUsername)
    {
        return this.getChatServiceInstance().setUsername(uid, newUsername);
    }

    public boolean setPassword(String uid, String newPassword)
    {
        return this.getChatServiceInstance().setPassword(uid, newPassword);
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
}
