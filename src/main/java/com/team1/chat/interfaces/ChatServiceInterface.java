package com.team1.chat.interfaces;

import com.team1.chat.models.User;

import java.util.ArrayList;

public interface ChatServiceInterface
{
    boolean createAccount(String username, String password);

    String login(String username, String password);

    boolean logout(String uid);

    boolean setUsername(int uid, String newUsername);

    boolean setPassword(int uid, String newPassword);

    boolean leaveChannel(String cid, String uid);

    boolean joinChannel(String cid, String uid);

    ArrayList<User> listChannelUsers(String cid, String uid);
}
