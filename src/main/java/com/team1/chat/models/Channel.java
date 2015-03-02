package com.team1.chat.models;

import com.team1.chat.interfaces.ChannelInterface;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class Channel implements ChannelInterface
{
    public boolean isRostered(String uid)
    {
        return false;
    }

    public ArrayList<User> listChannelUsers()
    {
        return null;
    }

    public boolean addChannelUser(String uid)
    {
        return false;
    }

    public boolean removeChannelUser(String uid)
    {
        return false;
    }

    public boolean isWhiteListed(String uid)
    {
        return false;
    }

    /*
        Channel listener
     */
    public class ChannelListener extends Thread
    {
        private String uid;
        private String username;
        private Socket socket;
        private BufferedReader input;
        private PrintWriter output;
        private ArrayList<PrintWriter> rosterWriters;

        // create new ChannelListener object for client requesting it.
        public ChannelListener(String uid, String username, Socket socket){

            this.uid = uid;
            this.username = username;
            this.socket = socket;
        }

        /*
            This creates a new thread for this unique client of this channel.
                - Sets the socket connection
                - Adds to list of ChannelUsers
                - Loops infinitely listening for text
                - sendsMessage to other Users of channel
         */
        public void start()
        {
            try{

                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                output = new PrintWriter(socket.getOutputStream(), true);

                // TODO synchronized(roster) client/server method here. Iteration 2.

                // check the connecting user is member of channel.
                if( !Channel.this.isRostered(uid))
                {
                    // If not on roster, tell them so and quit.
                    System.out.println("You were not found on Channel Roster. Disconnecting.");
                    return;
                }

                // Adds this client to list of writers in channel
                rosterWriters.add(output);

                while(true){
                    // read message
                    String message = input.readLine();

                    // check if the message is empty
                    if(message == null){
                        System.out.println("Cannot send empty message.");
                        continue;
                    }
                    sendMessage(username, message);
                }
            }
            catch(IOException e)
            {
                System.out.print("borked trying to connect to server.");
            }
            // Client disconnects, clean up
            finally
            {
                rosterWriters.remove(uid);
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        /*
            Distribute message to Channel members
         */
        public boolean sendMessage(String username, String msgText)
        {
            // loop all Channel writers and send this client's message.
            for(PrintWriter pw : rosterWriters){
                pw.println(username + ": " + msgText);
            }
            return true;
        }
    }
}
