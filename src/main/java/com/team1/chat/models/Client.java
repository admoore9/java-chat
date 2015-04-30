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
            if (socketInput != null)
                socketInput.close();
            if (socketOutput != null)
                socketOutput.close();
            if (socket != null)
                socket.close();
        } catch (IOException e) {
            System.out.println("Error closing client process. Error message: " + e);
        }
        csc.logout(userID);
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
        //Client client = new Client("104.236.206.121", 4444, uid);
        Client client = new Client("104.236.206.121", 4444, username, uid);

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
                    client.disconnect();
                    break;
                }

                else if (in.length == 2 && in[0].equals("/setUsername")) {
                    if (csc.setUsername(uid, in[1])) {
                        username = in[1];
                        System.out.println("Username has been set to " + username + ".");
                    }
                    else
                    {
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

                else if (in.length == 3 && in[0].equals("/inviteUserToChannel")) {
                    if (false) {

                    }
                    else {

                    }
                }

                else if (in.length == 3 && in[0].equals("/removeUserFromChannel")) {
                    if (false) {

                    }
                    else {

                    }
                }

                else if (in.length == 1 && in[0].equals("/viewPublicChannels")) {
                    if (false) {

                    }
                    else {

                    }
                }

                else if (in.length == 1 && in[0].equals("/viewPrivateChannels")) {
                    if (false) {

                    }
                    else {

                    }
                }

                else if (in.length == 1 && in[0].equals("/viewInvitedChannels")) {
                    if (false) {

                    }
                    else {

                    }
                }

                else if (in.length == 2 && in[0].equals("/toggleChannelVisibility")) {
                    if (false) {

                    }
                    else {

                    }
                }

                else if (in.length == 2 && in[0].equals("/addFriend")) {
                    if (false) {

                    }
                    else {

                    }
                }

                else if (in.length == 2 && in[0].equals("/removeFriend")) {
                    if (false) {

                    }
                    else {

                    }
                }

                else if (in.length == 1 && in[0].equals("/viewFriends")) {
                    if (false) {

                    }
                    else {

                    }
                }

                else if (in.length == 2 && in[0].equals("/addBlockedUser")) {
                    if (false) {

                    }
                    else {

                    }
                }

                else if (in.length == 2 && in[0].equals("/removeBlockedUser")) {
                    if (false) {

                    }
                    else {

                    }
                }

                else if (in.length == 1 && in[0].equals("/viewBlockedUsers")) {
                    if (false) {

                    }
                    else {

                    }
                }

                else if (in.length == 2 && in[0].equals("/setPublicName")) {
                    if (false) {

                    }
                    else {

                    }
                }

                else if (in.length == 2 && in[0].equals("/acceptInviteToChannel")) {
                    if (false) {

                    }
                    else {

                    }
                }

                else if (in.length == 2 && in[0].equals("/declineInviteToChannel")) {
                    if (false) {

                    }
                    else {

                    }
                }

                else {
                    System.out.println("Command is invalid, misspelled, or has the wrong number of arguments.");
                }
            }
            else if (message.length() > 0) {
                client.sendMessage(message);
            }
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
                    System.out.println("Server has close the connection: " + e);
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