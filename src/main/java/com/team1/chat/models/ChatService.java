package com.team1.chat.models;

import com.team1.chat.interfaces.ChatServiceInterface;
import java.util.ArrayList;

public class ChatService implements ChatServiceInterface
{
    // class-level instantiation of DatabaseSupport object.
    private DatabaseSupport dbs = null;

	/**
	 * Creates a new User in the database.
	 */
    public boolean createAccount(String username, String password)
    {
        User u = new User();
        u.createUser(username, password);
        if (dbs.putUser(u))
        {
        	return true;
        }
        else return false;
    }

    /**
     * Gets User data from the database. 
     * User joins the default channel.
     * @return Returns the user's id.
     * 		   If the database could not retrieve a valid user, id returned is empty.
     */
    public String login(String username, String password)
    {
    	User u = dbs.getUser(username, password);
    	String id = u.getId();
    	if (!id.isEmpty())
    	{
    		joinChannel("0",id);
    	}
        return id;
    }

    //TODO Need to eventually discuss what actions we want to occur on logout.
    /**
     * User logs out and leaves the default channel.
     * @return true on success, false on fail.
     */
    public boolean logout(String uid)
    {
    	User u = dbs.getUser(uid);
    	if (u.getId() != "")
    	{
    		//TODO In next iteration, need to leave all channels.
    		 leaveChannel("0",u.getId());
    		//TODO Need to ditch toInactive() method. 
    		 return true;
    	}
    	else return false;
    }

    /**
     * Fetch this uid associated object from database and update username.
     * @param uid
     * @param newUsername
     * @return true on successful update, false otherwise.
     */
    public boolean setUsername(String uid, String newUsername)
    {
        // get User object from database.
        User u = this.getDatabaseSupportInstance().getUser(uid);

       // if(u == null)
            //throw error here.

        // use setUsername method in User object to place new username.
        u.setUsername(uid, newUsername);

        // return User object to database, if returns false, we have an error.
        if( dbs.putUser(u)) {
            System.out.println("Database put error");
            return false;
        }
        return true;
    }

    /**
     * Fetch this uid associated object from database and update username.
     * @param uid
     * @param newPassword
     * @return  true on successful update, false otherwise.
     */
    public boolean setPassword(String uid, String newPassword)
    {
        // get User object from database.
        User u = getDatabaseSupportInstance().getUser(uid);

        // if(u == null)
             //throw error here.

        // use setUsername method in User object to place new username.
        u.setPassword(uid, newPassword);

        // return User object to database, if returns false, we have an error.
        if(!dbs.putUser(u)) {
            System.out.println("Database put error");
            return false;
        }
        return true;
    }

    /**
     * Method that removes a user from the channel by calling the removeChannelUser() method
     *
     * @param cid channel id
     * @param uid user id
     * @return true if successful: false otherwise
     */
    public boolean leaveChannel(String cid, String uid)
    {
        Channel ch = new Channel();
        User u = new User();
        // TODO the above code will be changed when db support is implemented

        // TODO get channel from db
        // TODO get user from db

        if (ch != null && u != null && ch.removeChannelUser(u))
        {
            // TODO put updated channel in db
            return true;
        }
        return false;
    }

    /**
     * Method that adds a user to the channel by calling the addChannelUser() method
     *
     * @param cid channel id
     * @param uid user id
     * @return true if successful: false otherwise
     */
    public boolean joinChannel(String cid, String uid)
    {
        Channel ch = new Channel();
        User u = new User();
        // TODO the above code will be changed when db support is implemented

        // TODO get channel from db
        // TODO get user from db

        if (ch != null && u != null && ch.addChannelUser(u))
        {
            // TODO put updated channel in db
            return true;
        }
        return false;
    }

    /**
     * Method that returns a list of users in the channel by calling the listChannelUsers() method
     *
     * @param cid channel id
     * @param uid user id
     * @return a list of channel users if successful: null otherwise
     */
    public ArrayList<User> listChannelUsers(String cid, String uid)
    {
        Channel ch = new Channel();
        User u = new User();
        // TODO the above code will be changed when db support is implemented

        // TODO get channel from db
        // TODO get user from db

        if (ch != null && u != null && ch.isWhiteListed(u))
        {
            return ch.listChannelUsers();
        }
        return null;
    }

    // iteration 2
    /**
     * Method that creates a new channel and adds the current user to that channel
     *
     * @param cname channel name
     * @param aid users id
     * @return true on success
     */
    public boolean createChannel(String cname, String aid)
    {
        if (this.getDatabaseSupportInstance().getChannelByName(cname) == null)
        {
            Channel c = new Channel(cname, aid);
            User u = this.getDatabaseSupportInstance().getUserById(aid);
            u.addChannel(c);

            // update database
            this.getDatabaseSupportInstance().putChannel(c);
            this.getDatabaseSupportInstance().putUser(u);

            return true;
        }
        return false;
    }

    /**
     * Method that deletes a channel
     *
     * @param cname channel name
     * @param aid users id
     * @return true on success
     */
    public boolean deleteChannel(String cname, String aid)
    {
        int i;
        Channel c;
        User user;
        ArrayList<User> users;

        c = this.getDatabaseSupportInstance().getChannelByName(cname);
        if (c != null)
        {
            users = c.deleteChannel(aid);
            if (users != null)
            {
                // the user is confirmed to be the admin and we have a list of users in the channel
                for (i = 0; i < users.size(); i++)
                {
                    user = this.getDatabaseSupportInstance().getUserById(users.get(i).getId());
                    user.deleteChannel(c);
                    this.getDatabaseSupportInstance().putUser(user);
                }
                this.getDatabaseSupportInstance().deleteChannel(c.getName());
            }
        }
        return false;
    }

    /**
     * Method that invites a user to a channel
     *
     * @param cname channel name
     * @param aid current user id
     * @param uname name of user to invite
     * @return true on success
     */
    public boolean inviteUserToChannel(String cname, String aid, String uname)
    {
        Channel c;
        User u;

        c = this.getDatabaseSupportInstance().getChannelByName(cname);
        u = this.getDatabaseSupportInstance().getUserByName(uname);

        if (c != null && u != null)
        {
            if (c.whiteListUser(aid, u) && u.addChannelInvite(c))
            {
                this.getDatabaseSupportInstance().putChannel(c);
                this.getDatabaseSupportInstance().putUser(u);

                return true;
            }
        }
        return false;
    }

    /**
     * Method that removes a user from a channel
     *
     * @param cname channel name
     * @param aid current user id
     * @param uname name of user to remove
     * @return true on success
     */
    public boolean removeUserFromChannel(String cname, String aid, String uname)
    {
        Channel c;
        User u;

        c = this.getDatabaseSupportInstance().getChannelByName(cname);
        u = this.getDatabaseSupportInstance().getUserByName(uname);

        if (c != null && u != null)
        {
            if (c.removeUser(aid, u) && u.removeFromChannel(c))
            {
                this.getDatabaseSupportInstance().putChannel(c);
                this.getDatabaseSupportInstance().putUser(u);

                return true;
            }
        }
        return false;
    }

    /**
     * Method that gets a list of channels the user is invited to
     *
     * @param uid users id
     * @return a list of channels the user is invited to
     */
    public ArrayList<Channel> viewInvitedChannels(String uid)
    {
        User u;

        u = this.getDatabaseSupportInstance().getUserById(uid);

        return u.viewInvitedChannels();
    }

    /**
     * Method that gets a list of channels the user has joined
     *
     * @param uid users id
     * @return a list of private channels
     */
    public ArrayList<Channel> viewPrivateChannels(String uid)
    {
        User u;

        u = this.getDatabaseSupportInstance().getUserById(uid);

        return u.viewPrivateChannels();
    }

    /**
     * Method that toggles visibility of a channel
     *
     * @param cname channel name
     * @param aid users id
     * @return true on success
     */
    public boolean toggleChannelVisibility(String cname, String aid)
    {
        Channel c;

        c = this.getDatabaseSupportInstance().getChannelByName(cname);

        return c != null && c.toggleChannelVisibility(aid);
    }

    /**
     * @return a new instance of DatabaseSupport class
     */
    private DatabaseSupport getDatabaseSupportInstance(){
        if(dbs == null)
            dbs = new DatabaseSupport();
        return dbs;
    }
}
