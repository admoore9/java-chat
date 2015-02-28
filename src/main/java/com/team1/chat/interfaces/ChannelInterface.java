package com.team1.chat.interfaces;

import java.util.ArrayList;

public interface ChannelInterface
{
    public boolean isRostered(String uid);

    public ArrayList<UserInterface> listChannelUsers();

    public boolean addChannelUser(String uid);

    public boolean removeChannelUser(String uid);

    public boolean isWhiteListed(String uid);
}
