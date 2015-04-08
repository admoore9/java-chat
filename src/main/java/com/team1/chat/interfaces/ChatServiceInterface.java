package com.team1.chat.interfaces;

import com.team1.chat.models.Channel;
import com.team1.chat.models.User;

import java.util.ArrayList;

public interface ChatServiceInterface
{
    // Iteration 1
    boolean createAccount(String username, String password);

    String login(String username, String password);

    boolean logout(String uid);

    boolean setUsername(String uid, String newUsername);

    boolean setPassword(String uid, String newPassword);

    boolean leaveChannel(String cid, String uid);

    boolean joinChannel(String cid, String uid);

    ArrayList<User> listChannelUsers(String cid, String uid);

    // Iteration 2
    boolean createChannel(String cname, String aid);

    boolean deleteChannel(String cname, String aid);

    boolean inviteUserToChannel(String cname, String aid, String uname);

    boolean removeUserFromChannel(String cname, String aid, String uname);

    ArrayList<Channel> viewInvitedChannels(String uid);

    ArrayList<Channel> viewPrivateChannels(String uid);

    boolean toggleChannelVisibility(String cname, String aid);
}
