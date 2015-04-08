package com.team1.chat.models;

import java.net.*;
import java.io.*;


/*
    Class for a server-side thread interface
 */
public class ServerThread extends Thread
{
    private Server server = null;
    private Socket socket = null;
    private int id = -1;
    private DataInputStream inputStream = null;
    private DataOutputStream outputStream = null;


    /*
        Constructor for the master server thread, calls the Thread superclass library , then initializes.

        A client thread can interface with the server, and other clients through this class.
     */
    public ServerThread(Server server, Socket socket)
    {
        super();
        this.server = server;
        this.socket = socket;

        id = socket.getPort();
    }

    /*
       Method for server thread to send message
     */
    public void sendMessage(String message)
    {
        try {
            outputStream.writeUTF(message);
            outputStream.flush();
        } catch (IOException e) {
            System.out.println("Error sending message from user: " + id + "Error is: " + e.getMessage());

            // If there is an send error, likely the socket failed,
            // remove the user and kill their thread.
            server.logoff(id);
            interrupt();
        }
    }

    /*
        getter for thread ID
     */
    public long getId()
    {
        return id;
    }

    /*
        Infinite "listener." Will keep listening and sending messages
     */
    public void run()
    {
        System.out.println("ServerThread: " + id + " is running.");

        while (true) {
            try {
                server.sendMessage(id, inputStream.readUTF());
            } catch (IOException e) {
                System.out.println(id + " Error reading: " + e.getMessage());
                server.logoff(id);
                interrupt();
            }
        }
    }

    /*
        Initialize and open the input buffer and output stream(s)
     */
    public void open() throws IOException
    {
        inputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        outputStream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
    }

    /*
        Close out socket, input & output
     */
    public void close() throws IOException
    {
        if (socket != null)
            socket.close();
        if (inputStream != null)
            inputStream.close();
        if (outputStream != null)
            outputStream.close();
    }
}