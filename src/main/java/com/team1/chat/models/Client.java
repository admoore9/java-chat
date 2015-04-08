package com.team1.chat.models;


import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class Client implements Runnable
{
    private Socket socket = null;
    private Thread thread = null;
    private DataInputStream userConsole = null;
    private DataOutputStream outputStream = null;
    private ClientThread client = null;
    private String uid;


    /*
        Create client object
     */
    public Client(String serverName, int serverPort, String uid)
    {
        System.out.println("Connecting to server host: " + serverName + " at port: " + serverPort);
        try {
            socket = new Socket(serverName, serverPort);
            System.out.println("Connected: " + socket);
            this.uid = uid;
            start();
        } catch (UnknownHostException e) {
            System.out.println("Host unknown: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Unexpected exception: " + e.getMessage());
        }
    }

    /*
        create a new client thread
     */
    public void run()
    {
        while (thread != null) {
            try {
                outputStream.writeUTF(userConsole.readLine());
                outputStream.flush();
            } catch (IOException e) {
                System.out.println("Sending error: " + e.getMessage());
                stop();
            }
        }
    }


    /*
        method for a client to send a message
     */
    public void sendMessage(String message)
    {
        if (message.equals("logmeout")) {
            System.out.println("logmeout recognized. Press RETURN to exit.");
            stop();
        }
        else
            System.out.println(uid + ": " +  message);
    }

    /*
        initialize a client thread
     */
    public void start() throws IOException
    {
        userConsole = new DataInputStream(System.in);
        outputStream = new DataOutputStream(socket.getOutputStream());

        if (thread == null) {
            client = new ClientThread(this, socket);
            thread = new Thread(this);
            thread.start();
        }
    }

    /*
        Log out a client thread, shut down connections, close streams
     */
    public void stop()
    {
        if (thread != null) {
            thread.interrupt();
            thread = null;
        }
        try {
            if (userConsole != null)
                userConsole.close();
            if (outputStream != null)
                outputStream.close();
            if (socket != null)
                socket.close();
        } catch (IOException e) {
            System.out.println("Error closing client process. Error message: " + e);
        }
        client.close();
        client.interrupt();
    }


    public static void main(String args[]) throws IOException
    {
        ChatServiceController csc = new ChatServiceController();
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<Channel> channels;
        Client client = null;
        String userInput;
        String uid = "";


        //Prompt user to login
        System.out.println("To login please enter: username password");
        while ((userInput = console.readLine()) != null) {
            String[] in = userInput.split(" ");

            // call login method
            uid = csc.login(in[0], in[1]);

            // if bad login, try again
            if (uid.equals("")) {
                System.out.println("Username or Password not found. Please re-enter.");
                continue;
            }
            else
                break;
        }

        //login succeeded, now set up and launch a new client thread.
        boolean waitingToStart = true;
        System.out.println("Welcome " + uid + ". Please enter the desired option number.");
        System.out.println("1. View Channel Invites.");
        System.out.println("2. View Private Channels.");
        System.out.println("3. View Public Channels.");
        System.out.println("4. Launch in your default channel.");

        while(waitingToStart){

            while ((userInput = console.readLine()) != null);

            if (userInput.equals("1")) {
                channels = csc.viewInvitedChannels(uid);
                for (Channel c : channels) {
                    System.out.println(c.getName());
                }
            }
            else if (userInput.equals("2")) {
                channels = csc.viewPrivateChannels(uid);
                for (Channel c : channels) {
                    System.out.println(c.getName());
                }
            }
            else if (userInput.equals("3")) {
                channels = csc.viewPublicChannels(uid);
                for (Channel c : channels) {
                    System.out.println(c.getName());
                }
            }
            else if (userInput.equals("4")) {

                waitingToStart = false;
            }
        }

        // launch client
        // Iteration 3 - integrate channel selection
        client = new Client("104.236.206.121", 4444, uid);
    }


}