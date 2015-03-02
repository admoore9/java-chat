package com.team1.chat.interfaces;

public interface UserInterface
{
    public boolean createUser(String username, String password);

    public int getId();

    // TODO what is toInactive() ?

    public boolean sendMessage(UserInterface u, String msgText);

    public boolean setUsername(int uid, String newUsername);

    public String getUsername();

    public boolean setPassword(int uid, String newPassword);
}
