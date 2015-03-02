package com.team1.chat.models;

import com.team1.chat.interfaces.DatabaseSupportInterface;

public class DatabaseSupport implements DatabaseSupportInterface
{
    public boolean putUser(User u)
    {
        return false;
    }

    public User getUser(String username, String password)
    {
        return null;
    }

    public User getUser(String uid)
    {
        return null;
    }

    public boolean nameAvailable(String newUsername)
    {
        return false;
    }

    public Channel getChannel(String cid)
    {
        return null;
    }

    public boolean putChannel(Channel c)
    {
        return false;
    }

}
