package com.team1.chat.interfaces;

import com.team1.chat.models.User;

import java.net.Socket;
import java.util.ArrayList;

public interface ChannelInterface
{
    public ArrayList<User> listChannelUsers();

    public boolean addChannelUser(User u);

    public boolean removeChannelUser(User u);

    public boolean isWhiteListed(User u);

    public interface ChannelListener{
        public void start();
        public boolean sendMessage(String username, String msgText);
    }
}
