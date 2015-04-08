package com.team1.chat.models;


import java.net.*;
import java.io.*;
import java.util.ArrayList;

/*
    Class to implement the server-side functionality and main method for a chat service
 */
public class Server implements Runnable
{
    private ChatServiceController csc;
    private ArrayList<ServerThread> clients = new ArrayList<ServerThread>();
    private ServerSocket server = null;
    private Thread thread = null;
    private int numClients = 0;

    // Maximum number of clients allowed on this server
    private static final int MAX_CLIENTS = 25;


    /*
        Constructor
     */
    public Server(int port)
    {

        try {
            // Prompt of port connection attempt
            System.out.println("Trying to connect to port: " + port);

            // make the connection to the port on our server
            server = new ServerSocket(port);

            // prompt user of connection success.
            System.out.println("Connection successful.");

            // start up the client
            start();

        } catch (IOException e) {
            // Problem connecting
            System.out.println("Could not attach to port: " + port + " The error was: " + e.getMessage());
        }
    }

    /*
         Listen for incoming connections
     */
    public void run()
    {
        while (thread != null) {
            try {
                System.out.println("Listening for client connections. ");
                addClient(server.accept());
            } catch (IOException e) {
                System.out.println("Server run() error: " + e);
            }
        }
    }


    /*
        Activate client thread
     */
    public void start()
    {
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }


    /*
        Find the client attempting to connect
     */
    private int findClient(int id)
    {
        for (int i = 0; i < numClients; i++) {
            if (clients.get(i).getId() == id)
                return i;
        }
        return -1;
    }


    /*
        Post a message to all users in this channel(i.e., host@port) from this id
     */
    public synchronized void sendMessage(int id, String message)
    {
        // logmeout phrase will exit a person from the channel
        if (message.equals("logmeout")) {
            logoff(id);
        }
        else {
            // Loops the clients array and posts the message to each that is not sender.
            for (ServerThread st : clients) {
                if(st.getId() != id)
                    st.sendMessage(id + ": " + message);
            }
        }
    }


    /*
        Remove a user from the active roster and close their connection
     */
    public synchronized void logoff(int id)
    {
        int clientsArrPos = findClient(id);

        if (clients.get(clientsArrPos).getId() == id) {

            try {

                clients.get(clientsArrPos).close();
                numClients--;

            }
            catch (IOException e) {
                System.out.println("Failed to log client off. Error message: " + e);
            }
        }
    }

    /*
        Add a client thread to the server
     */
    private void addClient(Socket socket)
    {
        // make sure clients is < 25 connections
        if (numClients < MAX_CLIENTS) {
            System.out.println("Client socket connection valid: " + socket);

            // Add a client thread to clients roster of active users.
            clients.add(new ServerThread(this, socket));

            try {
                clients.get(numClients).open();
                clients.get(numClients).start();
                numClients++;
            }
            catch (IOException e) {
                System.out.println("Error opening client thread: " + e);
                clients.remove(clients.size()-1);
            }
        }
        else {
            System.out.println("Sorry, this channel is full.");
        }
    }

    /*
        MAIN METHOD
     */
    public static void main(String args[])
    {
        Server server = null;

        if (args.length != 1)
            System.out.println("Usage: java Server port");
        else
            server = new Server(Integer.parseInt(args[0]));
    }
}
