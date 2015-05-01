package com.team1.chat.models;

import java.net.*;
import java.io.*;
import java.util.ArrayList;

/*
    Class to implement the server-side functionality and main method for a chat service
 */
public class Server
{
    private ArrayList<ClientThread> clients;
    private int port;
    private int numClients = 0;
    private boolean running;

    /*
        Constructor
     */
    public Server(int port)
    {
        this.port = port;
        clients = new ArrayList<ClientThread>();
    }

    /*
        Activate client thread
     */
    public void start()
    {
        running = true;
        try {
            // initialize server's socket
            ServerSocket serverSocket = new ServerSocket(port);

            // listen for connections
            while (running) {

                System.out.println("Server listening for clients on port: " + port);

                // someone is trying to connect. accecpt the conn.
                Socket socket = serverSocket.accept();
                
                if (!running)
                    break;

                // add thread to master-list and send them to initialize.
                ClientThread ct = new ClientThread(socket);
                clients.add(ct);
                System.out.println("Client #" + numClients + " has been connected.");
                ct.start();
            }

            // stop running
            try {
                serverSocket.close();

                // loop array and terminate each thread
                for (ClientThread cls : clients) {
                    try {
                        cls.socketInput.close();
                        cls.socketOutput.close();
                        cls.socket.close();
                    } catch (IOException e) {
                        System.out.println("Error during shutdown: " + e);
                    }
                }
            } catch (Exception e) {
                System.out.println("Exception closing the server and clients: " + e);
            }
        } catch (IOException e) {
            System.out.println("Critical error creating new server socket: " + e);
        }
    }

    public synchronized void broadcast(String message)
    {
        // print to server
        System.out.println(message);

        // print to clients
        for (int i = clients.size()-1; i >= 0; --i) {

            // if sendMessage for a client fails, disconnect them from channel.
            if (!clients.get(i).sendMessage(message)) {

                System.out.println("Client: " + clients.get(i).username + " disconnected. Removing from active.");
                removeFromServerList(i);
            }
        }
    }

    public synchronized void removeFromServerList(int indexToRemove)
    {
        int i = 0;
        for (ClientThread s : clients) {
            i++;
            if (s.thread_ID == indexToRemove) {
                clients.remove(i);
                return;
            }
        }
    }

    /*
        MAIN METHOD
     */
    public static void main(String args[])
    {
        if (args.length != 1)
            System.out.println("Usage: java Server < port >");
        else {
            Server server = new Server(Integer.parseInt(args[0]));
            server.start();
        }
    }

    /*
            Internal class for a client thread.

            One instance per client.
     */
    class ClientThread extends Thread
    {
        Socket socket = null;
        ObjectInputStream socketInput;
        ObjectOutputStream socketOutput;
        int thread_ID;
        int numClients;
        String username;
        String message;

        /* Construct a new client thread */
        public ClientThread(Socket socket)
        {
            thread_ID = numClients += 1;
            this.socket = socket;

            // create data streams
            System.out.println("Thread setting up Object I/O streams.");
            try {
                socketOutput = new ObjectOutputStream(socket.getOutputStream());
                socketInput = new ObjectInputStream(socket.getInputStream());
                username = (String) socketInput.readObject();
                System.out.println("Thread connection successful.");
            } catch (IOException e) {
                System.out.println("Error creating new I/O streams");
            } catch (ClassNotFoundException e) {
                System.out.println("Class error " + e);
            }
        }


        /*
             Listener for client in infinite loop.
             Terminates when "logmeoff" read by server during message send
         */
        public void run()
        {
            boolean running = true;
            while (running) {

                // read string
                try {
                    message = (String) socketInput.readObject();
                } catch (IOException e) {
                    System.out.println(thread_ID + " error thrown reading stream. " + e);
                    break;
                } catch (ClassNotFoundException c) {
                    break;
                }
                // user sent logout message. Set flag, and break loop. Perform logout.
                if(message.contains("/logout")){
                    running = false;
                    continue;
                }
                // user changed screen name. set this.username to  username.
                if(message.contains("/changeName")){

                    String[] input = message.split(" ");
                    if(!input[1].isEmpty())
                        username = input[1];

                }
                else {
                    // renamed from sendMessage, less ambiguity for server/client methods.
                    // print to server
                    broadcast(username + ": " + message);
                }
            }
            removeFromServerList(thread_ID);
            close();
        }

        public void close()
        {

            try {
                if (socketOutput != null)
                    socketOutput.close();
            } catch (Exception e) {
                System.out.println("Closing output socket threw: " + e);
            }
            try {
                if (socketInput != null)
                    socketInput.close();
            } catch (Exception ec) {
                System.out.println("Closing input socket threw: " + ec);
            }
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (Exception ece) {
                System.out.println("Closing server socket threw: " + ece);
            }
        }

        public boolean sendMessage(String message)
        {
            // a client is no longer connected, close out their connection.
            if (!socket.isConnected()) {
                close();
                return false;
            }

            try {
                socketOutput.writeObject(message);
            } catch (IOException e) {
                System.out.println("Message failed in route to: " + username);
                System.out.println(e.toString());
            }
            return true;
        }

    }
}