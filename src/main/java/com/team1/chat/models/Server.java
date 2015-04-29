package com.team1.chat.models;


import javax.swing.*;
import java.net.*;
import java.io.*;
import java.util.ArrayList;

/*
    Class to implement the server-side functionality and main method for a chat service
 */
public class Server
{
    protected ChatServiceController csc;
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
        csc = new ChatServiceController();
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
        int i = 0;
        // print to clients
        for (ClientThread st : clients) {
            i++;
            // if sendMessage for a client fails, disconnect them from channel.
            if (!st.sendMessage(message)) {
                st.logout(i);
                System.out.println("Disconnected client: " + st.username + " removed from active clients.");
            }
        }
    }

    public synchronized void logout(int id)
    {
        int i = 0;
        for (ClientThread s : clients) {
            i++;
            if (s.uid == id) {
                s.logout(i);
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
        int uid;
        int numClients;
        String username;
        String message;

        /* Construct a new client thread */
        public ClientThread(Socket socket)
        {
            uid = numClients += 1;
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
                    System.out.println(uid + " error thrown reading stream. " + e);
                    break;
                } catch (ClassNotFoundException c) {
                    break;
                }

                // renamed from sendMessage, less ambiguity for server/client methods.
                // print to server
                broadcast(username + ": " + message);
            }
            logout(uid);
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


        public void logout(int uid)
        {
            // TODO - fix me
            // csc.logout();
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