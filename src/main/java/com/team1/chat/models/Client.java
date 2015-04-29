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
    private String serverName;
    private int serverPort;

    private ChatServiceController csc;

    /*
        Create client object
     */
    public Client(String serverName, int serverPort, String username)
    {
        this.serverName = serverName;
        this.serverPort = serverPort;
        this.username = username;
    }

    // GUI constructor here


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

//    /*
//         Route message to the console or the gui
//    */
//    private void display(String msg)
//    {
//        if (cg == null)
//            // println in console mode
//            System.out.println(msg);
//        else
//            // append to the gui JTextArea
//            cg.append(msg + "\n");
//    }

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
        // TODO - logout()?
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
        String uid = "";


        //Prompt user to login
        System.out.println("To login please enter: username password");
        while ((userInput = scanner.nextLine()) != null) {

            // split the string into separate username and password tokens.
            String[] in = userInput.split(" ");
            String username = in[0];
            String password = in[1];

            // call login method
            uid = csc.login(username, password);

            // if bad login, try again
            if ((uid==null)||uid.equals("")) {
                System.out.println("Username or Password not found. Please re-enter.");
            }
            else
                break;
        }


        // create new client
        //Client client = new Client("104.236.206.121", 4444, uid);
        Client client = new Client("localhost",4444,uid);
        
        // test we can connect
        if (!client.start())
            return;

        while (true) {
            System.out.print("> ");
            String message = scanner.nextLine();

            if (message.equalsIgnoreCase("logmeout")) {
                break;
            }
            else if (message.equalsIgnoreCase("viewPublicChannels")) {

                System.out.println(csc.viewPublicChannels(uid));
            }
            else if (message.equalsIgnoreCase("viewPrivateChannels")) {

                System.out.println(csc.viewPrivateChannels(uid));
            }
            else if (message.equalsIgnoreCase("viewInvitedChannels")) {

                System.out.println(csc.viewInvitedChannels(uid));
            }
            else if (message.equalsIgnoreCase("addFriend")) {

                //TODO - prompt for uid && username
                boolean ret = csc.addFriend("1", "testUN1");
                if (ret) System.out.println("Friend added. ");
                else System.out.println("addFriend failed. ");
            }
            else if (message.equalsIgnoreCase("removeFriend")) {

                //TODO - prompt for uid && username
                boolean ret = csc.removeFriend("1", "testUN1");
                if (ret) System.out.println("Friend removed. ");
                else System.out.println("removeFriend failed. ");
            }
            else if (message.equalsIgnoreCase("viewFriends")) {

                System.out.println(csc.viewFriends(uid));
            }
            else if (message.equalsIgnoreCase("addBlockedUser")) {
                //TODO - prompt
                boolean ret = csc.addBlockedUser("1", "testUN1");
                if (ret) System.out.println("addBlockedUser success");
                else System.out.println("addBlockedUser failed");
            }
            else if (message.equalsIgnoreCase("setPublicName")) {
                //TODO - prompt
                System.out.println(csc.setPublicName("1", "Batman"));
            }
            else if (message.equalsIgnoreCase("removeBlockedUser")) {
                //TODO - prompt
                boolean ret = csc.removeBlockedUser("1", "testUN1");
                if (ret) System.out.println("removelockedUser success");
                else System.out.println("removeBlockedUser failed");
            }
            else if (message.equalsIgnoreCase("viewBlockedUsers")) {
                //TODO - prompt
                System.out.println(csc.viewBlockedUsers(uid));
            }
            else if (message.equalsIgnoreCase("acceptInvite")) {
                // TODO - prompt
                boolean ret = csc.acceptInviteToChannel("1", "testChannel");
                if(ret) System.out.println("Accept invite success.");
                else System.out.println("Accept invite failed");
            }
            else if (message.equalsIgnoreCase("declineInvite")) {
                //TODO - prompt
                boolean ret = csc.declineInviteToChannel("1", "testChannel");
                if(ret) System.out.println("Decline invite success.");
                else System.out.println("Decline invite failed");
            }
            else {
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