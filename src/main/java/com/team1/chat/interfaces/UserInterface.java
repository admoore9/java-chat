package com.team1.chat.interfaces;

import com.team1.chat.models.Channel;

import java.util.ArrayList;

public interface UserInterface
{
    // Iteration 1
    boolean createUser(String username, String password);

    String getId();

    boolean sendMessage(UserInterface u, String msgText);

    boolean setUsername(String uid, String newUsername);

    String getUsername();

    boolean setPassword(String uid, String newPassword);

    String getPassword();

    // Iteration 2
    boolean addChannel(Channel c);

    boolean deleteChannel(Channel c);

    boolean addChannelInvite(Channel c);

    boolean removeFromChannel(Channel c);

    ArrayList<Channel> viewInvitedChannels();

    ArrayList<Channel> viewPrivateChannels();
}
