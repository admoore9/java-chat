package com.team1.chat.models;

import com.team1.chat.interfaces.UserInterface;

public class User implements UserInterface
{
    public boolean createUser(String username, String password)
    {
        return false;
    }

    public String getId()
    {
        return null;
    }

    // TODO what is toInactive() ?

    public boolean sendMessage(UserInterface u, String msgText)
    {
        return false;
    }

    public boolean setUsername(String uid, String newUsername)
    {
        return false;
    }

    public String getUsername()
    {
        return null;
    }

    public boolean setPassword(String uid, String newPassword)
    {
        return false;
    }
}
