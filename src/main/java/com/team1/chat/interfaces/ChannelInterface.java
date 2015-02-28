package com.team1.chat.interfaces;

import com.team1.chat.models.User;

import java.util.ArrayList;

public interface ChannelInterface
{
    public boolean isRostered(String uid);

    public ArrayList<User> listChannelUsers();

    public boolean addChannelUser(String uid);

    public boolean removeChannelUser(String uid);

    public boolean isWhiteListed(String uid);
}
