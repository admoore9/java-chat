package com.team1.chat.interfaces;

import com.team1.chat.models.Channel;
import com.team1.chat.models.User;

public interface DatabaseSupportInterface
{
    boolean putUser(User u);

    User getUser(String username, String password);

    User getUser(String uid);

    boolean nameAvailable(String newUsername);

    Channel getChannel(String name);
    
    boolean putChannel(Channel c);
    
    void close();
}
