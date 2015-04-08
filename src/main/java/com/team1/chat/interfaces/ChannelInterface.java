package com.team1.chat.interfaces;

import com.team1.chat.models.User;

import java.net.Socket;
import java.util.ArrayList;

public interface ChannelInterface
{
    // Iteration 1
    ArrayList<User> listChannelUsers();

    boolean addChannelUser(User u);

    boolean removeChannelUser(User u);

    boolean isWhiteListed(User u);

    public interface ChannelListener{
        public void start();
        public boolean sendMessage(String username, String msgText);
    }

    // Iteration 2
    ArrayList<User> deleteChannel(String aid);

    boolean whiteListUser(String aid, User u);

    boolean removeUser(String aid, User u);

    boolean toggleChannelVisibility(String aid);
}
