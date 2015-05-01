package com.team1.chat.models;


import java.lang.reflect.Array;
import java.net.*;
import java.io.*;
import java.util.*;

public class Client
{
    private ObjectInputStream socketInput;
    private ObjectOutputStream socketOutput;
    private Socket socket = null;
    private String username;
    private String userID;
    private String serverName;
    private int serverPort;

    private ChatServiceController csc;

    /*
        Create client object
     */
    public Client(String serverName, int serverPort, String username, String userID)
    {
        this.serverName = serverName;
        this.serverPort = serverPort;
        this.username = username;
        this.userID = userID;
    }

    /*
        initialize a client
     */
    public boolean start()
    {

        System.out.println("Tring to connect to server host: " + serverName + " at port: " + serverPort);

        try {
            socket = new Socket(serverName, serverPort);
        } catch (UnknownHostException e) {
            System.out.println("Host unknown: " + e.getMessage());
            return false;
        } catch (IOException e) {
            System.out.println("Unexpected exception: " + e.getMessage());
            return false;
        }

        System.out.println("Connected: " + socket);

        try {
            socketInput = new ObjectInputStream(socket.getInputStream());
            socketOutput = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.out.println("Exception creating new Input/output Streams: " + e);
            return false;
        }

        // init server to client listener
        new ClientListener().start();

        try {
            socketOutput.writeObject(username);
        } catch (IOException e) {
            System.out.println("Could not create I/O streams: " + e);
            return false;
        }
        return true;
    }

    /*
        Business end of the Client. Send a message. It sends a message.
     */
    public void sendMessage(String message)
    {
        try {
            socketOutput.writeObject(message);
        } catch (IOException e) {
            System.out.println("Could not write message to server: " + e);
        }
    }


    /*
        Log out a client thread, shut down connections, close streams
     */
    public void disconnect()
    {
        // close out connections
        try {
            if (socketOutput != null)
                socketOutput.close();
            if (socketInput != null)
                socketInput.close();
            if (socket != null)
                socket.close();
        } catch (IOException e) {
            System.out.println("Error closing client process. Error message: " + e);
        }
        getChatServiceControllerInstance().logout(userID);
    }

    /*
        Make sure a null CSC object can exist.
        Used to pull database info at user/client request.
     */
    private ChatServiceController getChatServiceControllerInstance()
    {
        if (csc == null) {
            csc = new ChatServiceController();
        }
        return csc;
    }

    /*
        MAIN METHOD
     */
    public static void main(String args[]) throws IOException
    {
        ChatServiceController csc = new ChatServiceController();

        Scanner scanner = new Scanner(System.in);
        String userInput;
        String[] in;
        String uid = "";
        String username = "";


        System.out.println("\nTo login enter: login <username> <password>");
        System.out.println("To create an account enter: createAccount <username> <password>");
        System.out.println("To exit enter: exit");
        //Prompt user to login
        while ((userInput = scanner.nextLine()) != null) {
            // split the string into separate username and password tokens.
            in = userInput.split(" ");

            if (in.length == 3) {
                username = in[1];
                String password = in[2];

                // user logging in
                if (in[0].equals("login")) {
                    uid = csc.login(username, password);
                    if (uid != null) {
                        System.out.println("Login successful");
                        break;
                    }
                    else {
                        System.out.println("Username or Password not found. Please re-enter.");
                    }
                }
                // user creating an account
                else if (in[0].equals("createAccount")) {
                    if (csc.createAccount(username, password)) {
                        System.out.println("Account creation successful.");
                    }
                    else {
                        System.out.println("Account creation failed. Try a new username or password.");
                    }
                }
                else {
                    System.out.println("Incorrect input. You should enter three arguments.");
                }
            }
            else if (in.length == 1) {
                if (in[0].equals("exit")) {
                    return;
                }
            }
            else {
                System.out.println("Incorrect input. You should enter three arguments.");
            }
        }

        // create new client
        Client client = new Client("localhost", 4444, username, uid);
        //Client client = new Client("104.236.206.121", 4444, username, uid);

        // test we can connect
        if (!client.start())
            return;

        while (true) {
            System.out.print("> ");
            String message = scanner.nextLine();

            if (message.length() > 0 && message.charAt(0) == '/') {
                in = message.split(" ");

                if (in.length == 1 && in[0].equals("/help")) {
                    System.out.println("/logout");
                    System.out.println("/setUserName <username>");
                    System.out.println("/setPassword <password");
                    System.out.println("/listChannelUsers");
                    System.out.println("/joinChannel <channelName>");
                    System.out.println("/leaveChannel <channelName");
                    System.out.println("/createChannel <channelName");
                    System.out.println("/deleteChannel <channelName");
                    System.out.println("/inviteUserToChannel <username> <channelName>");
                    System.out.println("/removeUserFromChannel <username> <channelName>");
                    System.out.println("/viewPublicChannels");
                    System.out.println("/viewPrivateChannels");
                    System.out.println("/viewInvitedChannels");
                    System.out.println("/toggleChannelVisibility <channelName>");
                    System.out.println("/addFriend <username>");
                    System.out.println("/removeFriend <username>");
                    System.out.println("/viewFriends");
                    System.out.println("/addBlockedUser <username>");
                    System.out.println("/removeBlockedUser <username>");
                    System.out.println("/viewBlockedUsers");
                    System.out.println("/setPublicName <publicName>");
                    System.out.println("/acceptInviteToChannel <channelName>");
                    System.out.println("/declineInviteToChannel <channelName>");
                }

                else if (in.length == 1 && in[0].equals("/logout")) {
                    client.sendMessage("/logout");
                    client.disconnect();
                    break;
                }

                else if (in.length == 2 && in[0].equals("/setUsername")) {
                    if (csc.setUsername(uid, in[1])) {
                        //String oldUsername = username;
                        username = in[1];
                        client.sendMessage("/changeName " + username);

                        System.out.println("Username has been set to " + username + ".");
                    }
                    else {
                        System.out.println("Username change failed.");
                    }
                }

                else if (in.length == 2 && in[0].equals("/setPassword")) {
                    if (csc.setPassword(uid, in[1])) {
                        System.out.println("Password has been changed.");
                    }
                    else {
                        System.out.println("Password change failed.");
                    }
                }

                else if (in.length == 2 && in[0].equals("/listChannelUsers")) {
                    ArrayList<User> users = csc.listChannelUsers(in[1], uid);
                    if (users != null) {
                        System.out.println("Users in channel " + in[1] + " are:");

                        for (User u : users) {
                            System.out.println(u.getUsername());
                        }
                    }
                    else {
                        System.out.println("List channel users failed.");
                    }
                }

                else if (in.length == 2 && in[0].equals("/joinChannel")) {
                    if (csc.joinChannel(in[1], uid)) {
                        System.out.println("You have successfully joined " + in[1] + ".");
                    }
                    else {
                        System.out.println("Join channel failed.");
                    }
                }

                else if (in.length == 2 && in[0].equals("/leaveChannel")) {
                    if (csc.leaveChannel(in[1], uid)) {
                        System.out.println("You have successfully left " + in[1] + ".");
                    }
                    else {
                        System.out.println("Leave channel failed.");
                    }
                }

                else if (in.length == 2 && in[0].equals("/createChannel")) {
                    if (csc.createChannel(in[1], uid)) {
                        System.out.println("Channel " + in[1] + " has been created.");
                    }
                    else {
                        System.out.println("Create channel failed.");
                    }
                }

                else if (in.length == 2 && in[0].equals("/deleteChannel")) {
                    if (csc.deleteChannel(in[1], uid)) {
                        System.out.println("Channel " + in[1] + " has been deleted.");
                    }
                    else {
                        System.out.println("Delete channel failed.");
                    }
                }
                // TODO - untested from here down.
                else if (in.length == 3 && in[0].equals("/inviteUserToChannel")) {
                    if (csc.inviteUserToChannel(in[1], uid, in[2])) {
                        System.out.println("User: " + in[2] + " added to Channel: " + in[1]);
                    }
                    else {
                        System.out.println("Invite user to channel failed.");
                    }
                }

                else if (in.length == 3 && in[0].equals("/removeUserFromChannel")) {
                    if (csc.removeUserFromChannel(in[1], uid, in[2])) {
                        System.out.println("User: " + in[2] + " removed from Channel: " + in[1]);
                    }
                    else {
                        System.out.println("Error removing user from Channel");
                    }
                }

                else if (in.length == 1 && in[0].equals("/viewPublicChannels")) {
                    int i = 1;
                    ArrayList<Channel> publicChannels = csc.viewPublicChannels(uid);
                    if (!publicChannels.isEmpty()) {
                        System.out.println("\n List of " + username + "'s Public Channels:");
                        for(Channel c : publicChannels)
                            System.out.println(i + ". ) " + c.getName());
                    }
                    else {
                        System.out.println("Sorry, no public channels were found.");
                    }
                }

                else if (in.length == 1 && in[0].equals("/viewPrivateChannels")) {
                    int i = 1;
                    ArrayList<Channel> privateChannels = csc.viewPrivateChannels(uid);
                    if (!privateChannels.isEmpty()) {
                        System.out.println("\n List of " + username + "'s Private Channels:");
                        for(Channel c : privateChannels)
                            System.out.println(i + ". ) " + c.getName());
                    }
                    else {
                        System.out.println("Sorry, no private channels were found.");
                    }
                }

                else if (in.length == 1 && in[0].equals("/viewInvitedChannels")) {
                    int i = 1;
                    ArrayList<Channel> invitedChannels = csc.viewInvitedChannels(uid);
                    if (!invitedChannels.isEmpty()) {
                        System.out.println("\n List of " + username + "'s Channel Invitations:");
                        for(Channel c : invitedChannels)
                            System.out.println(i + ". ) " + c.getName());
                    }
                    else {
                        System.out.println("Sorry, no channel invites were found.");
                    }
                }
                // TODO - No way to get a Channel in CSC. Need it here, and for Channel switching.
                // TODO - should we instantiate a DBS object or add a method to CSC?
                else if (in.length == 2 && in[0].equals("/toggleChannelVisibility")) {
                    if (csc.toggleChannelVisibility(in[1], in[2])) {
                        System.out.println("Channel: " + in[1] + " visibility has been changed.");
                    }
                    else {
                        System.out.println("Error while toggling channel visibility.");
                    }
                }

                else if (in.length == 2 && in[0].equals("/addFriend")) {
                    if (csc.addFriend(uid, in[1])) {
                        System.out.println(in[1] + " has been added to your friends list.");
                    }
                    else {
                        System.out.println("Error adding " + in[1] + " to your friends list.");
                    }
                }

                else if (in.length == 2 && in[0].equals("/removeFriend")) {
                    if (csc.removeFriend(uid, in[1])) {
                        System.out.println(in[1] + " has been removed from your friends list.");
                    }
                    else {
                        System.out.println("Error removing " + in[1] + " from your friends list.");
                    }
                }

                else if (in.length == 1 && in[0].equals("/viewFriends")) {
                    ArrayList<User> friendList = csc.viewFriends(uid);
                    if (!friendList.isEmpty()) {
                        System.out.println(username + "'s Friends List:");
                        for(User u : friendList)
                            System.out.println("  * " + u.getUsername());
                    }
                    else {
                        System.out.println("Error retrieving your Friends List.");
                    }
                }

                else if (in.length == 2 && in[0].equals("/addBlockedUser")) {
                    if (csc.addBlockedUser(uid,in[1])) {
                        System.out.println("Messages from " + in[1] + " are now blocked.");
                    }
                    else {
                        System.out.println("Error adding user to block list.");
                    }
                }

                else if (in.length == 2 && in[0].equals("/removeBlockedUser")) {
                    if (csc.removeBlockedUser(uid, in[1])) {
                        System.out.println("Messages from " + in[1] + " are no longer blocked");
                    }
                    else {
                        System.out.println("Error removing user from block list.");
                    }
                }

                else if (in.length == 1 && in[0].equals("/viewBlockedUsers")) {
                    ArrayList<User> blockedList = csc.viewBlockedUsers(uid);
                    if (!blockedList.isEmpty()) {
                        System.out.println("Users currently on your Block List:");
                        for(User u : blockedList)
                            System.out.println("  * " + u.getUsername());
                    }
                    else {
                        System.out.println("Error retrieving your Block List.");
                    }
                }

                else if (in.length == 2 && in[0].equals("/setPublicName")) {
                    if (csc.setPublicName(uid, in[1])) {
                        System.out.println("Public name set to: " + in[1]);
                    }
                    else {
                        System.out.println("Error setting public name.");
                    }
                }

                else if (in.length == 2 && in[0].equals("/acceptInviteToChannel")) {
                    if (csc.acceptInviteToChannel(uid, in[1])) {
                        System.out.println("You are now a member of channel: " + in[1]);
                    }
                    else {
                        System.out.println("Error accepting Channel invite. Please try again.");
                    }
                }

                else if (in.length == 2 && in[0].equals("/declineInviteToChannel")) {
                    if (csc.declineInviteToChannel(uid, in[1])) {
                        System.out.println("Declined invite to " + in[1] + " was successful.");
                    }
                    else {
                        System.out.println("Could not process invitation decline. Try again.");
                    }
                }

                else {
                    System.out.println("Command is invalid, misspelled, or has the wrong number of arguments.");
                }
            }
            else if (message.length() > 0) {
                client.sendMessage(message);
            }

//            else if (message.equalsIgnoreCase("viewPublicChannels")) {
//
//                System.out.println(csc.viewPublicChannels(uid));
//            }
//            else if (message.equalsIgnoreCase("viewPrivateChannels")) {
//
//                System.out.println(csc.viewPrivateChannels(uid));
//            }
//            else if (message.equalsIgnoreCase("viewInvitedChannels")) {
//
//                System.out.println(csc.viewInvitedChannels(uid));
//            }
//            else if (message.equalsIgnoreCase("addFriend")) {
//
//                //TODO - prompt for uid && username
//                boolean ret = csc.addFriend("1", "testUN1");
//                if (ret) System.out.println("Friend added. ");
//                else System.out.println("addFriend failed. ");
//            }
//            else if (message.equalsIgnoreCase("removeFriend")) {
//
//                //TODO - prompt for uid && username
//                boolean ret = csc.removeFriend("1", "testUN1");
//                if (ret) System.out.println("Friend removed. ");
//                else System.out.println("removeFriend failed. ");
//            }
//            else if (message.equalsIgnoreCase("viewFriends")) {
//
//                System.out.println(csc.viewFriends(uid));
//            }
//            else if (message.equalsIgnoreCase("addBlockedUser")) {
//                //TODO - prompt
//                boolean ret = csc.addBlockedUser("1", "testUN1");
//                if (ret) System.out.println("addBlockedUser success");
//                else System.out.println("addBlockedUser failed");
//            }
//            else if (message.equalsIgnoreCase("setPublicName")) {
//                //TODO - prompt
//                System.out.println(csc.setPublicName("1", "Batman"));
//            }
//            else if (message.equalsIgnoreCase("removeBlockedUser")) {
//                //TODO - prompt
//                boolean ret = csc.removeBlockedUser("1", "testUN1");
//                if (ret) System.out.println("removelockedUser success");
//                else System.out.println("removeBlockedUser failed");
//            }
//            else if (message.equalsIgnoreCase("viewBlockedUsers")) {
//                //TODO - prompt
//                System.out.println(csc.viewBlockedUsers(uid));
//            }
//            else if (message.equalsIgnoreCase("acceptInvite")) {
//                // TODO - prompt
//                boolean ret = csc.acceptInviteToChannel("1", "testChannel");
//                if(ret) System.out.println("Accept invite success.");
//                else System.out.println("Accept invite failed");
//            }
//            else if (message.equalsIgnoreCase("declineInvite")) {
//                //TODO - prompt
//                boolean ret = csc.declineInviteToChannel("1", "testChannel");
//                if(ret) System.out.println("Decline invite success.");
//                else System.out.println("Decline invite failed");
//            }
//            else {
//                client.sendMessage(message);
//            }


        }
        // logout user, close all connections.
        csc.logout(uid);
        client.disconnect();
    }


    class ClientListener extends Thread
    {
        public void run()
        {
            while (true) {
                try {
                    String input = (String) socketInput.readObject();
                    // if console mode print the message and add back the prompt

                    System.out.println(input);
                    System.out.print("> ");


                } catch (IOException e) {
                    System.out.println("Server has closed the connection: " + e);
                    break;
                }
                // can't happen with a String object but need the catch anyhow
                catch (ClassNotFoundException e2) {
                    System.out.println("Shouldn't happen.");
                }
            }
        }
    }
}
