/**
 * Created by danielbardin on 4/6/15.
 */

import java.net.*;
import java.io.*;

public class ClientThread extends Thread
{

    private Socket socket = null;
    private Client client = null;
    private BufferedReader inputBuffer = null;

    public ClientThread(Client client, Socket socket)
    {
        this.client = client;
        this.socket = socket;
        open();
        start();
    }

    /*
        Connects this client via socket and creates a input reader
     */
    public void open()
    {
        try {
            inputBuffer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.out.println("Error getting input stream: " + e);
            client.stop();
        }
    }

    public void close()
    {
        try {
            if (inputBuffer != null)
                inputBuffer.close();
        }
        catch (IOException e) {
            System.out.println("Error closing input stream: " + e);
        }
    }

    /*
        Listener for client in infinite loop. Terminates when "logmeoff" read by server during message send
     */
    public void run()
    {
        while (true) {
            try {
                client.sendMessage(inputBuffer.readLine());
            } catch (IOException e) {
                System.out.println("Listening error: " + e.getMessage());
                client.stop();
            }
        }
    }
}
