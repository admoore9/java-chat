/**
 * Created by danielbardin on 4/6/15.
 */

import java.net.*;
import java.io.*;
import java.util.ArrayList;

/*
    Class to implement the server-side functionality and main method for a chat service
 */
public class Server implements Runnable
{
    // ArrayList of client/thread objects
    private ArrayList<ServerThread> clients = new ArrayList<ServerThread>();

    // This server's socket
    private ServerSocket server = null;

    // This server's main thread;
    private Thread thread = null;

    // How many clients are on this channel
    private int numClients = 0;

    // Maximum number of clients allowed on this server
    private static final int MAX_CLIENTS = 25;


    /*
        Constructor
     */
    public Server(int port)
    {
        try {
            // Prompt user of port connection attempt
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
         Start up
     */
    public void run()
    {
        while (thread != null) {
            try {
                System.out.println("Waiting for first client connection. ");
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

//    /*
//        Terminate client thread
//     */
//    public void interrupt()
//    {
//        if (!thread.isInterrupted()) {
//            thread.interrupt();
//            thread = null;
//        }
//    }

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
            // Loops the clients array and posts the message to each.
            for (ServerThread st : clients) {
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

            numClients--;

            try {
                clients.get(clientsArrPos).close();
                clients.remove(clientsArrPos);
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
