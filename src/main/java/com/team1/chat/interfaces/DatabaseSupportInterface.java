package com.team1.chat.interfaces;

public interface DatabaseSupportInterface
{
    boolean putUser(UserInterface u);

    UserInterface getUser(String username, String password);

    UserInterface getUser(String uid);

    boolean nameAvailable(String newUsername);

    ChannelInterface getChannel(String cid);

    boolean putChannel(ChannelInterface c);
}
