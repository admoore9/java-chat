package com.team1.chat.interfaces;

import java.util.ArrayList;

public interface ChatServiceInterface
{
    boolean createAccount(String username, String password);

    String login(String username, String password);

    boolean logout(String uid);

    boolean setUsername(String uid, String newUsername);

    boolean setPassword(String uid, String newPassword);

    boolean leaveChannel(String cid, String uid);

    boolean joinChannel(String cid, String uid);

    ArrayList<UserInterface> listChannelUsers(String cid, String uid);
}
