package com.team1.chat.models;

import com.team1.chat.interfaces.ChannelInterface;

import java.util.ArrayList;

public class Channel implements ChannelInterface
{
    public boolean isRostered(String uid)
    {
        return false;
    }

    public ArrayList<User> listChannelUsers()
    {
        return null;
    }

    public boolean addChannelUser(String uid)
    {
        return false;
    }

    public boolean removeChannelUser(String uid)
    {
        return false;
    }

    public boolean isWhiteListed(String uid)
    {
        return false;
    }
}
